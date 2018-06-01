package afei.api;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * Created by chaofei on 18-4-18.
 */

public class MainX1 {
    private static String TAG="MailX";

    public int saveNew(String subject, String content, List<File> files, String folderName){
        //save:
        IMAPFolder folder=null;
        try {
            folder=(IMAPFolder)store.getFolder(folderName); //"草稿箱"//INBOX
            folder.open(Folder.READ_WRITE);
            MimeMessage mmessage = new MimeMessage(session);
            mmessage.setFrom(new InternetAddress(username));
            //to
            mmessage.setSubject(subject);
            Multipart mainPart = new MimeMultipart();
            BodyPart html = new MimeBodyPart();
            html.setContent(content, "text/html; charset=utf-8");
            mainPart.addBodyPart(html);
            // 添加附件
            if (files != null && files.size() > 0) {
                for (File f : files) {
                    // 创建邮件附件
                    MimeBodyPart attach = new MimeBodyPart();
                   // DataHandler dh = new DataHandler(new FileDataSource(path + list.get(i)));
                    DataHandler dh = new DataHandler(new FileDataSource(f));// 得到数据源
                    attach.setDataHandler(dh); // 得到附件本身并至入BodyPart
                    attach.setFileName(MimeUtility.encodeText(dh.getName()));// 得到文件名同样至入BodyPart
                    mainPart.addBodyPart(attach);
                }
            }
            mmessage.setContent(mainPart);
            mmessage.setSentDate(new Date());
            mmessage.saveChanges();
            mmessage.setFlag(Flags.Flag.DRAFT, true);
            MimeMessage draftMessages[] = {mmessage};
            LogX.w(TAG,mmessage.getSubject());
            folder.appendMessages(draftMessages);
            return 0;
        }catch (Exception e){
            e.printStackTrace();
            LogX.w(TAG,"saveNew error!");
            return 1;
        } finally {
            try {
                if (folder != null) {
                    folder.close(false);
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return 1;
        }
    }
    private static Session session =null;
    private static IMAPStore store=null;
    private static String username="";
    private boolean initVar() {
        final String m1="111";
        final String m2="11a";
        String imaphost = "imap.163.com";
        final String a1="t";
        final String a2="adat";
        final String a3="ates";
        int imapport = 993; //143
        final String m3="11";
        username = a2+a3+a1+"@163.com";
        String password = m1+m3+m2;
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        //Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        Properties p = System.getProperties();
        p.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
        p.setProperty("mail.imap.socketFactory.port",String.valueOf(imapport));
        p.setProperty("mail.store.protocol","imap");
        p.setProperty("mail.imap.host", imaphost);
        p.setProperty("mail.imap.port", String.valueOf(imapport));
        p.setProperty("mail.imap.auth.login.disable", "true");
        session = Session.getDefaultInstance(p,null);
        session.setDebug(false); // mail debug mode
        try {
            store=(IMAPStore)session.getStore("imap");  // 使用imap会话机制，连接服务器
            store.connect(username,password);
            if (session!=null && store!=null) {
                return true;
            }
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int mailTest(){
        final String m1="111";
        final String m2="11a";
        String imaphost = "imap.163.com";
        final String a1="t";
        final String a2="adat";
        final String a3="ates";
        int imapport = 993;// 143;
        final String m3="11";
        username = a2+a3+a1+"@163.com";
        String password = m1+m3+m2;
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        //Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        Properties p = System.getProperties();
        p.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
        p.setProperty("mail.imap.socketFactory.port",String.valueOf(imapport));
        p.setProperty("mail.store.protocol","imap");
        p.setProperty("mail.imap.host", imaphost);
        p.setProperty("mail.imap.port", String.valueOf(imapport));
        p.setProperty("mail.imap.auth.login.disable", "true");
        Session session = Session.getInstance(p,null);
        int total = 0;
        IMAPStore store=null;
        try {
            store = (IMAPStore) session.getStore("imap"); // 使用imap会话机制，连接服务器
            store.connect(username, password);
        } catch (NoSuchProviderException e) {
            //e.printStackTrace();
        } catch (MessagingException e) {
            //e.printStackTrace();
        }
        try{
            IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX"); // 收件箱
            folder.open(Folder.READ_WRITE);
            // 获取总邮件数
            total = folder.getMessageCount();
            System.out.println("---Total：" + total + "");
            // 得到收件箱文件夹信息，获取邮件列表
            //List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            System.out.println("not read count：" + folder.getUnreadMessageCount());
            Message[] messages = folder.getMessages();
            if (messages.length > 0) {
                System.out.println("Messages's length: " + messages.length);
                for (int i = 0; i < messages.length; i++) {
                    System.out.println("======================");
                    MimeMessage msg = (MimeMessage)messages[i];
                    String str= msg.getSubject();
                    System.out.println("subject：" + str);
                }

            }
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public int saveMail(String subject,String content,List<File> files,String folderName) {
        if (initVar()) {
            //saveNew((new SimpleDateFormat("yyyy-MM-dd_HHmmss")).format(new Date()), (new SimpleDateFormat("yyyy-MM-dd_HHmmss")).format(new Date()), "now");
            saveNew(subject, content,files,folderName);
        }
        LogX.w(TAG,"done");
        return 0;
    }


}
