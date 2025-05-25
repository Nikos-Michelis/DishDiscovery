package com.reciperestapi.reciperestapi.security.email;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailService {


    private static final String SMTP_HOST = "Host type";
    private static final String SMTP_PORT = "Port";
    private static final String SMTP_USERNAME = "Your username";
    private static final String MAIL_ADMIN = "Your email";
    private static final String SMTP_PASSWORD = "Email application password";

    private Session session;

    public EmailService() {
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
            }
        });
    }

    public void sendOtpEmail(String to, String subject, String body) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(MAIL_ADMIN));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setContent(body, "text/html; charset=UTF-8");
        Transport.send(message);
    }

}
