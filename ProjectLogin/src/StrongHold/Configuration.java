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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.sql.*;


public class Configuration {

    /* To Change these values to fit your database, you must make the following changes if required:
    * 1. Change the sourceURL to the URL of your Remote Database.
    * 2. Change the Username and Password to your database credentials (dbUsername & dbPassword).
    * 3. The Keyword 'usersDB' is associated with the Database table. (Non-Case Sensitive).
    * 4. Verify your table and change the values within 'Table Column Names' section.
    * 5. You should also consider changing the min and max value of the verification generator,
    *    this can be found within the setVerification() method.
    */

    // Choose Company Name - [This would replace the default to your own]
    public final static String companyName = "StrongHold";

    //Static connection is created to prevent any value change in creating connections and connecting to the database.
    private static Connection connection;
    private static Statement stmt;

    //Set username and password for your database
    private final static String dbUsername = "root";
    private final static String dbPassword = "toor";

    //Establish a connection
    final static String sourceURL = "jdbc:mysql://localhost:8889/projectLogin_db";

    //Database Connection
    static {

        //Standard code to open a connection and statement to the derby database
        try {

            //Load JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //Set the connections url, username and password
            connection = DriverManager.getConnection(sourceURL, dbUsername, dbPassword);
            stmt = connection.createStatement();

        } catch (Exception eX) {
            AlertBox.displayMore("It appears that there is no connection to the database server!",
                    "Please check your internet connection or contact your administrator immediately.");
        }

    }

    /// DB Table Column Names ///
    final static String emailTB = "email_id";
    final static String usernameTB = "username";
    final static String passwordTB = "password";
    final static String fullAccessTB = "fullaccess";
    final static String verificationTB = "verification";
    final static String accountLockedTB = "accountlocked";

    //Get the Email address from user
    public static String getEmail(String userName) {

        try {

            //Execute Query to getEmail
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM usersDB WHERE username = '" + userName + "'");

            //Returns Email or else Null
            if (resultSet.next()) {
                return resultSet.getString(emailTB);
            } else {
                System.out.println("Username does not exist");
                return null;
            }

        } catch (SQLException sqlError) {
            //Error is caught and handled
            System.out.println("Cannot get result for email");
            return null;
        }

    }

    //Gets Username from Database from TextField
    public static String getUsername(TextField userName) {

        try {

            //Execute Query to getUsername
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM usersDB WHERE username = '" + userName.getText() + "'");

            //Returns Username or else Null
            if (resultSet.next()) {
                return resultSet.getString(usernameTB);
            } else {
                return null;
            }

        } catch (SQLException sqlError) {
            //Error is caught and handled
            System.out.println("Cannot get result for username");
            return null;
        }
    }

    //Verify if Username Exists - Return Boolean
    public static boolean verifyUserName(String userName) {

        try {
            //Verify User
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM usersDB WHERE username = '" + userName + "'");
            return resultSet.next();

        } catch (SQLException sqlError) {
            //Error is caught and handled
            System.out.println("User was not a match");
            //sqlError.printStackTrace();
            return false;
        }
    }


    //Gets Password from Database from Selected User
    public static String getPassword(String userName) {

        try {

            //Execute Query to getPassword
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM usersDB WHERE username = '" + userName + "'");

            //Returns Username or else Null
            if (resultSet.next()) {
                return resultSet.getString(passwordTB);
            } else {
                System.out.println("Username does not exist");
                return null;
            }

        } catch (SQLException sqlError) {
            //Error is caught and handled
            System.out.println("Cannot get result for password");
            return null;
        }
    }

    //Checks weather user has full access
    public static boolean hasFullAccess(String userName) {

        try {

            //Execute Query to getAuth
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM usersDB WHERE username = '" + userName + "'");

            //Returns isFullAccess or else False
            if (resultSet.next()) {
                return resultSet.getBoolean(fullAccessTB);
            } else {
                System.out.println("Username does not exist");
                return false;
            }

        } catch (SQLException sqlError) {
            //Error is caught and handled
            System.out.println("Cannot get result for isFullAccess");
            return false;
        }

    }

    //Get any email with FillAccess permissions
    public static String getFullAccessEmail() {

        try {

            //Execute Query to getEmails
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM usersDB WHERE fullaccess = 1");

            while (resultSet.next()) {
                return resultSet.getString(emailTB);
            }

        } catch (SQLException sqlError) {
            //Error is caught and handled
            System.out.println("Cannot get result for FullAccess users with Emails");
            return null;
        }

        return null;
    }

    //Checks weather account is locked
    public static boolean isAccountLocked(String userName) {

        try {

            //Execute Query to getAuth
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM usersDB WHERE username = '" + userName + "'");

            //Returns isAccountLocked or else False
            if (resultSet.next()) {
                return resultSet.getBoolean(accountLockedTB);
            } else {
                System.out.println("Username does not exist");
                return false;
            }

        } catch (SQLException sqlError) {
            //Error is caught and handled
            System.out.println("Cannot get result for isAccountLocked");
            return false;
        }
    }

    //Changes the status of locked account
    public static void setAccountLocked(String userNameStr, boolean accountLockedBool) {

        //Convert the Boolean Permission to Int
        int accountLockedInt = convertPermissionToInt(accountLockedBool);

        //Store the Query within this String
        String updateAccountLocked = "UPDATE usersDB SET accountlocked = '" + accountLockedInt + "' WHERE username = '" + userNameStr + "'";

        try {

            //Update status of AccountLocked
            stmt.executeUpdate(updateAccountLocked);

        } catch (SQLException sqlError) {
            //Error is caught and handled
            System.out.println("Cannot set accountLocked on database");
            sqlError.printStackTrace();

        } catch (Exception ex) {
            //Error is caught and handled
            System.out.println("Cannot find User " + userNameStr);
        }

    }

    //Get Verification Key
    public static String getVerification(String userName) {

        try {

            //Execute Query to getVerification
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM usersDB WHERE username = '" + userName + "'");

            //Returns Username or else Null
            if (resultSet.next()) {
                return resultSet.getString(verificationTB);
            } else {
                System.out.println("Username does not exist");
                return null;
            }

        } catch (SQLException sqlError) {
            //Error is caught and handled
            System.out.println("Cannot Get Verification Key");
            return null;
        }

    }

    //Set Verification Key
    public static void setVerification(String userName) {

        //Set the minimum and maximum value for the generator
        int minimumGenValue = 1004383;
        int maximumGenValue = 929843148;

        //Secure Random makes it harder to predict the number
        SecureRandom random = new SecureRandom();

        //Gen Random Key and convert to String
        int ranGemInt = random.nextInt(maximumGenValue) + minimumGenValue;
        String ranGemStr = Integer.toString(ranGemInt);

        //Store the Query within this String
        String verificationKey = "UPDATE usersDB SET verification = '" + ranGemStr + "' WHERE username = '" + userName + "'";

        try {

            //Update Key
            stmt.executeUpdate(verificationKey);

        } catch (SQLException sqlError) {
            //Error is caught and handled
            System.out.println("Cannot Create Verification Key");
        } catch (Exception ex) {
            System.out.println("Cannot find User " + userName);
        }

    }

    //Delete Verification Key
    public static void delVerification(String userName) {

        //Store the Query within this String
        String deleteVerificationKey = "UPDATE usersDB SET verification = '" + "" + "' WHERE username = '" + userName + "'";

        try {

            //Delete Key
            stmt.executeUpdate(deleteVerificationKey);

        } catch (SQLException sqlError) {
            //Error is caught and handled
            System.out.println("Cannot Delete Verification Key");
        }
    }

    //Add New User
    public static void addUser(String emailID, String userName, String passWord, int fullAccess, int accountLocked) {

        //Hash passWord before placing on table
        String securePass = genHashWithSalt(passWord);

        //Convert the Boolean Permission to Int
        //int fullAccessInt = convertPermissionToInt(fullAccess);

        //Store the Query within this String
        String writeString
                = "INSERT INTO usersDB(email_id, username, password, fullaccess, accountLocked) VALUES('"
                + emailID + "', '" + userName + "', '" + securePass + "', '" + fullAccess + "', " + accountLocked + ")";

        try {
            //Add User
            stmt.executeUpdate(writeString);

        } catch (SQLException sqlError) {
            //Error is caught and handled
            System.out.println("Cannot Add User, Print Stack Trace for more details");
            sqlError.printStackTrace();
        }
    }

    //Update User
    public static void updateUser(String origEmailID, String emailID, String userName, String passWord, int fullAccess, int accountLocked) {

        //Hash passWord before placing on table
        String securePass = genHashWithSalt(passWord);

        //Store the Query's within various String
        String updateEmail = "UPDATE usersDB SET email_id = '" + emailID + "' WHERE email_id = '" + origEmailID + "'";;
        String updateUserName = "UPDATE usersDB SET username = '" + userName + "' WHERE email_id = '" + origEmailID + "'";
        String updatePassWord = "UPDATE usersDB SET password = '" + securePass + "' WHERE email_id = '" + origEmailID + "'";
        String updateAccess = "UPDATE usersDB SET fullaccess = '" + fullAccess + "' WHERE email_id = '" + origEmailID + "'";
        String updateLockedAccount = "UPDATE usersDB SET accountlocked = '" + accountLocked + "' WHERE email_id = '" + origEmailID + "'";

        try {
            //Update User - Username, Password and Access Level
            stmt.executeUpdate(updateEmail);
            stmt.executeUpdate(updateUserName);
            stmt.executeUpdate(updatePassWord);
            stmt.executeUpdate(updateAccess);
            stmt.executeUpdate(updateLockedAccount);

        } catch (SQLException sqlError) {
            //Error is caught and handled
            System.out.println("Cannot Update User, Print Stack Trace for more details");
            //sqlError.printStackTrace();
        }
    }

    //Delete user
    public static void deleteUser(String userName) {

        //Store the Query within this String
        String deleteUserStr = "DELETE FROM usersDB WHERE username = '" + userName + "'";

        try {
            //Delete User
            stmt.executeUpdate(deleteUserStr);

        } catch (SQLException sqlError) {
            //Error is caught and handled
            System.out.println("Cannot delete User " + userName);
        }
    }

    //Get all of the users
    public static ObservableList<UserDetails> getAllUsers() {
        ObservableList<UserDetails> users = FXCollections.observableArrayList(); //this is where you can place java objects inside

        try {

            // Execute query and store result in a ResultSet
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM usersdb");

            while (resultSet.next()) {

                //If you wish to modify the results, modify them in the Configuration Class under <Table Column Names>
                users.add(new UserDetails(resultSet.getString(emailTB), resultSet.getString(usernameTB),
                        resultSet.getString(passwordTB), resultSet.getInt(fullAccessTB),
                        resultSet.getInt(accountLockedTB)));
            }

            return users;

        } catch (SQLException ex) {
            System.err.println("Error " + ex);
        }

        return null;
    }

    //Convert the true/false into an Int
    public static int convertPermissionToInt(boolean fullAccess) {

        //Initialize variable
        int boolToIntAccess;

        //Checks if user has full access or not then converts to an Integer
        if (fullAccess) {
            boolToIntAccess = 1;
        } else {
            boolToIntAccess = 0;
        }

        return boolToIntAccess;
    }

    //Generate BCrypt with Salt - BCrypt Class
    public static String genHashWithSalt(String passWord) {

        //Hashes Password with Salt
        String generatedSecuredPasswordHash = BCrypt.hashpw(passWord, BCrypt.gensalt(12));
        return generatedSecuredPasswordHash;
    }

    //Verifies Manual Written Password with Hashed Password
    public static boolean verifyPasswordWithHash(String userName, String passWord) {

        //Gets the current Database Password
        String passwordDBStr = getPassword(userName);

        //Checks for a Match and stores it within a boolean
        Boolean verifyPassMatch = BCrypt.checkpw(passWord, passwordDBStr);
        if (verifyPassMatch) {
            System.out.println("Password is a Match");
            return true;
        } else {
            System.out.println("Incorrect Password");
            return false;
        }
    }

    //Send Secure Email for Verification
    public static void sendVerificationEmail(String userName) {

        //Get Email and store in String
        String sendEmailToStr = getEmail(userName);

        //Get Verification Key and store in String
        String getVerificationKey = getVerification(userName);

        //Get IP Address
        try {
            InetAddress thisIp = InetAddress.getLocalHost();
            thisIPAddressStr = thisIp.getHostAddress().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Get Operating System
        String getOperatingSystemStr = System.getProperty("os.name");

        //String Builder for the body of the email
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("A user has logged on to your " + companyName + " account, if this is you, please enter the " +
                "verification key within the " + companyName + " application. Didn't log in? please notify your administrator!\n");
        stringBuilder.append("Logged User: " + userName + "\n");
        stringBuilder.append("Logged IP Address: " + getIPAddress() + "\n");
        stringBuilder.append("Logged Operating System: " + getOperatingSystemStr + "\n");
        stringBuilder.append("Verification Key: " + getVerificationKey + "\n");

        //Send Email to User
        EmailConfiguration.emailUser(sendEmailToStr, "Verification Key: " + getVerificationKey, stringBuilder.toString());

    }

    //Send email if there has been changes made to the account
    public static void sendAccountChangesEmail() {

        //Get the administrators (any with full access at 1) email and store in String
        String sendEmailToAdminStr = getFullAccessEmail();

        //Send Email to Admin
        EmailConfiguration.emailUser(sendEmailToAdminStr, companyName + " Account changes...", "Changes has been made to one or more accounts" +
                ", ignore this email if you are aware of this activity.");

    }

    //Send Email to Admin for too many attempts
    public static void sendAccountLockEmail(String userName) {

        //Get the administrators (any with full access at 1) email and store in String
        String sendEmailToAdminStr = getFullAccessEmail();

        //Get IP Address
        try {
            InetAddress thisIp = InetAddress.getLocalHost();
            thisIPAddressStr = thisIp.getHostAddress().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Get Operating System
        String getOperatingSystemStr = System.getProperty("os.name");

        //String Builder for the body of the email
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("There has been an account breach to the user " + userName + ", and the account has been " +
                "temporary locked. Please take immediate action!\n");
        stringBuilder.append("Logged User: " + userName + "\n");
        stringBuilder.append("Logged IP Address: " + getIPAddress() + "\n");
        stringBuilder.append("Logged Operating System: " + getOperatingSystemStr + "\n");

        //Send Email to Admin
        EmailConfiguration.emailUser(sendEmailToAdminStr, "Something doesn't look right...", stringBuilder.toString());

    }

    //Find and Get IP Address and store it within the String
    static String thisIPAddressStr;

    //Find IP Address by reading line from a website
    public static void findIPAddress() {
        try {

            //Use this if you want to get external IP Address
            //InetAddress thisIp = InetAddress.getLocalHost();
            //thisIPAddressStr = thisIp.getHostAddress().toString();

            URL connection = new URL("http://checkip.amazonaws.com/");
            URLConnection con = connection.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            thisIPAddressStr = reader.readLine();

        } catch (Exception e) {
            //Error is caught and handled
            System.out.println("Could not find the IP address!");
        }
    }

    //Get IP Address
    public static String getIPAddress() {
        findIPAddress();
        return thisIPAddressStr;
    }

}
