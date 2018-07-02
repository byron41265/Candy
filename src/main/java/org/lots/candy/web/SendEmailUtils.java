package org.lots.candy.web;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class SendEmailUtils {  
	  
    @Autowired  
    private JavaMailSender javaMailSender; 
  
    @Value("${spring.mail.username}")  
    private  String username;    
  
    private  void  sendNormalEmail(  
            String title,boolean titleWithName,  
            String content, boolean contentWithName, String email ){  
        String dName="Registration verification";  
        MimeMessage mimeMessage=null;  
        try {  
            mimeMessage= javaMailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,true);  
//            helper.setFrom(new InternetAddress(username,dName,"UTF-8")); 
            helper.setTo(email);
            title= titleWithName?title +"-"+dName:title;
            helper.setSubject(title);
            if(contentWithName) {  
                content += "<p style='text-align:right'>" +dName+ "</p>";  
  
            }  
            helper.setText(content,true);
            javaMailSender.send(mimeMessage);
        }catch (Exception e){  
            e.printStackTrace();  
        }  
             
    }  
  
  
 
    public  void  sendRegisterUrl(String email, String url){  
          final StringBuffer sb= new StringBuffer();
          sb.append("<h2>"+email+",Hello！<h2>")  
                  .append("<p style='color:red'>Click on the link to activate the user：</p>")
                  	.append("<a href="+url+">Click here</a>")	;  
          new Thread(new Runnable(){

			@Override
			public void run() {
				sendNormalEmail("Registration verification", true, sb.toString(), true, email);
			}
          }).start();;
    }  
  

}  
