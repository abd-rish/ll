package com.llp.util;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.llp.dao.UtilDAO;
import com.llp.pojos.EmailTemplates;

@Service
public class MailSender {
	
	@Autowired
	private UtilDAO utilDAO;
	
	public void sendEmail(String to, String subject, String content) {
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				Properties properties = System.getProperties();
				properties.put("mail.smtp.host", ApplicationProperties.Mail.HOST);
				properties.put("mail.smtp.port", ApplicationProperties.Mail.PORT);
				properties.put("mail.smtp.auth", ApplicationProperties.Mail.AUTH);
				properties.put("mail.smtp.starttls.enable", ApplicationProperties.Mail.STARTTLS_ENABLE);
				properties.put("mail.smtp.starttls.required", ApplicationProperties.Mail.STARTTLS_REQUIRED);
				
				Session session = Session.getDefaultInstance(properties, null);
				
				try {
					MimeMessage message = new MimeMessage(session);
					message.setFrom(new InternetAddress(ApplicationProperties.Mail.FROM));
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
					message.setSubject(subject);
					message.setContent(content, "text/html");
					message.setSentDate(new Date());
					
					Transport transport = session.getTransport("smtp");
					transport.connect(ApplicationProperties.Mail.HOST, ApplicationProperties.Mail.FROM, ApplicationProperties.Mail.PASSWORD);
					transport.sendMessage(message, message.getAllRecipients());
					transport.close();
				} catch (MessagingException mex) {
					mex.printStackTrace();
				}
			}
		};
		
		new Thread(runnable).start();
	}
	
	public void sendEmail(String to, String templateCode, Map<String, Object> params) {
		if(to == null || templateCode == null)
			return;
		EmailTemplates emailTemplate = utilDAO.getEmailTemplate(templateCode);
		if(emailTemplate == null)
			return;
		sendEmail(to, Helper.replaceVariables(emailTemplate.getSubject(), params), Helper.replaceVariables(emailTemplate.getTemplate(), params));
	}
	
}