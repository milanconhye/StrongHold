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

//Package name
package StrongHold;

//Required Imports
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;

public class SecureLogin extends Application {

    //Initialize Stage: window
    private Stage window;

    //Creating the Scenes
    private Scene loginScene, loginVerificationScene, accountLockedScene;

    //StyleSheet Storage
    private final String styleSheetStr = "/StrongHold/design/style.css";

    //ProgressTimer Storage
    private final String progressTimerStr = "assets/ProgressBar.gif";

    //Initialize Text Fields
    private TextField userNameTF = new TextField();
    private PasswordField passWordTF = new PasswordField();
    private TextField verificationTF = new TextField();

    //Initialize Buttons
    private Button loginBtn = new Button("Login");
    private Button verifyBtn = new Button("Verify");

    //Initialize username and password String
    private String userNameStr;
    private String passWordStr;

    //Limit the number of attempts for login
    private int totalAttempts = 4;

    public static void main(String[] args) {
        //Checks Internet Connection
        //...

        //Launches arguments
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {

        //Set window properties
        window = primaryStage;
        window.setTitle(Configuration.companyName + " - Secure Login");
        window.setResizable(false);
        window.centerOnScreen();

        //////////////////////////// Login Window ///////////////////////////

        //Get Logo from File
        File file = new File("assets/YourLogo.png");
        Image image = new Image(file.toURI().toString());
        ImageView strongHoldLogo = new ImageView();
        strongHoldLogo.setImage(image);

        //Create Layout for Logo
        VBox logoLayout = new VBox();
        logoLayout.setAlignment(Pos.CENTER);
        logoLayout.setPadding(new Insets(10, 0, 0, 0));
        logoLayout.getChildren().add(strongHoldLogo);

        //Initialize Local Labels for username and password
        Label userNameLbl = new Label("Username: ");
        Label passWordLbl = new Label("Password: ");

        //Initialize Layout and Set Main Properties
        GridPane layout = new GridPane();
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(10, 0, 0, 0));
        layout.setHgap(10);
        layout.setVgap(8);

        //Positions Layouts in appropriate places
        GridPane.setConstraints(userNameLbl, 0, 0);
        GridPane.setConstraints(userNameTF, 1, 0);
        GridPane.setConstraints(passWordLbl, 0, 1);
        GridPane.setConstraints(passWordTF, 1, 1);
        GridPane.setConstraints(loginBtn, 1, 2);

        //Adds Components to Layout
        layout.getChildren().addAll(userNameLbl, userNameTF,
                passWordLbl, passWordTF, loginBtn);

        //Initialize main layout, setting top and center
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(logoLayout);
        mainLayout.setCenter(layout);

        //Close Program Request
        window.setOnCloseRequest(e -> {

            //e.consume would prevent the close dialog from being bypassed
            e.consume();

            //Close program dialog
            closeProgram();
        });

        //Checks if fields are empty.
        loginBtn.disableProperty().bind(userNameTF.textProperty().isEqualTo("").or(passWordTF.textProperty().isEqualTo("")));

        //When login button is pressed
        loginBtn.setOnAction(e -> executeLogin());

        //Set First Scene and Style
        loginScene = new Scene(mainLayout, 315, 290);
        loginScene.getStylesheets().add(styleSheetStr);

        //Set Defaults
        window.setScene(loginScene);
        window.show();
    }

    //Close Program on Request
    private void closeProgram() {
        //Checks response and stores on a boolean
        Boolean isYesOrNo = ConfirmBox.display("Are you sure you want to close the program?");

        if (isYesOrNo) {

            //If entered username (stored in string) exists in database
            if (Configuration.verifyUserName(userNameStr)) {

                //Delete Verification Key
                Configuration.delVerification(userNameStr);
                System.out.println("Verification Key Deleted!");
                window.close();
            }

            window.close();
        }
    }

    //If loginButton has been executed
    private void executeLogin() {

        //Store the data from username and password text fields into String
        userNameStr = userNameTF.getText();
        passWordStr = passWordTF.getText();

        //Checks if account is locked before proceeding any further
        if (Configuration.isAccountLocked(userNameStr)) {
            lockedAccountWindow();
            window.setScene(accountLockedScene);
            System.out.println("Account Locked, wait for Timer!");

        } else {

            //Checks the number of attempts used
            if (totalAttempts != 1) {

                //Matches Typed Text with Database Stored Text
                if (Configuration.verifyUserName(userNameStr) && Configuration.verifyPasswordWithHash(userNameStr, passWordStr)) {

                    //Reset Total Attempts
                    totalAttempts = 4;

                    //Generate VerificationKey
                    Configuration.setVerification(userNameStr);

                    //Send Verification Email to the user Logged In
                    Configuration.sendVerificationEmail(userNameStr);

                    //Open loginVerificationWindow
                    loginVerificationWindow();
                    window.setScene(loginVerificationScene);

                } else {

                    //Clear Fields
                    userNameTF.clear();
                    passWordTF.clear();

                    //Decrease Number of Attempts and display error
                    totalAttempts--;
                    AlertBox.display("Wrong Username or Password. You have " + totalAttempts + " attempt(s) left!");
                }

            } else {

                //Send Warning Email to Admin
                Configuration.sendAccountLockEmail(userNameStr);

                //Lock account
                Configuration.setAccountLocked(userNameStr, true);

                //Go to Locked Account State
                lockedAccountWindow();
                window.setScene(accountLockedScene);
            }
        }
    }

    //One Minute Timer to enter Verification Key or Disable Account Lock
    private Timeline timeline;
    private void runTimer() {
        //if timer is finished before verification is entered
        timeline = new Timeline(new KeyFrame(Duration.seconds(60), ev -> {

            //Reset maximum number of attempts
            totalAttempts = 4;

            //Clear Text Fields
            userNameTF.clear();
            passWordTF.clear();
            verificationTF.clear();

            Configuration.delVerification(userNameStr);
            Configuration.setAccountLocked(userNameStr, false);
            window.setScene(loginScene);

            //Stop Timer
            timeline.stop();
            System.out.println("Timer Stopped!");

        }));

        //Play Timer
        timeline.play();
        System.out.println("Timer Started!");
    }

    //Login Verification Window
    private void loginVerificationWindow() {

        //Set Verification TextField Properties
        verificationTF.setPromptText("Enter Verification Key");
        verificationTF.setMaxWidth(150);
        verificationTF.setTooltip(new Tooltip("Enter the verification Key"));
        verificationTF.setFocusTraversable(false);

        //Set mainLayout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20, 20, 20, 20));

        //Set progressBar Layout
        VBox progressBarLayout = new VBox(5);
        progressBarLayout.setAlignment(Pos.CENTER);

        //Set VerificationLayout - TF & BTN
        HBox verificationLayout = new HBox(5);
        verificationLayout.setAlignment(Pos.CENTER);

        //Get Progress Bar GIF
        File file = new File(progressTimerStr);
        Image image = new Image(file.toURI().toString());
        ImageView progressBar = new ImageView();
        progressBar.setImage(image);

        //Set Timer Animation to top
        mainLayout.setTop(progressBarLayout);

        //Set the VerificationLayout to bottom
        mainLayout.setBottom(verificationLayout);

        //Checks if Verification field is empty
        verifyBtn.disableProperty().bind(verificationTF.textProperty().isEqualTo(""));

        //When the verification button is clicked
        verifyBtn.setOnAction(e -> executeVerification());

        //Start Countdown Timer
        runTimer();

        //Add components to layout
        progressBarLayout.getChildren().add(progressBar);
        verificationLayout.getChildren().addAll(verificationTF, verifyBtn);

        //Set Scene and add Style Sheet
        loginVerificationScene = new Scene(mainLayout, 450, 260);
        loginVerificationScene.getStylesheets().add(styleSheetStr);
    }

    //If verificationButton has been executed
    private void executeVerification() {

        //Checks the number of attempts used
        if (totalAttempts != 1) {

            if (verificationTF.getText().equals(Configuration.getVerification(userNameStr))) {

                //Reset maximum number of attempts
                totalAttempts = 4;

                //Delete Verification Key
                Configuration.delVerification(userNameStr);

                //Stop Timer
                timeline.stop();
                System.out.println("Timer Stopped!");

                //Check Super User or not -> go to Window!
                if (Configuration.hasFullAccess(userNameStr)) {

                    //Launch SuperUser Window
                    SuperUser.start();

                    //Close Current Window
                    window.close();

                } else {

                    //Replace your content with this window [remove YourContent class]
                    YourContent.start();

                    //Close Current Window
                    window.close();
                }

            } else {

                //Clear Field
                verificationTF.clear();

                //Decrease Number of Attempts and display
                totalAttempts--;
                AlertBox.display("Incorrect Verification Key. You have " + totalAttempts + " attempt(s) left!");

            }

        } else {

            //Stop Timer
            timeline.stop();
            System.out.println("Timer Stopped!");

            //Send Warning Email to Admin
            Configuration.sendAccountLockEmail(userNameStr);

            //Delete Verification Key
            Configuration.delVerification(userNameStr);

            //Lock account for maximum attempts
            Configuration.setAccountLocked(userNameStr, true);
            lockedAccountWindow();
            window.setScene(accountLockedScene);
        }
    }

    //Locked Account Window
    private void lockedAccountWindow() {

        //Delete Verification Key
        Configuration.delVerification(userNameStr);

        //Set mainLayout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20, 20, 20, 20));

        //Set progressBar Layout
        VBox progressBarLayout = new VBox(5);
        progressBarLayout.setAlignment(Pos.CENTER);

        //Set accountMessageLayout - TF & BTN
        HBox accountMessageLayout = new HBox(5);
        accountMessageLayout.setAlignment(Pos.CENTER);

        //Initialize Message
        Label accountMessage = new Label("Account or Login has been Temporary Locked");

        //Get Progress Bar GIF
        File file = new File(progressTimerStr);
        Image image = new Image(file.toURI().toString());
        ImageView progressBar = new ImageView();
        progressBar.setImage(image);

        //Set accountMessageLayout to top
        mainLayout.setTop(accountMessageLayout);

        //Set the Timer Animation to bottom
        mainLayout.setBottom(progressBarLayout);

        //Start Countdown Timer
        runTimer();

        //Add components to layout
        progressBarLayout.getChildren().add(progressBar);
        accountMessageLayout.getChildren().add(accountMessage);

        //Set Scene and add Style Sheet
        accountLockedScene = new Scene(mainLayout, 450, 260);
        accountLockedScene.getStylesheets().add(styleSheetStr);
    }
}