package demo.api;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;


//网民的力量是强大的，据说从2014.12.20开始，网易提供了偷偷提供了一个入口设置解决这个问题，链接地址：http://config.mail.163.com/settings/imap/index.jsp?uid=xxxxxx@163.com
public class MailCmd {
    private static String TAG="MailCmd";
    public static boolean isRunning=false;
    private static List<CMD> cmdlist=new ArrayList<CMD>();
    private static Session session =null;
    private static IMAPStore store=null;
    private static String username="";
    private static String password="";
    private static boolean isConnecting=false;

    public int saveNew1(String subject,String content,List<File> files,String folderName){
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
            e.printStackTrace();
            log("saveNew error!");
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

    private boolean initVar() {
        if (session!=null && store!=null) {
            isConnecting=true;
            return true;
        }
        isConnecting=false;
        final String m1="111";
        final String m2="11a";
        String imaphost = "imap.163.com";
        final String a1="t";
        final String a2="adat";
        final String a3="ates";
        int imapport = 993;
        final String m3="11";
        username = a2+a3+a1+"@163.com";
        password = m1+m3+m2;
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
//      Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
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
        try {
            store=(IMAPStore)session.getStore("imap");  // 使用imap会话机制，连接服务器
            store.connect(imaphost,imapport,username,password);
            //store.connect(username,password);

            if (session==null) {
                log("session==null");
            }
            if (store==null) {
                log("store==null");
            }
            log("initVar done");
            if (session!=null && store!=null) {
                isConnecting=true;
            }
        } catch (NoSuchProviderException e) {
            log("NoSuchProviderException e : "+e.getLocalizedMessage());
            e.printStackTrace();
        } catch (MessagingException e) {
            log("MessagingException e : "+e.getLocalizedMessage());
            e.printStackTrace();
        }finally {
            //log("***NOTICE: this func must be user in new Thread, otherwise will displsy 'Using Network Security Config from resource network_security_config enableDebug: true'");
            // Task1 t = new Task1();  class Task1 extends Thread { Run() {  init Mail connect ..., and Send mail...}
            // t.start();
            log("isConnecting : "+isConnecting);
            return isConnecting;
        }
    }


    public MimeMessage newMessage(String subject,String content,List<File> files){
        MimeMessage mmessage = new MimeMessage(session);
        try {
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
            return mmessage;
        }catch (Exception e){
            e.printStackTrace();
            log("saveNew error!");
            return mmessage;
        }
    }

    public int cmdReponse(String subject,String content,List<File> files,String folderName,String cmdKey) {
        if (!initVar()) {
            return 1;
        }
//save:
        IMAPFolder folder=null;
        try {
            folder = (IMAPFolder) store.getFolder(folderName);
            folder.open(Folder.READ_WRITE);
            int total = folder.getMessageCount();
            LogX.w(TAG,"---Total：" + total + " ,not read count"+folder.getUnreadMessageCount());
            //List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            Message[] messages = folder.getMessages();
            if (messages.length > 0) {
                String subject1=null;
                for (int i = 0; i < messages.length; i++) {
                    Message msg = messages[i];
                    subject1 = msg.getSubject();
                    if (subject1.indexOf(cmdKey)==0){
                        LogX.d(TAG," delete "+i);
                        if (subject1.compareToIgnoreCase(subject)>0) {
                            LogX.w(TAG," compareToIgnoreCase()>0 !!!");
                            return 2;
                        }else{
                            msg.setFlag(Flags.Flag.DELETED, true);
                        }
                    }
                }
            }
            MimeMessage mmessage =newMessage(subject,content,files);
            MimeMessage draftMessages[] = {mmessage};
            log(mmessage.getSubject());
            folder.appendMessages(draftMessages);
            return 0;
        }catch (Exception e){
            e.printStackTrace();
            log("saveNew error!");
            return 1;
        } finally {
            try {
                if (folder != null) {
                    folder.close(false);
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }

        }
    }



    public int getList(String folderName) {
        if (!initVar()) {
            log("error 1111");
            return 1;
        }
        log("2222!");
        IMAPFolder folder=null;
        try {
            folder = (IMAPFolder) store.getFolder(folderName);
            folder.open(Folder.READ_WRITE);
            int total = folder.getMessageCount();
            Message[] messages = folder.getMessages();
            if (messages.length > 0) {
                String subject1=null;
                for (int i = 0; i < messages.length; i++) {
                    Message msg = messages[i];
                    subject1 = msg.getSubject();
                        LogX.d(TAG," "+i+" : "+subject1);

                }
            }
            return 0;
        }catch (Exception e){
            e.printStackTrace();
            log("saveNew error!");
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
    public int cmdSave11(String from,String subject,String content,List<File> files,String folderName){
        //save:
        if (!initVar()) {
            return 1;
        }
        if (from==null){
            from=username;
        }
        log("cmdSave() 1, "+TimeX.getToday());
        IMAPFolder folder=null;
        try {
            folder = (IMAPFolder) store.getFolder(folderName);
            folder.open(Folder.READ_WRITE);

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
            log("cmdSave() 3 "+TimeX.getToday() +", "+mmessage.getSubject());
            folder.appendMessages(draftMessages);
            log("cmdSave() 4 "+TimeX.getToday());
            return 0;
        }catch (Exception e){
            log("Error:"+e.getMessage());
            e.printStackTrace();
            return 1;
        }finally {
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
    public int cmdSave(String from,String subject,String content,List<File> files,String folderName){
        //save:
        if (!initVar()) {
            return 1;
        }
        if (from==null){
            from=username;
        }
        log("cmdSave2() 1, "+TimeX.getToday());
        IMAPFolder folder=null;
        try {
            folder = (IMAPFolder) store.getFolder(folderName);
            folder.open(Folder.READ_WRITE);

            MimeMessage mmessage =newMessage(subject,content,files);
            MimeMessage draftMessages[] = {mmessage};
            log(mmessage.getSubject());
            log("cmdSave2() 2 "+TimeX.getToday() +", "+mmessage.getSubject());
            folder.appendMessages(draftMessages);
            log("cmdSave2() 3 "+TimeX.getToday());
            return 0;
        }catch (Exception e){
            log("Error:"+e.getMessage());
            e.printStackTrace();
            return 1;
        }finally {
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
    public int delteAllNotToday(String FolderName,String today) {
        if (!initVar()) {
            return 1;
        }
        IMAPFolder folder= null;
        try {
            store=(IMAPStore)session.getStore("imap");  // 使用imap会话机制，连接服务器
            store.connect(username,password);
            folder=(IMAPFolder)store.getFolder(FolderName); //"草稿箱"//INBOX
            folder.open(Folder.READ_WRITE);
            //int size = folder.getMessageCount();
            Message[] msgs=folder.getMessages();
            //Message message = folder.getMessage(size);
            log("delete "+FolderName+" count:"+msgs.length);
            for (int i = 0; i <msgs.length; i++) {
                Message msg = msgs[i];
                String subject = msg.getSubject();
                if (subject.indexOf(today)<0) {
                    msgs[i].setFlag(Flags.Flag.DELETED, true); //delete ok.
                }
            }
            log("delete done!");
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
    public int delteAll(String FolderName) {
        if (!initVar()) {
            return 1;
        }
        IMAPFolder folder= null;
        try {
            store=(IMAPStore)session.getStore("imap");  // 使用imap会话机制，连接服务器
            store.connect(username,password);
            folder=(IMAPFolder)store.getFolder(FolderName); //"草稿箱"//INBOX
            folder.open(Folder.READ_WRITE);
            //int size = folder.getMessageCount();
            Message[] msgs=folder.getMessages();
            //Message message = folder.getMessage(size);
            log("delete "+FolderName+" count:"+msgs.length);
            for (int i = 0; i <msgs.length; i++) {
                msgs[i].setFlag(Flags.Flag.DELETED, true); //delete ok.
            }
            log("delete done!");
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

    private static void log(String text) {
        LogX.w("MainCmd",text);
        //if (logFile.exists()) {
        //    write2File(logFile.getAbsolutePath(), text);
        //}
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

    public ArrayList<String> cmdCheck(MimeMessage msg,String pathDir) {
        boolean isContainerAttachment = isContainAttachment(msg);
        if (isContainerAttachment) {
            LogX.d(TAG,"cmdCheck：has attachment.");
            return saveAttachment(msg, pathDir);
        }else{
            LogX.d(TAG,"cmdCheck：not found attachment!");
        }
        return null;
    }

    public ArrayList<String> cmdRead(String key,String pathDir) {
        ArrayList<String> filens = new ArrayList<String>();
        if (!initVar()) {
            LogX.e(TAG, "imap connect fail!");
            return null;
        }
        key = "";
        IMAPFolder folder=null;
        try {
            folder = (IMAPFolder) store.getFolder("now");
            folder.open(Folder.READ_WRITE);
            int total = folder.getMessageCount();
            LogX.d(TAG,"---Total：" + total + "");
            //List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            LogX.d(TAG,"not read count：" + folder.getUnreadMessageCount());
            Message[] messages = folder.getMessages();
            if (messages.length > 0) {
                LogX.d(TAG,"Messages's length: " + messages.length);
                Message msgmax=null;
                String subjectmax="";
                for (int i = 0; i < messages.length; i++) {
                    System.out.println("======================");
                    Message msg = messages[i];
                    String subject = msg.getSubject();
                    LogX.d(TAG,"subject：" + subject);
                    String time=subject;
                    if (msgmax==null){
                        msgmax=msg;
                        subjectmax=subject;
                    }else if (subject.compareTo(subjectmax)>0) {
                        //if (subject.indexOf("2018-04-19_113010")<0){
                        LogX.d(TAG," delete");
                            msgmax.setFlag(Flags.Flag.DELETED, true);
                        //}
                        msgmax=msg;
                        subjectmax=subject;
                    }else{
                        msg.setFlag(Flags.Flag.DELETED, true);
                    }
                    if (filens != null) {
                        //msg.setFlag(Flags.Flag.DELETED, true);
                    }
                }

                MimeMessage msg = (MimeMessage) msgmax;
                LogX.d(TAG,pathDir);
                filens = cmdCheck(msg, pathDir);
                if (filens!=null) {
                    for (String fn : filens) {
                        LogX.w(TAG, fn);
                    }
                }else{
                    LogX.d(TAG,"NULL");
                }
                //msgmax.setFlag(Flags.Flag.DELETED, true);
                LogX.d(TAG,"999");
            }
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }finally {
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
            return filens;
        }

    }


    /**
     * 解析邮件
     *
     * @param messages 要解析的邮件列表
     */
    public void deleteMessage(Message... messages) throws MessagingException, IOException {
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
    public String getSubject(MimeMessage msg) throws UnsupportedEncodingException, MessagingException {
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
    public String getFrom(MimeMessage msg) throws MessagingException, UnsupportedEncodingException {
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
    public String getReceiveAddress(MimeMessage msg, Message.RecipientType type) throws MessagingException {
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
    public String getSentDate(MimeMessage msg, String pattern) throws MessagingException {
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
    public boolean isContainAttachment(Part part)  {
        boolean flag = false;
        try {
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
        }catch ( MessagingException me){
            me.printStackTrace();
        }catch (IOException ioe){
            ioe.printStackTrace();
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
    public boolean isSeen(MimeMessage msg) throws MessagingException {
        return msg.getFlags().contains(Flags.Flag.SEEN);
    }

    /**
     * 判断邮件是否需要阅读回执
     *
     * @param msg 邮件内容
     * @return 需要回执返回true, 否则返回false
     * @throws MessagingException
     */
    public boolean isReplySign(MimeMessage msg) throws MessagingException {
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
    public String getPriority(MimeMessage msg) throws MessagingException {
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
    public void getMailTextContent(Part part, StringBuffer content) throws MessagingException, IOException {
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
    public ArrayList<String> saveAttachment(Part part, String destDir)  {
        ArrayList<String> filens=new ArrayList<String>();
        try {
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
                        filens.add(decodeText(bodyPart.getFileName()));
                        saveFile(is, destDir, decodeText(bodyPart.getFileName()));
                    } else if (bodyPart.isMimeType("multipart/*")) {
                        return saveAttachment(bodyPart, destDir);
                    } else {
                        String contentType = bodyPart.getContentType();
                        if (contentType.indexOf("name") != -1 || contentType.indexOf("application") != -1) {
                            filens.add(decodeText(bodyPart.getFileName()));
                            saveFile(bodyPart.getInputStream(), destDir, decodeText(bodyPart.getFileName()));
                        }
                    }
                }
            } else if (part.isMimeType("message/rfc822")) {
                return saveAttachment((Part) part.getContent(), destDir);
            }
            return filens;
        }catch (UnsupportedEncodingException uee){
            uee.printStackTrace();
        }catch (MessagingException me){
            me.printStackTrace();
        }catch (FileNotFoundException fnfe){
            fnfe.printStackTrace();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
        return null;
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
    private void saveFile(InputStream is, String destDir, String fileName)
            throws FileNotFoundException, IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(new File(destDir +"/"+ fileName)));
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
    public String decodeText(String encodeText) throws UnsupportedEncodingException {
        if (encodeText == null || "".equals(encodeText)) {
            return "";
        } else {
            return MimeUtility.decodeText(encodeText);
        }
    }
}