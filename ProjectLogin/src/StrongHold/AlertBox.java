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
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {

    //StyleSheet Storage
    private final static String styleSheetStr = "/StrongHold/design/style.css";

    //Display requested Information
    public static void display(String message) {

        //Initialize Stage
        Stage window = new Stage();

        //Prevent Action from other windows
        window.initModality(Modality.APPLICATION_MODAL);

        //Set Properties for the Stage: window
        window.setTitle(Configuration.companyName + " - Alert");
        window.setMinWidth(250);
        window.setResizable(false);
        window.centerOnScreen();

        //Initialize Label and set to what program requests
        Label messageLbl = new Label(message);

        //Initialize Close Button and set Action
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> window.close());

        //Create Main layout for message box and set properties
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setAlignment(Pos.CENTER);

        //Add components to layout
        layout.getChildren().addAll(messageLbl, closeButton);

        //Create, set scene and add style sheet
        Scene scene = new Scene(layout);
        scene.getStylesheets().add(styleSheetStr);
        window.setScene(scene);

        //Display the window, before it returns it needs to be closed
        window.showAndWait();

    }

    //Display requested Information
    public static void displayMore(String message, String secondMessage) {

        //Initialize Stage
        Stage window = new Stage();

        //Prevent Action from other windows
        window.initModality(Modality.APPLICATION_MODAL);

        //Set Properties for the Stage: window
        window.setTitle(Configuration.companyName + " - Alert");
        window.setMinWidth(250);
        window.setResizable(false);
        window.centerOnScreen();

        //Initialize Label and set to what program requests
        Label messageLbl = new Label(message);
        Label secondMessageLbl = new Label(secondMessage);

        //Initialize Close Button and set Action
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> window.close());

        //Create Main layout for message box and set properties
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setAlignment(Pos.CENTER);

        //Add components to layout
        layout.getChildren().addAll(messageLbl, secondMessageLbl, closeButton);

        //Create, set scene and add style sheet
        Scene scene = new Scene(layout);
        scene.getStylesheets().add(styleSheetStr);
        window.setScene(scene);

        //Display the window, before it returns it needs to be closed
        window.showAndWait();

    }

}