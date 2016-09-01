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

//Required Import
import javafx.beans.property.*;

public class UserDetails {

    //Keep this the same as your database column names
    private final StringProperty email_id;
    private final StringProperty username;
    private final StringProperty password;
    private final IntegerProperty fullaccess;
    private final IntegerProperty accountlocked;

    //Convert the String and Integer to a Simple Property
    public UserDetails(String email_id, String username, String password, int fullaccess, int accountlocked) {
        this.email_id = new SimpleStringProperty(email_id);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.fullaccess = new SimpleIntegerProperty(fullaccess);
        this.accountlocked = new SimpleIntegerProperty(accountlocked);

    }

    //Getters and Setters
    public String getEmail_id() {
        return email_id.get();
    }

    public StringProperty email_idProperty() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id.set(email_id);
    }

    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public int getFullaccess() {
        return fullaccess.get();
    }

    public IntegerProperty fullaccessProperty() {
        return fullaccess;
    }

    public void setFullaccess(int fullaccess) {
        this.fullaccess.set(fullaccess);
    }

    public int getAccountlocked() {
        return accountlocked.get();
    }

    public IntegerProperty accountlockedProperty() {
        return accountlocked;
    }

    public void setAccountlocked(int accountlocked) {
        this.accountlocked.set(accountlocked);
    }

}
