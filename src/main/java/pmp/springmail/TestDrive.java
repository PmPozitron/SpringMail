/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pmp.springmail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 *
 * @author Pozitron
 */
public class TestDrive {

    MailSender sender;
    SimpleMailMessage template;

    final static String TO = "oldlamer@mail333.com";
//    final static String TO = "pmpozitron@gmail.com";
    final static String FROM = "pmpozitron@gmail.com";
//    final static String FROM = "oldlamer@mail333.com";
    final static String USER = "pmpozitron@gmail.com";
//    final static String USER = "oldlamer@mail333.com";    
    final static String PASS = "incorrect";
    
    final static String HOST = "smtp.gmail.com";
    final static String PORT = "25";
    final static String MAIL_SMTP_AUTH_KEY = "mail.smtp.auth";
    final static String MAIL_SMTP_START_TLS_KEY = "mail.smtp.starttls.enable";
    final static String MAIL_SMTP_HOST_KEY = "mail.smtp.host";
    final static String MAIL_SMTP_PORT_KEY = "mail.smtp.port";
    final static Properties props = new Properties();
    
    final static DateFormat DATE_FORMAT = new SimpleDateFormat("dd/mm/yy HH:mm");
    final static String CONTEXT_FILE_NAME = "context.xml";
    
    

    static {
        props.put(MAIL_SMTP_AUTH_KEY, true);
        props.put(MAIL_SMTP_START_TLS_KEY, true);
        props.put(MAIL_SMTP_HOST_KEY, HOST);
        props.put(MAIL_SMTP_PORT_KEY, PORT);
    }

    public static void main(String[] args) {
        TestDrive td = new TestDrive();
        td.init();
//        td.sendEmailViaSpring("Test");
        td.sendEmailViaPlainMail("Test");
    }

    void init() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(CONTEXT_FILE_NAME);
        sender = ctx.getBean("mailSender", MailSender.class);
        template = ctx.getBean("template", SimpleMailMessage.class);
    }

    void sendEmailViaSpring(String text) {
        SimpleMailMessage message = new SimpleMailMessage(template);
        message.setTo("oldlamer@mail333.com");
//        message.setFrom("pmpozitron@gmail.com");
        message.setText(text);
        sender.send(message);
    }

    void sendEmailViaPlainMail(String text) {
        Authenticator authenticator = new Authenticator() {
            
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USER, PASS);                
            }            
        };
        
        Session session = Session.getInstance(props, authenticator);        
        Message message = new MimeMessage(session);
        
        try {
            message.setFrom(new InternetAddress(FROM));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
            message.setSubject("Plain JavaMail Test");            
            message.setText(DATE_FORMAT.format(new Date()) + " " + text);
            
            Transport.send(message);
            
        } catch (Exception e) {
            System.err.println(e.getClass().getSimpleName() + " " + e.getMessage());
        }       
    }
}
