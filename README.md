#StrongHold
<i>Login &amp; Account Management Forged with Encryption and Security - created using Java FX</i>

While technology rapidly grows, security becomes the absence for those who don't take it seriously. Whilst it is great to create cutting edge applications which revolutionize the way we think, create and manage; what has happened with protecting these applications from being hacked or the protection of the user's information? Security may be at present but it is not taken as a priority, this needs to change. The aim of this project is to provide companies or individuals an insight and working code in order to protect the integrity of their information or software applications. StrongHold intercepts nearly all attack methods, providing secure account management tools and protects your company software from espionage and social engineering.

<h3>A preview of how StrongHold works</h3>

//PLACE GIF HERE & YOUTUBE LINK

<h2><b>Requirements and Notes</b></h2>

This Application 'StrongHold' was created in JavaFX using 'IntelliJ IDEA', therefore when editing these files, you could use 'IntelliJ IDEA' to import the project folder or simply use your favorite and "Smart" IDE<b>(Java files can be found in /src/)</b>. All code is open-source, but a reference to 'Milan Conhye' would be much appreciated. You may change any asset files, simply just replace the items with your own. Adobe AfterEffect files are included if you wish to change the properties of the timer/progressbar <b>(found in /assets/)</b>. The CSS Design file can easily be adopted to your personal theme (found in /src/design/) and finally the required libraires can be updated and relinked<b>(found in /lib/)</b>.

As you may know, there are three types of Data Encryption: Hashing, Symmetric Methods and the Asymmetric Methods. Further informaton can be found <a href="http://datashieldcorp.com/2013/06/04/3-different-data-encryption-methods/" target="_blank">here</a>. 'StrongHold' uses a Hashing Algorithm called 'BCrypt' with Salt added, this algorithm is not as popular as the other hashing methods, however, this algorithm has known to become impenetrable to this day - obviously with the correct use<b>(the hashing class can be found within /src/StrongHold/BCrypt.java)</b>.

This project was created so that providing security for your personal or corperate use can easily be merged as one. Although StrongHold does claim to provide numerous layers of security, it is significant for you as a developer to improve upon these layers, making the layers more robust and efficant. However, before further implementing on the application, there are important changes that need to be made to the application so that it simply works is functioning correctly. Thus, there is a configuration manual provided below. Tips on how you make these levels stronger can be found <a href="https://crackstation.net/hashing-security.htm" target="_blank">here</a>.

'StrongHold' is compatible with Mac OSX, Linux and Windows. 

<h2><b>Configuration</b></h2>

In this section, I will be going through how you can easily migrate this project to your "Smart" IDE, configure the application such that it connects to your online database and how you can easily adopt the application to your requirements. Although 'StrongHold' uses drivers from MYSQL to power the database, you can use your own database client in which java supports. 

<h3>Project Migration</h3>

If you wish to carry on using 'IntelliJ IDEA', then you may skip this section, however, if you wish to use your own IDE such as NetBeans then carry on reading. 

1. Launch your IDE and create an empty project.
2. Within the <b>/src/</b> of your empty project, copy and paste the package 'StrongHold' from <b>/src/</b> to your destination.
3. Go to the root of your project folder, copy and paste the 'lib' and 'assets' folder to your destination.
4. Finally, relaunch your IDE and allow it to index and complile your project.
5. From here you can rename the Package 'StrongHold' to your custom name <b>(be sure to check rename all)</b>.

<h3>Creating your online database</h3>

It is adviced for you to create a new empty table, with the exact column names I will be providing you in this section. The reason for this is, it would give you less work in changing all these names within the project. You will need your database server (MAMP, XAMPP etc) in this section.

1. Create a new database and give it a relavant name to your project. 
2. Create a new table within the database and name it <b>usersDB</b>.
3. Take note of the source url which links to this database, and remember your database username and password.
4. Create your columns such that it matches the table below:

Column Names                                    | Data Type & Length
-------------                                   | -------------
email_id                                        | VARCHAR(100)
username                                        | VARCHAR(25)
password                                        | VARCHAR(100)
fullaccess                                      | TINYINT(1) OR BOOL(1)
accountlocked                                   | TINYINT(1) OR BOOL(1)
verification                                    | VARCHAR(15)

<h3>Changing the Database Connection [Source Code]</h3>

In this section, I will be showing you how you can link your database with the 'StrongHold' application. The instructions below will show you the mandatory changes providing that you are using MYSQL as the database server. If you are using something other than MYSQL then the same instructions apply to you, but you must find the correct library online and change the class name within code.

1. Open the Configuration.java file within the 'StrongHold' project.
2. Within the Configuration.java file, change the following code:

```java
//Change the value to your companyName
public final static String companyName = "StrongHold"
```

```java
//Change the value to your dbUsername and dbPassword 
private final static String dbUsername = "root";
private final static String dbPassword = "toor";
```

```java
//Change the value to your sourceURL
final static String sourceURL = "jdbc:mysql://localhost:8889/projectLogin_db";
```

```java
//Change this only if you are using something else other than MYSQL
Class.forName("com.mysql.jdbc.Driver");
```

<h3>Changing the SMTP Connection Details [Source Code]</h3>

'StrongHold' provides a two step verification when logging in, once logged in successfully, you are required to enter a verification code which is sent to your email account. Other emails are also sent throughout the application, such as, when the account is locked. In this section, I will be showing you how you can link your company email account to the StrongHold application, so that users and your self would receive emails of confirmations and important notices. Note that, while StrongHold uses GMail to send emails, with a simple configuration of the host and properties, you can customise it to your company email domain. 

1. Open the EmailConfiguration.java file within the 'StrongHold' project.
2. Within the Configuration.java file, change the following code:

```java
//Change the following values for the USER_NAME & PASSWORD
private static String USER_NAME = "youremail@gmail.com";
private static String PASSWORD = "your$TR@NGPass$";
```

```java
//You must configure these properties if you are not using GMail as a sender
Properties properties = System.getProperties();
String host = "smtp.gmail.com";
properties.put("mail.smtp.starttls.enable", "true");
properties.put("mail.smtp.host", host);
properties.put("mail.smtp.user", from);
properties.put("mail.smtp.password", pass);
properties.put("mail.smtp.port", "587");
properties.put("mail.smtp.auth", "true");
```

// THE BELOW IS NOT A FINAL DRAFT - More Coming Soon...
<h2>Licence and Agreement</h2>

The software is provided "as is" and the author disclaims all warranties with regard to this software including all implied warranties of merchantability and fitness. This software in no way claims to fully protect the integrity of your information, instead it protects the naked information from the human eye and makes it harder for hackers to decrypt. In no event shall the author be liable for any special, direct, indirect, or consequential damages or any damages whatsoever resulting from loss of use, data or profits, whether in an action of contract, negligence or other tortious action, arising out of or in connection with the use or performance of this software. Please acknowledge and agree to this agreement before downloading and using this software. 

<a rel="license" href="http://creativecommons.org/licenses/by-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-sa/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-sa/4.0/">Creative Commons Attribution-ShareAlike 4.0 International License</a>.
