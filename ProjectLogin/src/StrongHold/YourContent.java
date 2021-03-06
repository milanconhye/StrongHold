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

package StrongHold;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

//Replace your JavaFX, JavaSwing or any other content with this class.
public class YourContent {

    public static void start() {

        Stage window = new Stage();

        window.setTitle("Content Place Holder");
        window.centerOnScreen();

        StackPane layout = new StackPane();
        Label contentLabel = new Label("Place your content here");
        layout.getChildren().add(contentLabel);

        Scene scene = new Scene(layout, 400, 300);
        scene.getStylesheets().add("/StrongHold/design/style.css");
        window.setScene(scene);
        window.show();

    }

}
