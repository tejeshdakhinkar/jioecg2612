package com.rjil.jio.nasan.client;

import com.rjil.jio.nasan.forgotpwd.ForgotPassword;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class File {

	public static void main(String[] args) throws Exception {
		/*java.io.File f=new java.io.File("D:/upload/4ed11f73-e997-4556-8173-7e0743cbcb7f.jpeg");
		if(f.exists())
			System.out.println("Present");
		else
			System.out.println("Not Present");*/
		
		/*ForgotPassword fp=new ForgotPassword();
		fp.sendEmail("tejesh.dakhinkar@gmail.com", "jioadmin", "111111");*/
		
		File f=new File();
		f.send();
		
		
	}
		
		   public void send()
		   {    
		      // Recipient's email ID needs to be mentioned.
		      String to = "tejesh.dakhinkar@gmail.com";

		      // Sender's email ID needs to be mentioned
		      String from = "reliancejiohealth@gmail.com";

		      // Assuming you are sending email from localhost
		      String host = "smtp.gmail.com";

		      // Get system properties
		      Properties properties = System.getProperties();

		      // Setup mail server
		      properties.setProperty("mail.smtp.host", host);

		      // Get the default Session object.
		      Session session = Session.getDefaultInstance(properties);

		      try{
		         // Create a default MimeMessage object.
		         MimeMessage message = new MimeMessage(session);

		         // Set From: header field of the header.
		         message.setFrom(new InternetAddress(from));

		         // Set To: header field of the header.
		         message.addRecipient(Message.RecipientType.TO,
		                                  new InternetAddress(to));

		         // Set Subject: header field
		         message.setSubject("This is the Subject Line!");

		         // Now set the actual message
		         message.setText("This is actual message");

		         // Send message
		         Transport.send(message);
		         System.out.println("Sent message successfully....");
		      }catch (MessagingException mex) {
		         mex.printStackTrace();
		      }
		   }
		

	}


