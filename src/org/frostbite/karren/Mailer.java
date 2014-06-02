package org.frostbite.karren;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Created by frostbite on 13/05/14.
 */
public class Mailer {
    private Properties prop = new Properties();
    private Session session;
    private Message msg;
    public Mailer(BotConfiguration botConf) throws UnsupportedEncodingException, MessagingException {
        final String address = botConf.getEmailAddress();
        final String password = botConf.getEmailPassword();
        prop.put("mail.smtp.host", botConf.getSmtpServer());
        prop.put("mail.smtp.from", botConf.getEmailAddress());
        prop.put("mail.smtp.port", "25");
        prop.put("mail.smtp.auth", true);
        session = Session.getDefaultInstance(prop, new Authenticator(){
            protected PasswordAuthentication getPasswordAuthenication(){
                return new PasswordAuthentication(address, password);
            }
        });
        msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(botConf.getEmailAddress(), botConf.getBotname()));

    }
    public void sendMail(String sendTo, String body, String subject) throws UnsupportedEncodingException, MessagingException {
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(sendTo, "RDG User"));
        msg.setSubject(subject);
        msg.setText(body);
        Transport.send(msg);
    }
}
