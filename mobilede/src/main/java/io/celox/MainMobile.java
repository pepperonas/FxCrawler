package io.celox;

import com.jfoenix.controls.JFXButton;
import com.pepperonas.jbasx.log.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

        showDialog("", "");
    }

    public static void showDialog(String title, String msg) {
        Stage dialogStage = new Stage(StageStyle.UTILITY);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setWidth(500);
        dialogStage.setHeight(420);

        BorderPane borderPane = new BorderPane();

        borderPane.setPadding(new Insets(10));
        borderPane.setPrefWidth(Integer.MAX_VALUE);
        borderPane.setPrefHeight(Integer.MAX_VALUE);

        Scene scene = new Scene(borderPane);
        dialogStage.setScene(scene);
        UtilsGui.closeOnEsc(borderPane, scene);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, t -> {
            if (t.getCode() == KeyCode.ESCAPE) {
            }
        });

        // Top
        Label textTitle = new Label(title);
        textTitle.setStyle("-fx-font-size: 18px;");

        HBox hBoxTop = new HBox(5);
        hBoxTop.getChildren().addAll(textTitle);
        borderPane.setTop(hBoxTop);

        // Center
        Label labelContent = new Label(msg);
        labelContent.setStyle("-fx-font-size: 14px; -fx-font-family: monospace;");
        HBox hBoxInputPane = new HBox(10);
        hBoxInputPane.setAlignment(Pos.CENTER);

        VBox vBoxCenter = new VBox(10);
        vBoxCenter.setPadding(new Insets(25, 0, 15, 0));
        vBoxCenter.getChildren().addAll(labelContent);
        borderPane.setCenter(vBoxCenter);

        JFXButton btnOk = new JFXButton("OK");
        btnOk.setAlignment(Pos.CENTER_RIGHT);
        btnOk.setStyle("-fx-text-fill: WHITE; -fx-background-color: #388E3C; -fx-font-size: 14px;");
        btnOk.setOnAction(event -> {
            dialogStage.close();
        });

        // Bottom
        HBox hBoxBottom = new HBox();
        final Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        hBoxBottom.getChildren().addAll(spacer, btnOk);
        borderPane.setBottom(hBoxBottom);

        dialogStage.show();

        //        try {
        //            tryJsoup(labelContent);
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }

        try {
            tryJsoup2();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void tryJsoup2() throws IOException {
        //        Document doc = Jsoup.connect("https://suchen.mobile.de")
        //                .data("query", "Java")
        //                .userAgent("Mozilla")
        //                .cookie("auth", "token")
        //                .timeout(3000)
        //                .post();
        Document doc = Jsoup.connect("https://suchen.mobile.de/fahrzeuge/search.html?dam=0&isSearchRequest=true&ms=3500;45&vc=Car")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                .referrer("http://www.google.com")
                .timeout(3000)
                .get();

        Elements links = doc.getAllElements();

        int ctr = 0;
        for (Element link : links) {
            String linkHref = link.attr("href");
            String linkText = link.text();
            if (linkHref != null && !linkHref.isEmpty()) {
                Log.i(TAG, "link(" + (ctr++) + "): " + linkText + " --- " + linkHref);
            }
        }
    }

    private static void tryJsoup(Label label) throws IOException {
        String url = "https://www.google.com";
        print("Fetching %s...", url);

        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");

        StringBuilder builder = new StringBuilder();

        print("\nMedia: (%d)", media.size());
        builder.append(String.format("\nMedia: (%d)", media.size()));
        for (Element src : media) {
            if (src.tagName().equals("img")) {
                print(" * %s: <%s> %sx%s (%s)",
                        src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
                        trim(src.attr("alt"), 20));
                builder.append(String.format(" * %s: <%s> %sx%s (%s)",
                        src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
                        trim(src.attr("alt"), 20))).append("\n");
            } else {
                print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
                builder.append(String.format(" * %s: <%s>", src.tagName(), src.attr("abs:src"))).append("\n");
            }

        }

        print("\nImports: (%d)", imports.size());
        builder.append(String.format("\nImports: (%d)", imports.size()));
        for (Element link : imports) {
            print(" * %s <%s> (%s)", link.tagName(), link.attr("abs:href"), link.attr("rel"));
            builder.append(String.format(" * %s <%s> (%s)", link.tagName(), link.attr("abs:href"), link.attr("rel"))).append("\n");
        }

        print("\nLinks: (%d)", links.size());
        builder.append(String.format("\nLinks: (%d)", links.size()));
        for (Element link : links) {
            print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
            builder.append(String.format(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35))).append("\n");
        }
        label.setText(builder.toString());
    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width) {
            return s.substring(0, width - 1) + ".";
        } else {
            return s;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}