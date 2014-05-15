package org.frostbite.karren;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by frostbite on 13/05/14.
 */
public class Mailer {
    private Properties prop = new Properties();
    private Session session = Session.getDefaultInstance(prop);
    private Message msg = new MimeMessage(session);
    public Mailer(String botAddress, String emailHost, String emailPort){

    }

}
