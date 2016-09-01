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
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

//Please make sure you have the JavaMailAPI Installed: https://java.net/projects/javamail/pages/Home#Download_JavaMail_Release
public class EmailConfiguration {

    /// Configure these Settings to Send email ///

    /* WARNING: THE EMAIL ACCOUNT (USER_NAME & PASSWORD) CANNOT HAVE A 2-STEP AUTHENTICATION WHEN SENDING EMAILS.
    *  TIP: CREATE A TEMPORARY AND NON PERSONAL EMAIL INSTEAD.
    */
    private static String USER_NAME = "youremail@gmail.com";
    private static String PASSWORD = "your$TR@NGPass$";

    public static void emailUser(String emailID, String subjectEmail, String emailBody) {

        String from = USER_NAME;
        String pass = PASSWORD;
        String[] to = { emailID };
        String subject = subjectEmail;
        String body = emailBody;

        //You must configure these properties if you are not using GMail as a sender
        Properties properties = System.getProperties();
        String host = "smtp.gmail.com";
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.user", from);
        properties.put("mail.smtp.password", pass);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");

        //Create Session
        Session session = Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(session);

        //Attempting to send Message
        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            //Set Email Format and Message
            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();

        } catch (MessagingException me) {
            me.printStackTrace();
        }
    }
}
