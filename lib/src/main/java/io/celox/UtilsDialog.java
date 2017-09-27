/*
 * Copyright (c) 2017 kjtech.de - All Rights Reserved.
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */

package io.celox;

import com.jfoenix.controls.JFXButton;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Martin Pfeffer
 *         <a href="mailto:martin.pfeffer@celox.io">martin.pfeffer@celox.io</a>
 * @see <a href="https://celox.io">https://celox.io</a>
 */
public class UtilsDialog {

    private static final String TAG = "UtilsDialog";

    private static boolean sIsShowing = false;

    public static void showDialog(String title, String msg, String defaultStyle,
                                  @Nullable String customStyle, String... styledWords) {
        if (sIsShowing) return;

        Stage dialogStage = new Stage(StageStyle.UTILITY);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setWidth(400);
        dialogStage.setHeight(220);

        BorderPane borderPane = new BorderPane();

        borderPane.setPadding(new Insets(15));
        borderPane.setPrefWidth(Integer.MAX_VALUE);
        borderPane.setPrefHeight(Integer.MAX_VALUE);

        Scene scene = new Scene(borderPane);
        dialogStage.setScene(scene);
        sIsShowing = true;
        dialogStage.show();
        UtilsGui.closeOnEsc(borderPane, scene);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, t -> {
            if (t.getCode() == KeyCode.ESCAPE) {
                sIsShowing = false;
            }
        });

        // Top
        Text textTitle = new Text(title);
        textTitle.setStyle("-fx-font-size: 18px;");

        HBox hBoxTop = new HBox(10);
        hBoxTop.getChildren().addAll(textTitle);
        borderPane.setTop(hBoxTop);

        // Center
        TextFlow textFlow = new TextFlow();
        List<String> words = Arrays.asList(msg.split(" "));
        List<String> styledWordsList = Arrays.asList(styledWords);
        for (String word : words) {
            Text tmpWord = new Text(word);
            if (styledWordsList.contains(word
                    .replace(".", "")
                    .replace(",", "")
                    .replace("?", "")
                    .replace("!", "")
                    .replace(";", "")
                    .replace("\n", "")
            )) {

                tmpWord.setStyle(customStyle);
            } else {
                if (defaultStyle == null) {
                    tmpWord.setStyle("");
                } else {
                    tmpWord.setStyle(defaultStyle);
                }
            }
            tmpWord.setText(tmpWord.getText());
            textFlow.getChildren().add(tmpWord);
            textFlow.getChildren().add(new Text(" "));
        }
        Text textMsg = new Text(msg);
        textMsg.setStyle("-fx-font-size: 14px;");
        HBox hBoxInputPane = new HBox(10);
        hBoxInputPane.setAlignment(Pos.CENTER);

        VBox vBoxCenter = new VBox(10);
        vBoxCenter.setPadding(new Insets(25, 0, 15, 0));
        vBoxCenter.getChildren().addAll(textFlow);
        borderPane.setCenter(vBoxCenter);

        JFXButton btnOk = new JFXButton("OK");
        btnOk.setAlignment(Pos.CENTER_RIGHT);
        btnOk.setStyle("-fx-text-fill: WHITE; -fx-background-color: #5264AE; -fx-font-size: 14px;");
        btnOk.setOnAction(event -> {
            sIsShowing = false;
            dialogStage.close();
        });

        // Bottom
        HBox hBoxBottom = new HBox();
        final Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        hBoxBottom.getChildren().addAll(spacer, btnOk);
        borderPane.setBottom(hBoxBottom);

        // store on close
        dialogStage.setOnCloseRequest(event -> sIsShowing = false);
    }

    public static void showDialog(String title, String msg) {
        if (sIsShowing) {
            return;
        }

        Stage dialogStage = new Stage(StageStyle.UTILITY);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setWidth(400);
        dialogStage.setHeight(220);

        BorderPane borderPane = new BorderPane();

        borderPane.setPadding(new Insets(10));
        borderPane.setPrefWidth(Integer.MAX_VALUE);
        borderPane.setPrefHeight(Integer.MAX_VALUE);

        Scene scene = new Scene(borderPane);
        dialogStage.setScene(scene);
        sIsShowing = true;
        dialogStage.show();
        UtilsGui.closeOnEsc(borderPane, scene);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, t -> {
            if (t.getCode() == KeyCode.ESCAPE) {
                sIsShowing = false;
            }
        });

        // Top
        Label textTitle = new Label(title);
        textTitle.setStyle("-fx-font-size: 18px;");

        HBox hBoxTop = new HBox(5);
        hBoxTop.getChildren().addAll(textTitle);
        borderPane.setTop(hBoxTop);

        // Center
        Label textMsg = new Label(msg);
        textMsg.setStyle("-fx-font-size: 14px; -fx-font-family: monospace;");
        HBox hBoxInputPane = new HBox(10);
        hBoxInputPane.setAlignment(Pos.CENTER);

        VBox vBoxCenter = new VBox(10);
        vBoxCenter.setPadding(new Insets(25, 0, 15, 0));
        vBoxCenter.getChildren().addAll(textMsg);
        borderPane.setCenter(vBoxCenter);

        JFXButton btnOk = new JFXButton("OK");
        btnOk.setAlignment(Pos.CENTER_RIGHT);
        btnOk.setStyle("-fx-text-fill: WHITE; -fx-background-color: #388E3C; -fx-font-size: 14px;");
        btnOk.setOnAction(event -> {
            sIsShowing = false;
            dialogStage.close();
        });

        // Bottom
        HBox hBoxBottom = new HBox();
        final Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        hBoxBottom.getChildren().addAll(spacer, btnOk);
        borderPane.setBottom(hBoxBottom);

        // store on close
        dialogStage.setOnCloseRequest(event -> sIsShowing = false);
    }
}
