package org.example.notification;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class email {

    public void send_email(String receiver, String title, String text){
        String to = "hanzehu1998@gmail.com";

        // Sender's email ID needs to be mentioned
        String from = "ruige306@gmail.com";

        // Assuming you are sending email from localhost
//        String host = "localhost";
        String host = "smtp.gmail.com";
        // Get system properties
        Properties properties = System.getProperties();
        properties.put("mail.smtp.port", "465");

        // Setup mail server
        properties.setProperty("mail.smtp.host",host);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the default Session object.
//        Session session = Session.getDefaultInstance(properties);
        Session session = Session.getInstance(properties, new javax.mail.Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication() {
                // use app application password here https://opensource.com/article/18/8/postfix-open-source-mail-transfer-agent Step 1
                return new PasswordAuthentication("ruige306@gmail.com", "wurhwxpfsaliudds");
            }
        });

        session.setDebug(true);

        try{
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("This is the Subject Line!");

            // Now set the actual message
            message.setText("This is actual message");

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
