package com.musicmindproject.backend.servlets;

import javax.json.JsonObject;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import java.io.IOException;
import java.util.Properties;

@Path("/contact")
public class ContactServlet {

    private static final String USERNAME = "musicmindproject@gmail.com";
    private static final String PASSWORD = "PNnTz8My";

    /**
     * sends an email from our email acc to our email acc with the data of the client
     * @param name
     * @param email
     * @param subject
     * @param comment
     * @throws ServletException
     * @throws IOException
     */
    @POST
    public void doPost(@FormParam("name") String name, @FormParam("email") String email, @FormParam("subject") String subject, @FormParam("comment") String comment) throws ServletException, IOException {
        //sending the email
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.port", 587);
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");

        Authenticator auth = new SMTPAuthenticator();
        Session session = Session.getInstance(props, auth);
        Transport transport = null;
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
            System.out.println("Successfully sent contact information");
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
         *
         * @return will return a password authentication
         */
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(USERNAME, PASSWORD);
        }
    }
}
