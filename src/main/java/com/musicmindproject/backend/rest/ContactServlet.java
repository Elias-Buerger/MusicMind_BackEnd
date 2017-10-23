package com.musicmindproject.backend.rest;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;
@WebServlet("contact")
public class ContactServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Parameters
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String comment = req.getParameter("comment");

        //Own data
        String mail = "musicmindproject@gmail.com";
        String pw = "PNnTz8My";

        //mailserver data
        String hostServer = "smtp.gmail.com";
        String hostPort = "587";

        //sending the email
        Properties prop = new Properties();
        prop.setProperty("mail.smtp.host", hostServer);
        prop.setProperty("mail.user", mail);
        prop.setProperty("mail.password", pw);

        Session session = Session.getDefaultInstance(prop);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(mail);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(mail));
            message.setSubject("Contact from " + name);
            message.setText("mail from "+ email + "\n" + comment);
            Transport.send(message);
        }
        catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
