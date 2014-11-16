package com.polarbear.plutus.technical;

//http://stackoverflow.com/questions/2020088/sending-email-in-android-using-javamail-api-without-using-the-default-built-in-a

import javax.activation.DataHandler;   
import javax.activation.DataSource;   
import javax.mail.Message;   
import javax.mail.PasswordAuthentication;   
import javax.mail.Session;   
import javax.mail.Transport;   
import javax.mail.internet.InternetAddress;   
import javax.mail.internet.MimeMessage;   
import java.io.ByteArrayInputStream;   
import java.io.IOException;   
import java.io.InputStream;   
import java.io.OutputStream;   
import java.security.Security;   
import java.util.Properties;   

public class GMailSender extends javax.mail.Authenticator 
{   
    private String mailHost = "smtp.gmail.com";   
    private String user;   
    private String password;   
    private Session session;   

    static
    {   
        Security.addProvider(new JSSEProvider());   
    }  

    public GMailSender(String user, String password) 
    {   
        this.user = user;   
        this.password = password;   
        Properties mailProps = new Properties();   
        mailProps.setProperty("mail.transport.protocol", "smtp");   
        mailProps.setProperty("mail.host", mailHost);   
        mailProps.put("mail.smtp.auth", "true");   
        mailProps.put("mail.smtp.port", "465");   
        mailProps.put("mail.smtp.socketFactory.port", "465");   
        mailProps.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");   
        mailProps.put("mail.smtp.socketFactory.fallback", "false");   
        mailProps.setProperty("mail.smtp.quitwait", "false");   
        session = Session.getDefaultInstance(mailProps, this);   
    }   

    protected PasswordAuthentication getPasswordAuthentication() {   
        return new PasswordAuthentication(user, password);   
    }   

    public synchronized void sendMail(String subject, String body, String sender, String recipients) throws Exception 
    {   
        try
        {
        MimeMessage msg = new MimeMessage(session);   
        DataHandler hndlr = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));   
        msg.setSender(new InternetAddress(sender));   
        msg.setSubject(subject);   
        msg.setDataHandler(hndlr);   
        if (recipients.indexOf(',') > 0)   
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));   
        else  
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));   
        Transport.send(msg);   
        }catch(Exception e){

        }
    }   

    public class ByteArrayDataSource implements DataSource {   
        private byte[] data;   
        private String type;   
        public ByteArrayDataSource(byte[] data, String type) {   
            super();   
            this.data = data;   
            this.type = type;   
        }   

        public ByteArrayDataSource(byte[] data) {   
            super();   
            this.data = data;   
        }   

        public void setType(String type) {   
            this.type = type;   
        }   

        public String getContentType() {   
            if (type == null)   
                return "application/octet-stream";   
            else  
                return type;   
        }   

        public InputStream getInputStream() throws IOException {   
            return new ByteArrayInputStream(data);   
        }   

        public String getName() {   
            return "ByteArrayDataSource";   
        }   

        public OutputStream getOutputStream() throws IOException {   
            throw new IOException("Not Supported");   
        }   
    }   
}  
