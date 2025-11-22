package com.receipt.emailutility;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;    
import javax.mail.internet.*;
import javax.servlet.ServletContext;
import javax.ws.rs.core.MediaType;

import com.sun.mail.imap.IMAPStore;
import com.sun.mail.smtp.SMTPTransport; 

public class SendUtility {

	/*
	 * private final static String from= "nilaya.foundation@gmail.com"; private
	 * final static String password = "fldkgkyftqbnjjow";
	 */
	private final static String from= "nilaya.foundation@gmail.com"; private
	final static String password = "fldkgkyftqbnjjow";
	
	public static void send(String name,String to,String CC,String file, InputStream resourceAsStream) throws Exception{  
       
        Properties props = new Properties();    
        props.put("mail.smtp.host", "smtp.gmail.com");    
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");    
        props.put("mail.smtp.port", "587");    
        props.put("mail.smtp.ssl.protocols", "TLSv1.2"); 
        //get Session   
        
        
        Session session = Session.getDefaultInstance(props,    
         new javax.mail.Authenticator() { 
        	
         protected PasswordAuthentication getPasswordAuthentication() {    
         return new PasswordAuthentication(from,password);  
         }    
        });    
           
        
        
        try {  
        	
         String sub = "Donation Receipt - "+name;
         String msg = "";
         msg = getMessage(name, resourceAsStream);
         MimeMessage message = new MimeMessage(session);    
         message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));    
         message.addRecipient(Message.RecipientType.CC,new InternetAddress(CC));
         message.setSubject(sub);    
         
         BodyPart messageBodyPart = new MimeBodyPart();
         
         
         messageBodyPart.setContent(msg, MediaType.TEXT_HTML);   
        
         Multipart multipart = new MimeMultipart();

         // Set text message part
         multipart.addBodyPart(messageBodyPart);

         // Part two is attachment
         messageBodyPart = new MimeBodyPart();
        
         DataSource source = new FileDataSource(file);
         messageBodyPart.setDataHandler(new DataHandler(source));
         messageBodyPart.setFileName(name.replace(" ", "")+".pdf");
         multipart.addBodyPart(messageBodyPart);
         
         message.setContent(multipart);
       
         //send message  
         Transport transport = session.getTransport("smtp");
        // SMTPTransport transport2 = getTransport(from, "4498781067-d8pj3ot435is67nlmsqi4mrmb0bcolp3.apps.googleusercontent.com");
         
        // transport2.send(message);
         transport.send(message);  
         System.out.println("message sent successfully");    
        } catch (Exception e) {throw e;}    
           
  }
	
	 public static SMTPTransport getTransport( String email ,  String oauthToken){
		   

		    OAuth2Authenticator.initialize();
		    SMTPTransport smtpTransport = null;
		  
		    try {
				 smtpTransport = OAuth2Authenticator.connectToSmtp("smtp.gmail.com",
				                                            587,
				                                            email,
				                                            oauthToken,
				                                            true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    System.out.println("Successfully authenticated to SMTP.");
		    return smtpTransport;
	 }
	 public static String getMessage(String name, InputStream resourceAsStream) throws Exception  {
		 
		 
		 StringBuffer sbBuffer = new StringBuffer();
		 try {
			
			BufferedReader reader =  new BufferedReader(new InputStreamReader(resourceAsStream));
			String sLine = reader.readLine();
			 while(reader.ready() && sLine != null ) {
				 sLine = reader.readLine();
				 sbBuffer.append(sLine);
				 
			 }
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			throw e;
		}
		 String finalString = sbBuffer.toString();
		 finalString = finalString.replace("[Name]", name);
		 return finalString;
		 
	 }
	
}
