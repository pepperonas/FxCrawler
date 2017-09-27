package io.celox;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.pepperonas.jbasx.log.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * @author Martin Pfeffer
 *         <a href="mailto:martin.pfeffer@celox.io">martin.pfeffer@celox.io</a>
 * @see <a href="https://celox.io">https://celox.io</a>
 */
public class MobileDeController implements Initializable {

    private static final String TAG = "MobileDeController";

    private static final String DEFAULT_PHONE_NUMBER = "+49 6151 391 5916";
    private static final String ANDROID_DEVICE_IP = "192.168.178.81";
    private static final int PORT = 8123;
    private static final int TIMEOUT = 5000;

    private Socket mClientSocket;

    @FXML
    public Label label_error;
    @FXML
    public JFXButton btn_send;
    @FXML
    public JFXTextField tf_phone_number;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tf_phone_number.setText(DEFAULT_PHONE_NUMBER);
        tf_phone_number.setPromptText("Phone number");
        tf_phone_number.setLabelFloat(true);
        UtilsGui.makeTextFieldNumeric(tf_phone_number);

        startClient();
    }

    public void doAction() {

    }

    public void updateLabelStatus(String msg) {
        label_error.setText(msg);
    }

    private void sendMessage(String message) {
        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                try {
                    PrintWriter printWriter = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(mClientSocket.getOutputStream())), true);
                    printWriter.println(message);
                    Log.d(TAG, "sent: " + message);

                    BufferedReader in = new BufferedReader(new InputStreamReader(mClientSocket.getInputStream()));
                    String response = in.readLine();
                    Log.i(TAG, "resp: " + response);

                    Platform.runLater(() -> updateLabelStatus(response));

                } catch (Exception e) {
                    Platform.runLater(() -> updateLabelStatus("Something went wrong..."));
                }
                return null;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    private void startClient() {
        try {
            InetAddress inetAddress = InetAddress.getByName(ANDROID_DEVICE_IP);
            mClientSocket = new Socket(inetAddress, PORT);
            mClientSocket.setSoTimeout(TIMEOUT);
        } catch (IOException e) {
            Platform.runLater(() -> updateLabelStatus("Error: " + e.getMessage()));
        }
    }

    @FXML
    public void onBtnSend(@SuppressWarnings("unused") ActionEvent actionEvent) {
        sendMessage(tf_phone_number.getText());
    }

    @FXML
    public void onBtnHelp(@SuppressWarnings("unused") ActionEvent actionEvent) {
        UtilsDialog.showDialog("Help",
                "CTRL+Q:\tchange background color\n" +
                        "ESC:\tclose window ");
    }

}