package com.rjil.jio.nasan.forgotpwd;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgotPassword {

	private static final String SMTP_AUTH_USER = "reliancejiohealth@gmail.com";
	private static final String SMTP_AUTH_PWD  = "123456@jio";
	private static final String SMTP_HOST_NAME = "smtp.gmail.com";
	private static final int SMTP_HOST_PORT = 465;
	
	 public static void sendEmail(String email,String username,String password) throws Exception{
         Properties props = new Properties();
         props.put("mail.transport.protocol", "smtps");
         props.put("mail.smtps.host", SMTP_HOST_NAME);
         props.put("mail.smtps.auth", "true");
         // props.put("mail.smtps.quitwait", "false");
  
         Session mailSession = Session.getDefaultInstance(props);
         mailSession.setDebug(true);
         Transport transport = mailSession.getTransport();
         //String password=LoginUtils.getPassword(username);
         MimeMessage message = new MimeMessage(mailSession);
         // message subject
         message.setSubject("Automated Mail - From Jio Health Cloud - User Details");
         // message body
         message.setContent("Dear Customer, please find user details. Your user name is "+username+" password is "+password+".", "text/plain");
         message.addRecipient(Message.RecipientType.TO,
              new InternetAddress(email));
  
         transport.connect
           (SMTP_HOST_NAME, SMTP_HOST_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);
  
         transport.sendMessage(message,
             message.getRecipients(Message.RecipientType.TO));
         transport.close();
     }
	
	
}

