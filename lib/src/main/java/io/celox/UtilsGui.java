package io.celox;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Nullable;

import javafx.beans.value.ChangeListener;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * @author Martin Pfeffer
 *         <a href="mailto:martin.pfeffer@celox.io">martin.pfeffer@celox.io</a>
 * @see <a href="https://celox.io">https://celox.io</a>
 */
public class UtilsGui {

    public static void closeOnEsc(Parent root, Scene scene) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, t -> {
            if (t.getCode() == KeyCode.ESCAPE) {
                System.out.println("-ESC- pressed, closing stage...");
                Stage sb = (Stage) root.getScene().getWindow();
                sb.close();
            }
        });
    }

    public static void makeTextFieldNumeric(JFXTextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public static ChangeListener<String> makeTextFieldDecimal(JFXTextField textField) {
        ChangeListener<String> changeListener = (observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                textField.setText(oldValue);
            }
        };
        textField.textProperty().addListener(changeListener);
        return changeListener;
    }

    public static void updateStageTitle(Connection connection, Stage primaryStage, String appName, int versionCode, @Nullable Label label) {
    }

    public static void displayReadableTimeStamp(JFXComboBox<String> comboBox) {
        comboBox.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
                    setText(sdf.format(new Date(Long.parseLong(item))));
                }
            }
        });
    }

    public static void displayReadableTimeStampFailSafe(JFXComboBox<String> comboBox) {
        comboBox.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    if (item.length() >= 13) {
                        item = item.substring(0, 13);
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
                    setText(sdf.format(new Date(Long.parseLong(item))));
                }
            }
        });
    }
}
