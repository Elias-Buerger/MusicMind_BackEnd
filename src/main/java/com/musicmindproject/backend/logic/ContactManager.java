package com.musicmindproject.backend.logic;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Properties;

public class ContactManager {

    private static ContactManager instance;

    public static ContactManager getInstance() {
        if (instance == null) {
            instance = new ContactManager();
        }
        return instance;
    }

    private final String USERNAME = "musicmindproject@gmail.com";
    private final String PASSWORD = "PNnTz8My";

    private ContactManager() {
    }

    public void sendEmail(String name, String email, String subject, String comment) throws ServletException, IOException {
        //sending the email
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", 587);
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");

        Authenticator auth = new SMTPAuthenticator();
        Session session = Session.getInstance(props, auth);
        Transport transport;
        try {
            transport = session.getTransport();
            MimeMessage message = new MimeMessage(session);
            message.setSubject("Comment by " + name + ": " + subject);
            message.setContent("Mail from : " + email + "\n" + comment, "text/plain");
            message.setFrom(new InternetAddress(USERNAME));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(USERNAME));
            transport.connect();
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private class SMTPAuthenticator extends javax.mail.Authenticator {
        /**
         * @return will return a password authentication
         */
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(USERNAME, PASSWORD);
        }
    }
}
