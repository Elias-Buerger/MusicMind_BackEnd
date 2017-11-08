package com.musicmindproject.backend.servlets;

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Properties;

@WebServlet("/contact")
public class ContactServlet extends HttpServlet {

    private static final String USERNAME = "musicmindproject@gmail.com";
    private static final String PASSWORD = "PNnTz8My";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");

        //Parameters
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String subject = req.getParameter("subject");
        String comment = req.getParameter("comment");

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
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(USERNAME, PASSWORD);
        }
    }
}
