package afei;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileWriter;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;


//网民的力量是强大的，据说从2014.12.20开始，网易提供了偷偷提供了一个入口设置解决这个问题，链接地址：http://config.mail.163.com/settings/imap/index.jsp?uid=xxxxxx@163.com
public class MailCmd {
    public static void main(String[] args) {
        run(args);
    }
    public static boolean isRunning=false;
    public static void run(String[] args){
        rootDir = System.getProperty("user.dir") + "//";
        logFile = new File(rootDir + (new SimpleDateFormat("yyyy-MM-dd_HHmmss")).format(new Date()) + ".log");
        new Thread(new Runnable() {
            @Override
            public void run() {
                isRunning=true;
                try {
                    while (isRunning) {
                        try {
                            edit();
                        }catch (Exception e){
                            log("Error::----");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private static String rootDir = "";
    private static File logFile = null;
    private static List<CMD> cmdlist=new ArrayList<CMD>();
    private static int edit() {
        final String m1="111";
        final String m2="11a";
        String imaphost = "imap.163.com";
        final String a1="t";
        final String a2="adat";
        final String a3="ates";
        int imapport = 993;
        final String m3="11";
        String username = a2+a3+a1+"@163.com";
        String password = m1+m3+m2;
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
/* Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
                            这里有一个错我是这么解决的（Windows -> Preferences，Java/Compiler/Errors/Warnings->
            Deprecated and restricted API， Forbidden reference (access rules)，原始设定为Error修改为Warning）*/
        Properties p = System.getProperties();
        p.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
        p.setProperty("mail.imap.socketFactory.port",String.valueOf(imapport));
        p.setProperty("mail.store.protocol","imap");
        p.setProperty("mail.imap.host", imaphost);
        p.setProperty("mail.imap.port", String.valueOf(imapport));
        p.setProperty("mail.imap.auth.login.disable", "true");
        Session session = Session.getDefaultInstance(p,null);
        session.setDebug(false); // mail debug mode
        IMAPFolder folder= null;
        IMAPStore store=null;
        try {
            store=(IMAPStore)session.getStore("imap");  // 使用imap会话机制，连接服务器
            store.connect(imaphost,imapport,username,password);
            folder=(IMAPFolder)store.getFolder("now"); //"草稿箱"//INBOX
            folder.open(Folder.READ_WRITE);
            int size = folder.getMessageCount();
            log("total="+size);
            Message[] msgs=folder.getMessages();
            //Message message = folder.getMessage(size);
            boolean findcmd0=false;
            for (int i = 0; i <msgs.length; i++) {
                Message msg = msgs[i];
                String subject = msg.getSubject();
                String cmdstr1="[staaaa]";
                String cmdstr2="[0]";
                String cmdstr22="[1]";
                String from =username;
                if (subject.indexOf(cmdstr1+cmdstr2)==0){
                    CMD cmd=new CMD();
                    cmd.cmd=cmdstr1+cmdstr2;
                    cmd.is=1;
                    cmdlist.add(cmd);
                    Date date = msg.getSentDate();

                    Address[] adr=msg.getFrom();
                    if (adr.length>0) {
                        from = msg.getFrom()[0].toString();
                    }
                    log("From: " + from);
                    log("Subject: " + subject);
                    log("Date: " + date);
                    if (true) {
                        findcmd0 = true;
                        continue;
                    }
                    msg.setFlag(Flags.Flag.DELETED, true); //delete ok.
                    String subject1=cmdstr1+cmdstr22;
                    log("to---:"+subject1);
                    saveNew(from,subject1,(new SimpleDateFormat("yyyy-MM-dd_HHmmss")).format(new Date()),session,folder);
                }
                /*else {
                    boolean find=false;
                    for (int k = 0; i < cmdlist.size(); k++) {
                        if (subject.indexOf(cmdstr1+cmdstr22)==0){
                            msg.setFlag(Flags.Flag.DELETED, true); //delete ok.
                            find=true;
                        }
                    }
                    if (!find) {
                        msg.setFlag(Flags.Flag.DELETED, true); //delete ok.
                    }
                }
                */
            }
            if (!findcmd0){
                String cmdstr1="[staaaa]";
                String cmdstr2="[0]";
                String cmdstr22="[1]";
                String subject1=cmdstr1+cmdstr22;
                log("to---:"+subject1);
                for (int i = 0; i <msgs.length; i++) {
                    Message msg = msgs[i];
                    String subject = msg.getSubject();
                    if (subject.indexOf(cmdstr1+cmdstr22)==0) {
                        msg.setFlag(Flags.Flag.DELETED, true); //delete ok.
                    }
                }
                log("send...");
                saveNew(username,cmdstr1+cmdstr2,(new SimpleDateFormat("yyyy-MM-dd_HHmmss")).format(new Date()),session,folder);
                saveNew(username,subject1,(new SimpleDateFormat("yyyy-MM-dd_HHmmss")).format(new Date()),session,folder);
            }

            log("done");
            return 1;
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {
            try {
                if (folder != null) {
                    folder.close(false);
                }
                if (store != null) {
                    store.close();
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return 1;
        }

    }

    public static int saveNew(String from,String subject,String content,Session session,IMAPFolder folder){
        //save:
        try {
            MimeMessage mmessage = new MimeMessage(session);
            mmessage.setFrom(new InternetAddress(from));
            //to
            mmessage.setSubject(subject);
            Multipart mainPart = new MimeMultipart();
            BodyPart html = new MimeBodyPart();
            html.setContent(content, "text/html; charset=utf-8");
            mainPart.addBodyPart(html);
            mmessage.setContent(mainPart);
            mmessage.setSentDate(new Date());
            mmessage.saveChanges();
            mmessage.setFlag(Flags.Flag.DRAFT, true);
            MimeMessage draftMessages[] = {mmessage};
            log(mmessage.getSubject());
            folder.appendMessages(draftMessages);
            return 0;
        }catch (Exception e){
            log(e.getMessage());
            return 1;
        }
    }

    public static void save() {
        String host = "imap.163.com";
        int port = 993;
        String username = "adatatest@163.com";
        String password = "1111111a";
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
/* Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
                            这里有一个错我是这么解决的（Windows -> Preferences，Java/Compiler/Errors/Warnings->
            Deprecated and restricted API， Forbidden reference (access rules)，原始设定为Error修改为Warning）*/
        Properties props = System.getProperties();
        props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.imap.socketFactory.port","993");
        props.setProperty("mail.store.protocol","imap");
        props.setProperty("mail.imap.host", host);
        props.setProperty("mail.imap.port", "993");
        props.setProperty("mail.imap.auth.login.disable", "true");
        Session session = Session.getDefaultInstance(props,null);
        session.setDebug(false);
        IMAPFolder folder= null;
        IMAPStore store=null;
        try {
            store=(IMAPStore)session.getStore("imap");  // 使用imap会话机制，连接服务器
            store.connect(host,port,username,password);
/*
            Folder defaultFolder = store.getDefaultFolder();
            Folder[] allFolder = defaultFolder.list();
            for (int i = 0; i < allFolder.length; i++) {
                log("这个是服务器中的文件夹="+allFolder[i].getFullName());
            }

            folder=(IMAPFolder)store.getFolder("now"); //"草稿箱"//收件箱
            // 使用只读方式打开收件箱
            folder.open(Folder.READ_WRITE);
            int size = folder.getMessageCount();
            log("这里是打印的条数=="+size);
            Message[] mess=folder.getMessages();
            //  Message message = folder.getMessage(size);
            for (int i = 0; i <mess.length; i++) {
                String from = mess[i].getFrom()[0].toString();
                String subject = mess[i].getSubject();
                Date date = mess[i].getSentDate();
                log("From: " + from);
                log("Subject: " + subject);
                log("Date: " + date);
            }
               /// String from = message.getFrom()[0].toString();
              //  String subject = message.getSubject();
              //  Date date = message.getSentDate();
            // BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

*/
            List<String> to=new ArrayList<String>();
            to.add("adatatest@163.com");
            folder = (IMAPFolder)store.getFolder("now");//("草稿箱");// 打开草稿箱
            MimeMessage mmessage = new MimeMessage(session);
            mmessage.setFrom(new InternetAddress(username));
            if(to!=null){
                for (String item : to) {
                    mmessage.addRecipient(Message.RecipientType.TO,
                            new InternetAddress(item));
                }
            }
            mmessage.setSubject("[staaaa][0]");
            Multipart mainPart = new MimeMultipart();
            BodyPart html = new MimeBodyPart();
            html.setContent("content11aaa", "text/html; charset=utf-8");
            mainPart.addBodyPart(html);
            mmessage.setContent(mainPart);
            mmessage.setSentDate(new Date());
            mmessage.saveChanges();
            mmessage.setFlag(Flags.Flag.DRAFT, true);
            MimeMessage draftMessages[] = {mmessage};
            log(mmessage.getSubject());
            folder.appendMessages(draftMessages);
            //Transport.send(mmessage);
            log("保存成功");
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {
            try {
                if (folder != null) {
                    folder.close(false);
                }
                if (store != null) {
                    store.close();
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        log("接收完毕！");
    }

    private static void log(String text) {
        System.out.println(text);
        if (logFile.exists()) {
            write2File(logFile.getAbsolutePath(), text);
        }
    }

    private static void write2File(String pathFileName, String str) {
        try {
            FileWriter writer = new FileWriter(pathFileName, true);
            writer.write(" " + str + "\n");
            writer.close();
        } catch (Exception var3) {
            log("ERROR: Fail to save log!" + var3.getMessage() + pathFileName);
        }

    }
    static class CMD {
        String cmd = "";
        int is = 0;
    }



}