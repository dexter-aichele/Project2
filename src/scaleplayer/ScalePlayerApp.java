package scaleplayer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.stream.IntStream;

/**
 * App which lets the user play scales.
 * @author Janet Davis
 * @author Maxwell Brown
 * @author Dexter Aichele
 * @author Trung Vu
 * @since 2017-01-26
 */
public final class ScalePlayerApp extends Application {

    /** the MIDI player associated with the app */
    private final MidiPlayer player = new MidiPlayer(4, 100);
    /** the last starting note used to play a scale within the app */
    private int lastStartingNote = 60;

    /**
     * Launches the Scale Player app.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     * Starts the app by setting up the primary stage.
     * @param stage the primary stage
     */
    @Override
    public void start(Stage stage) {
        BorderPane pane = new BorderPane();
        pane.setTop(createMenuBar());
        pane.setCenter(createControls());
        stage.setTitle("Scale Player");
        stage.setScene(new Scene(pane, 300, 250));
        stage.show();
    }

    /**
     * Creates the app menu bar (with File -> Exit functionality).
     * @return a menu bar
     */
    private MenuBar createMenuBar() {
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(__ -> Platform.exit());
        return new MenuBar(new Menu("File", null, exit));
    }

    /**
     * Creates the app controls (the 'Play scale' and 'Stop playing' buttons).
     * @return an HBox containing the app controls
     */
    private HBox createControls() {
        Button play = new Button("Play scale");
        play.setDefaultButton(true);
        play.setOnAction(__ -> promptForStartingNote().ifPresent(this::playMajorScale));
        Button stop = new Button("Stop playing");
        stop.setCancelButton(true);
        stop.setOnAction(__ -> player.stop());
        HBox controls = new HBox(10, play, stop);
        controls.setAlignment(Pos.CENTER);
        return controls;
    }

    /**
     * Prompts the user to enter a starting note via a blocking modal dialog.
     * @return the entered starting note, if the dialog was not cancelled
     */
    private Optional<Integer> promptForStartingNote() {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(lastStartingNote));
        dialog.setTitle("Starting note");
        dialog.setHeaderText("Give me a starting note (0–115):");
        dialog.getEditor().setTextFormatter(createDigitFormatter());
        ObjectBinding<Optional<Integer>> startingNote = Bindings.createObjectBinding(
                () -> tryParseInt(dialog.getEditor().getText())
                        .filter(note -> 0 <= note && note <= 115),
                dialog.getEditor().textProperty());
        dialog.getDialogPane().lookupButton(ButtonType.OK).disableProperty()
                .bind(startingNote.isEqualTo(Optional.empty()));
        return dialog.showAndWait()
                .flatMap(__ -> startingNote.getValue())
                .map(note -> lastStartingNote = note);
    }

    /**
     * Creates a TextFormatter which accepts only digits.
     * @return the new TextFormatter
     */
    private TextFormatter<Integer> createDigitFormatter() {
        return new TextFormatter<>(change -> {
            change.setText(change.getText().codePoints()
                    .filter(Character::isDigit)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint,
                            StringBuilder::append).toString());
            return change;
        });
    }

    /**
     * Tries to parse an integer.
     * @return the parsed integer, if valid
     */
    private static Optional<Integer> tryParseInt(String s) {
        try {
            return Optional.of(Integer.parseInt(s));
        } catch (NumberFormatException __) {
            return Optional.empty();
        }
    }

    /**
     * Plays a major scale, first ascending and then descending.
     * @param startingNote the starting note for the scale
     */
    private void playMajorScale(int startingNote) {
        player.stop();
        player.clear();
        IntStream.range(0, 8).forEach(beat -> {
            int note = startingNote + 2*beat - (beat + 1)/4;
            player.addNote(note, 80, 4*beat, beat == 7 ? 7 : 4, 0, 0);
            player.addNote(note, 80, 4*(18 - beat), beat == 0 ? 7 : 4, 0, 0);
        });
        player.play();
    }

}
