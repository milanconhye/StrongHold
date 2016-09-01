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
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.mail.internet.InternetAddress;
import java.util.function.Function;

public class SuperUser {

    //Three Stages
    private static Stage window;
    private static Stage addStage;
    private static Stage editStage;

    //Initialize Data Type and Components
    private static String currentEmail;
    private static boolean hasChangesBeenMade;
    private static TextField emailField;
    private static TextField usernameField;
    private static TextField passwordField;
    private static CheckBox hasFullAccessCheckBox;
    private static CheckBox isAccountLockedCheckBox;

    //Initialize TableView and Column
    private static TableView<UserDetails> tableUser;
    private static TableColumn<UserDetails, UserDetails> editRow;

    //StyleSheet Storage
    private static final String styleSheetStr = "/StrongHold/design/style.css";

    //Main Window
    public static void start() {

        //Create new stage and set title
        window = new Stage();
        window.setTitle(Configuration.companyName + " - Super User");

        //Email Column and Properties
        TableColumn<UserDetails, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setResizable(false);
        emailColumn.setMinWidth(200);
        emailColumn.setCellValueFactory(new PropertyValueFactory<>(Configuration.emailTB));

        //Username Column and Properties
        TableColumn<UserDetails, Double> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setResizable(false);
        usernameColumn.setMinWidth(100);
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>(Configuration.usernameTB));

        //Password Column and Properties
        TableColumn<UserDetails, Integer> passwordColumn = new TableColumn<>("Password");
        passwordColumn.setResizable(false);
        passwordColumn.setMinWidth(200);
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>(Configuration.passwordTB));

        //FullAccess Column and Properties
        TableColumn<UserDetails, Integer> fullAccessColumn = new TableColumn<>("FullAccess");
        fullAccessColumn.setResizable(false);
        fullAccessColumn.setMinWidth(100);
        fullAccessColumn.setCellValueFactory(new PropertyValueFactory<>(Configuration.fullAccessTB));

        //Locked Column and Properties
        TableColumn<UserDetails, Integer> accountLockedColumn = new TableColumn<>("Locked");
        accountLockedColumn.setResizable(false);
        accountLockedColumn.setMinWidth(110);
        accountLockedColumn.setCellValueFactory(new PropertyValueFactory<>(Configuration.accountLockedTB));

        //AddRow Column and Properties
        TableColumn<UserDetails, Boolean> addRow = new TableColumn<>("Add");
        addRow.setResizable(false);
        addRow.setMinWidth(80);
        addRow.setSortable(false);

        //Define Boolean to stop buttons on empty rows
        addRow.setCellValueFactory(features -> new SimpleBooleanProperty(features.getValue() != null));
        //Add a button to each row, providing that the row is not null
        addRow.setCellFactory(personBooleanTableColumn -> new addRowControl(window, tableUser));

        //EditRow Column and Properties
        editRow = column("Edit", ReadOnlyObjectWrapper<UserDetails>::new, 80);
        editRow.setResizable(false);
        editRow.setSortable(false);
        editRowControl();

        //DeleteRow Column and Properties
        TableColumn<UserDetails, Boolean> deleteRow = new TableColumn<>("Delete");
        deleteRow.setResizable(false);
        deleteRow.setMinWidth(80);
        deleteRow.setSortable(false);

        //Define Boolean to stop buttons on empty rows
        deleteRow.setCellValueFactory(features -> new SimpleBooleanProperty(features.getValue() != null));
        //Add a button to each row, providing that the row is not null
        deleteRow.setCellFactory(personBooleanTableColumn -> new deleteRowControl(tableUser));

        //Create new TableView, SetItems and add all Columns
        tableUser = new TableView<>();
        tableUser.setItems(Configuration.getAllUsers());
        tableUser.getColumns().addAll(emailColumn, usernameColumn, passwordColumn, fullAccessColumn,
                accountLockedColumn, addRow, editRow, deleteRow);

        //Set Groups and scenes
        Group root = new Group();
        Scene scene = new Scene(root, 955, 350);
        scene.getStylesheets().add(styleSheetStr);

        //Initializing Layout
        BorderPane layout = new BorderPane();
        layout.setCenter(tableUser);

        //Binding Layout with Scene
        layout.prefHeightProperty().bind(scene.heightProperty());
        layout.prefWidthProperty().bind(scene.widthProperty());
        root.getChildren().add(layout);

        //Change placeholder when there is no database data
        Label placeHolder = new Label("It appears that you have nothing on your database!");
        tableUser.setPlaceholder(placeHolder);

        //Close Program Request
        window.setOnCloseRequest(e -> {

            //e.consume would prevent the close dialog from being bypassed
            e.consume();

            //Close program dialog
            closeProgram();
        });

        //Set Defaults
        window.setScene(scene);
        window.setResizable(false);
        window.show();
    }

    //Close Program on Request
    private static void closeProgram() {

        //Checks response and stores on a boolean
        Boolean isYesOrNo = ConfirmBox.display("Are you sure you want to close the program?");

        if (isYesOrNo) {

            //If changes has been made then send email to admin
            if (hasChangesBeenMade) {

                //Send Admin Email
                Configuration.sendAccountChangesEmail();
                window.close();
            }

            //Close window
            window.close();
        }
    }

    //Separate class for adding a button on a cell
    private static class addRowControl extends TableCell<UserDetails, Boolean> {

        //Initialize button
        Button addButton = new Button("Add");

        //Used to display prompt and getting selected row to make sure data is valid
        addRowControl(final Stage stage, final TableView userDetails) {
            addButton.setOnAction(event -> {
                addUserPrompt(stage);
                userDetails.getSelectionModel().select(getTableRow().getIndex());
            });
        }

        //If cell is empty then place no button.
        @Override protected void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                setGraphic(addButton);
            }
        }
    }

    //UserPrompt for adding Users
    private static void addUserPrompt(Stage parent) {

        //Initialize Label and adding style
        Label addUserLabel = new Label("Add User");
        addUserLabel.setStyle("-fx-font-size: 20px; -fx-alignment: center");

        //Initialize Button and layout; adding components to the layout
        Button closeButton = new Button("X");
        HBox topLeftLayout = new HBox(75);
        topLeftLayout.setAlignment(Pos.TOP_LEFT);
        topLeftLayout.getChildren().addAll(closeButton, addUserLabel);

        //Initialize TextFields and CheckBoxes
        emailField = new TextField();
        usernameField = new TextField();
        passwordField = new TextField();
        hasFullAccessCheckBox = new CheckBox();
        isAccountLockedCheckBox = new CheckBox();

        //Initialize gridPane and setting properties
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(16));

        //Create a grid for the data entry.
        grid.addRow(0, new Label("Email: "), emailField);
        grid.addRow(1, new Label("Username: "), usernameField);
        grid.addRow(2, new Label("Password: "), passwordField);
        grid.addRow(3, new Label("FullAccess: "), hasFullAccessCheckBox);
        grid.addRow(4, new Label("Locked: "), isAccountLockedCheckBox);

        //Initialize Button and set Layout
        Button submitButton = new Button("Submit");
        VBox bottomLayout = new VBox(5);
        bottomLayout.setAlignment(Pos.BOTTOM_CENTER);
        bottomLayout.getChildren().addAll(new Label("Note: Passwords are automatically encrypted."), submitButton);

        //Checks if fields are empty and disables submit button.
        submitButton.disableProperty().bind(emailField.textProperty().isEqualTo("")
                .or(usernameField.textProperty().isEqualTo("").or(passwordField.textProperty().isEqualTo(""))));

        //Set Constraints and Growth
        ColumnConstraints leftCol = new ColumnConstraints();
        leftCol.setHgrow(Priority.NEVER);
        leftCol.setHalignment(HPos.RIGHT);
        ColumnConstraints rightCol = new ColumnConstraints();
        rightCol.setHgrow(Priority.SOMETIMES);
        grid.getColumnConstraints().addAll(leftCol, rightCol);
        GridPane.setHalignment(submitButton, HPos.CENTER);

        //Set MainLayout and section the sub layouts
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(10, 10, 10, 10));
        mainLayout.setTop(topLeftLayout);
        mainLayout.setCenter(grid);
        mainLayout.setBottom(bottomLayout);

        //Initialize the addStage.
        addStage = new Stage();
        Scene scene = new Scene(mainLayout);
        scene.getStylesheets().add(styleSheetStr);

        //Required actions for the Buttons
        submitButton.setOnAction(e -> executeAdd());
        closeButton.setOnAction(e -> addStage.hide());

        //Set Stage Properties
        addStage.initOwner(parent);
        addStage.initModality(Modality.APPLICATION_MODAL);
        addStage.initStyle(StageStyle.UNDECORATED);
        addStage.setScene(scene);
        addStage.show();
    }

    //When the submit button is pressed on the add user form
    private static void executeAdd() {

        //Initialize Access and Locked accounts to 0
        int hasFullAccessInt = 0;
        int isAccountLockedInt = 0;

        //Checks weather CheckBox has been selected
        if (hasFullAccessCheckBox.isSelected()) hasFullAccessInt = 1;
        if (isAccountLockedCheckBox.isSelected()) isAccountLockedInt = 1;

        //If the entered username does not match the database username
        if (!usernameField.getText().equals(Configuration.getUsername(usernameField))) {

            //Checks Password Policy Method
            if (checkPasswordRequirements(passwordField) && isEmailAddressValid(emailField)) {

                //Record Account changes
                hasChangesBeenMade = true;

                //Stores UserFields onto database
                Configuration.addUser(emailField.getText(), usernameField.getText(),
                        passwordField.getText(), hasFullAccessInt, isAccountLockedInt);
                tableUser.setItems(Configuration.getAllUsers());

                //Display AlertBox and Hide
                AlertBox.display("User has successfully been updated!");

                //Close Stage
                addStage.close();
            }

        } else {

            //AlertBox - The username must be unique
            AlertBox.display("Username must be unique!");
        }

    }

    //Separate class for adding a button on a cell
    private static void editRowControl() {

        //Add Component to Cell Factory
        editRow.setCellFactory(col -> {

            //Initialize editButton on Cell
            Button editButton = new Button("Edit");

            //Creates new TableCell from the class UserDetails and Places Component on the cell
            TableCell<UserDetails, UserDetails> editCell = new TableCell<UserDetails, UserDetails>() {
                @Override
                public void updateItem(UserDetails userDetails, boolean empty) {
                    super.updateItem(userDetails, empty);

                    //Checks where to place the component
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(editButton);
                    }
                }

            };

            //Set the editButton action
            editButton.setOnAction(e -> editUserPrompt(editCell.getItem(), window));
            return editCell;
        });
    }

    //Custom Formatting for the  Edit Column
    private static <S,T> TableColumn<S,T> column(String title, Function<S, ObservableValue<T>> property, double width) {
        TableColumn<S,T> col = new TableColumn<>(title);
        col.setCellValueFactory(cellData -> property.apply(cellData.getValue()));
        col.setPrefWidth(width);
        return col;
    }

    //Displays Edit User Form
    private static void editUserPrompt(UserDetails userDetails, Stage primaryStage) {

        //Initialize Label and adding style
        Label addUserLabel = new Label("Edit User");
        addUserLabel.setStyle("-fx-font-size: 20px; -fx-alignment: center");

        //Initialize Button and layout; adding components to the layout
        Button closeButton = new Button("X");
        HBox topLayout = new HBox(75);
        topLayout.setAlignment(Pos.TOP_LEFT);
        topLayout.getChildren().addAll(closeButton, addUserLabel);

        //Initializes TextField and CheckBoxes and sets the property depending on the cell selected
        currentEmail = userDetails.getEmail_id();
        emailField = new TextField(userDetails.getEmail_id());
        usernameField = new TextField(userDetails.getUsername());
        passwordField = new TextField(userDetails.getPassword());
        checkFullAccess(userDetails);
        checkLockedAccount(userDetails);

        //Initialize gridPane and setting properties
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(16));

        //Create a grid for the data entry.
        grid.addRow(0, new Label("Current Email: "), new Label(currentEmail));
        grid.addRow(1, new Label("New Email: "), emailField);
        grid.addRow(2, new Label("Username: "), usernameField);
        grid.addRow(3, new Label("Password: "), passwordField);
        grid.addRow(4, new Label("FullAccess: "), hasFullAccessCheckBox);
        grid.addRow(5, new Label("Locked: "), isAccountLockedCheckBox);

        //Initialize Button and set Layout
        Button submitButton = new Button("Submit");
        VBox bottomLayout = new VBox(5);
        bottomLayout.setAlignment(Pos.BOTTOM_CENTER);
        bottomLayout.getChildren().addAll(new Label("Note: Passwords are automatically encrypted."), submitButton);

        //Checks if fields are empty and disables submit button.
        submitButton.disableProperty().bind(emailField.textProperty().isEqualTo("")
                .or(usernameField.textProperty().isEqualTo("").or(passwordField.textProperty().isEqualTo(""))));

        //Set MainLayout and section the sub layouts
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(10, 10, 10, 10));
        mainLayout.setTop(topLayout);
        mainLayout.setCenter(grid);
        mainLayout.setBottom(bottomLayout);

        //Set Constraints and Growth
        ColumnConstraints leftCol = new ColumnConstraints();
        leftCol.setHgrow(Priority.NEVER);
        leftCol.setHalignment(HPos.RIGHT);
        ColumnConstraints rightCol = new ColumnConstraints();
        rightCol.setHgrow(Priority.SOMETIMES);
        grid.getColumnConstraints().addAll(leftCol, rightCol);
        GridPane.setHalignment(submitButton, HPos.CENTER);

        //Initialize the editStage.
        editStage = new Stage();
        Scene scene = new Scene(mainLayout);
        scene.getStylesheets().add(styleSheetStr);

        //Required actions for the Buttons
        closeButton.setOnAction(e -> editStage.hide());
        submitButton.setOnAction(e -> executeUpdate());

        //Set Stage Properties
        editStage.initModality(Modality.APPLICATION_MODAL);
        editStage.initOwner(primaryStage);
        editStage.initStyle(StageStyle.UNDECORATED);
        editStage.setScene(scene);
        editStage.show();
    }

    //When the submit button is pressed on the edit user form
    private static void executeUpdate() {

        //Initialize Access and Locked accounts to 0
        int hasFullAccessInt = 0;
        int isAccountLockedInt = 0;

        //Checks weather CheckBox has been selected
        if (hasFullAccessCheckBox.isSelected()) hasFullAccessInt = 1;
        if (isAccountLockedCheckBox.isSelected()) isAccountLockedInt = 1;

        //Checks Password Policy Method
        if (checkPasswordRequirements(passwordField) && isEmailAddressValid(emailField)) {

            //Record Account changes
            hasChangesBeenMade = true;

            //Update User Fields
            Configuration.updateUser(currentEmail, emailField.getText(), usernameField.getText(),
                    passwordField.getText(), hasFullAccessInt, isAccountLockedInt);
            tableUser.setItems(Configuration.getAllUsers());

            //Display AlertBox and Close Window
            AlertBox.display("User " + usernameField.getText() + " has successfully been updated!");
            editStage.close();

        }

    }

    //Separate class for adding a button on a cell
    private static class deleteRowControl extends TableCell<UserDetails, Boolean> {

        //Initialize button
        Button deleteButton = new Button("Delete");

        //Used to display prompt and getting selected row to make sure data is valid
        deleteRowControl(final TableView tableUser) {
            deleteButton.setOnAction(event -> {

                //Get index from table through a selected row
                tableUser.getSelectionModel().select(getTableRow().getIndex());

                //Store the rows information within UserDetails and displays prompt
                UserDetails user = (UserDetails) tableUser.getSelectionModel().getSelectedItem();
                boolean deleteConfirmation = ConfirmBox.display("Are you sure you want to delete the user " + user.getUsername());

                //Extracts the Username from table and stores it within a String
                String modelUsername = user.getUsername();

                if (deleteConfirmation) {

                    //Record Account changes
                    hasChangesBeenMade = true;

                    //Delete User using SQL Query within Configuration class
                    Configuration.deleteUser(modelUsername);

                    //Reset Table to view changes
                    tableUser.setItems(Configuration.getAllUsers());
                }

            });
        }

        //If cell is empty then place no button.
        @Override protected void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                setGraphic(deleteButton);
            }
        }
    }

    //Checks the status if user has full access by default
    private static void checkFullAccess(UserDetails userDetails) {

        //Create new CheckBox
        hasFullAccessCheckBox = new CheckBox();

        //Depending on the users permission, the check box will be selected if true
        if (userDetails.getFullaccess() == 1) hasFullAccessCheckBox.setSelected(true);
        else if (userDetails.getFullaccess() == 0) hasFullAccessCheckBox.setSelected(false);
    }

    //Checks the status if users has been locked
    private static void checkLockedAccount(UserDetails userDetails) {

        //Create new CheckBox
        isAccountLockedCheckBox = new CheckBox();

        //Depending on the locked status, the check box will be selected if true
        if (userDetails.getAccountlocked() == 1) isAccountLockedCheckBox.setSelected(true);
        else if (userDetails.getAccountlocked() == 0) isAccountLockedCheckBox.setSelected(false);

    }

    //Checks if password meets policy requirements
    private static boolean checkPasswordRequirements(TextField passwordField) {

        /// Strong Password Policy ///

        /*
         * ^                 # start-of-string
         * (?=.*[0-9])       # a digit must occur at least once
         * (?=.*[a-z])       # a lower case letter must occur at least once
         * (?=.*[A-Z])       # an upper case letter must occur at least once
         * (?=.*[@#$%^&+=])  # a special character must occur at least once
         * (?=\S+$)          # no whitespace allowed in the entire string
         * .{8,}             # anything, at least eight places though
         * $                 # end-of-string
         */

        //Store a password pattern within a String
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

        //Store the password from textfield into a String
        String userPassword = passwordField.getText();

        //If the password entered matches the conditions of a string then return true
        if (userPassword.matches(passwordPattern)) return true;
        else {

            //Create String Builder and append
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("* A number must occur at least once.\n");
            stringBuilder.append("* A lower case letter must occur at least once.\n");
            stringBuilder.append("* An upper case letter must occur at least once.\n");
            stringBuilder.append("* A special character must occur at least once.\n");
            stringBuilder.append("* No whitespace allowed.\n");
            stringBuilder.append("* Must be at least Eight Places.");

            //Displays Alert Box to user and returns false
            AlertBox.displayMore("Password does not meet policy requirements!", stringBuilder.toString());
            return false;
        }

    }

    //First Step Email Verification Uses JavaMail API
    public static boolean isEmailAddressValid(TextField emailField) {

        //Initialize boolean to check if email is valid
        boolean isValid = true;

        try {

            //Stores the email field within an InternetAddress Object
            InternetAddress emailAddressValid = new InternetAddress(emailField.getText());

            //Attempts to Validate the email address
            emailAddressValid.validate();

        } catch (javax.mail.internet.AddressException ex) {
            //Exception is caught if an error occurs, displays AlertBox and returns false
            AlertBox.display("Invalid Email Address!");
            isValid = false;
        }

        //returns the boolean of isValid
        return isValid;
    }

}
