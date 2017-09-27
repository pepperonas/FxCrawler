package io.celox;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * @author Martin Pfeffer
 *         <a href="mailto:martin.pfeffer@celox.io">martin.pfeffer@celox.io</a>
 * @see <a href="https://celox.io">https://celox.io</a>
 */
public class MainMobile extends Application {

    private static final String TAG = "MainMobile";

    private MobileDeController mController;

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("resources/layout.fxml").openStream());
        mController = fxmlLoader.getController();

        Scene scene = new Scene(root);

        primaryStage.setTitle("Crawler App - Mobile.de");
        primaryStage.setScene(scene);
        primaryStage.show();

        UtilsGui.closeOnEsc(root, scene);

        //noinspection Duplicates
        root.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            final KeyCombination kbSwitchConnection = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN);

            public void handle(KeyEvent ke) {
                if (kbSwitchConnection.match(ke)) {
                    ke.consume();
                    mController.doAction();
                }
            }
        });

        UtilsGui.closeOnEsc(root, scene);

        root.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }

}