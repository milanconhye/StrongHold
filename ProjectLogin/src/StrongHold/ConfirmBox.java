/*
-------------------------------------------------
|   Created by Milan Conhye                     |
|   University of Greenwich                     |
|                                               |
|   Website: www.milanconhye.com                |
|   GitHub: https://github.com/milanconhye      |
|                                               |
-------------------------------------------------

Copyright (c) 2016 Milan Conhye

* Permission to use, copy, modify, and distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

* The software is provided "as is" and the author disclaims all warranties with regard
to this software including all implied warranties of merchantability and fitness.
This software in no way claims to “fully” protect the integrity of the information stored.
In no event shall the author be liable for any special, direct, indirect, or consequential
damages or any damages whatsoever resulting from loss of use, data or profits, whether in
an action of contract, negligence or other tortious action, arising out of or in connection
with the use or performance of this software. Please acknowledge and agree to this agreement
before using this software.

*/

//Package Name
package StrongHold;

//Required Imports
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {

    static Stage window;

    //Initialize Yes or No answer
    static boolean isYesOrNo;

    public static boolean display(String message) {

        //Initialize Stage
        window = new Stage();

        //Prevent Action from other windows
        window.initModality(Modality.APPLICATION_MODAL);

        //Window properties
        window.setTitle(Configuration.companyName + " - Confirm Dialog");
        window.setMinWidth(260);
        window.setMinHeight(100);
        window.setResizable(false);
        window.centerOnScreen();

        //Initialize Components on screen
        Label messageLbl = new Label(message);
        Button yesBtn = new Button("Yes");
        Button noBtn = new Button("No");

        //Yes and No Button Actions
        yesBtn.setOnAction(e -> {
            isYesOrNo = true;
            window.close();
        });

        noBtn.setOnAction(e -> {
            isYesOrNo = false;
            window.close();
        });

        //Setting Layouts for components - VBox, HBox & BorderPane
        VBox messageLayout = new VBox(10);
        messageLayout.setAlignment(Pos.CENTER);
        messageLayout.getChildren().add(messageLbl);

        HBox answerLayout = new HBox(5);
        answerLayout.setAlignment(Pos.CENTER);
        answerLayout.getChildren().addAll(yesBtn, noBtn);

        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(10, 10, 10, 10));
        mainLayout.setTop(messageLayout);
        mainLayout.setBottom(answerLayout);

        //Create, set scene and add style sheet
        Scene scene = new Scene(mainLayout);
        scene.getStylesheets().add("/StrongHold/design/style.css");
        window.setScene(scene);

        //Display the window, before it returns it needs to be closed
        window.showAndWait();

        //Return an Answer
        return isYesOrNo;

    }

}
