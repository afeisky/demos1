package DailyParser;


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

import static java.lang.System.getProperties;

public class Mail1 {  //POP3ReceiveMailTest
    //https://www.cnblogs.com/sunhaoyu/p/6480117.html
    //http://blog.csdn.net/xyang81/article/details/7675160

    private static String saveMailPath = "";
    private static File fileSave = null;
    private static boolean needDeleteAfterRead = false;
    //------------------
    private static String mailServerHost;
    private static String mailServerPort;
    /**
     * 登陆邮件发送服务器的用户名和密码
     */
    private static String userName;
    private static String passWord;
    /**
     * 是否需要身份验证
     */
    private static boolean validate = false;
    private static String typeEncode="text/html;charset=UTF-8";
    //--
    //------------------
    protected Mail1()  {
        final String m1="111";
        final String m2="11a";
        final String m3="11";
        String subject="title";
        String content="content";
        final String mailserver="smtp"+".163.com";//"smtp.163.com";
        final String mailport="25";
        final String mailac="adat"+"ates"+"t@163.com";
        final String mailpa=m1+m3+m2;
        boolean validate=true;
        this.mailServerHost=mailserver;
        this.mailServerPort=mailport;
        this.userName=userName;
        this.passWord=passWord;
        this.validate=validate;

        //String type ="text/html;charset=UTF-8";
        //String mailServerPort = "25"; // 端口号
        //String mailServerHost="smtp.163.com";// send服务器地址
    }
    protected Mail1(String mailServerHost, String mailServerPort, String userName, String passWord, boolean validate){
        this.mailServerHost=mailServerHost;
        this.mailServerPort=mailServerPort;
        this.userName=userName;
        this.passWord=passWord;
        this.validate=validate;
    }
    /////////
    public static void main(String[] args) throws Exception {
        saveMailPath = System.getProperty("user.dir") + "//";
        fileSave = new File(saveMailPath + (new SimpleDateFormat("yyyy-MM-dd_HHmmss")).format(new Date()) + ".txt");
        new Thread() {public void run() {sendMailPre();}}.start();//readPop3Mail();//  readMailIMAP();
    }


    public static void sendMailPre(){
        String from="adatatest@163.com";
        List<String> to=new ArrayList<String>();
        to.add("adatatest@163.com");
        String subject="[st]";
        String content="<html>"+(new SimpleDateFormat("yyyy-MM-dd_HHmmss")).format(new Date())+"</html>";
        List<File> files=null;
        boolean draft=false;
        sendMail(from,to,subject,content,files,draft);//readPop3Mail();//  readMailIMAP();
    }
    private void seendMail(){
        log("mailServerHost:"+mailServerHost);
        new Thread() {public void run() {sendMailPre();}}.start();
    }

    private void sendJavaMail() {

        final String m1="111";
        final String m2="11a";
        final String m3="11";
        String subject="title";
        String content="content";
        final String mailserver="smtp"+".163.com";//"smtp.163.com";
        final String mailport="25";
        final String mailac="adat"+"ates"+"t@163.com";
        final String mailpa=m1+m3+m2;
        boolean validate=true;
        //
        List<String> to=new ArrayList<String>();
        to.add(mailac);
        ArrayList<File> attachment=new ArrayList<File>();
        String filename="D:\\rd\\javaApp\\demo1\\demo1.xml";
        attachment.add(new File(filename));
        if (attachment!=null){
            StringBuilder sb= new StringBuilder("");//sb.setLength(0);//sb.delete( 0, sb.length() );
            for (File f:attachment){
                sb.append(f.getName());
                if (attachment.size()>1) {
                    sb.append(",");
                }
            }
            content=sb.toString()+",h";
            subject=attachment.get(0).getName();
        }
        boolean draft=false;
        if (sendMail(mailac,to,subject,content,attachment,draft)==0){

        }

    }
    public static int sendMail(String from, List<String> to, String subject,String content, List<File> files, boolean draft) {

        log("sendMail()-->");

        // 准备连接服务器的会话信息
        Properties props = new Properties();
        props.put("mail.smtp.host",mailServerHost);//使用pop3协议
        props.put("mail.smtp.port", mailServerPort); // 端口
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", validate ? "true" : "false");
            //1、根据邮件会话属性和密码验证器构造一个发送邮件的session
            Session session  = Session.getDefaultInstance(props);
            //开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
            session.setDebug(true);
            //创建邮件
            MimeMessage message = null;
            try {
                message = new MimeMessage(session);
                // 设置邮件的基本信息
                //创建邮件发送者地址
                Address fromAddress = new InternetAddress(from);
                //设置邮件消息的发送者
                message.setFrom(fromAddress);
                //创建邮件的接受者地址，并设置到邮件消息中
                if(to!=null){
                    for (String item : to) {
                        // 检查邮箱格式是否正确
                        //if (item.matches("\\b^['_a-z0-9-\\+]"
                        //        + "+(\\.['_a-z0-9-\\+]+)*@[a-z0-9-]+"
                        //        + "(\\.[a-z0-9-]+)*\\.([a-z]{2}|aero|arpa|asia"
                        //        + "|biz|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|"
                        //        + "name|nato|net|org|pro|tel|travel|xxx)$\\b")) {
                        //    log( "javamail send fail! to mail address format error!["+item+"]?");//创建带附件的邮件失败
                        //    return 1;
                        //}
                        //设置邮件消息的接受者, Message.RecipientType.TO属性表示接收者的类型为TO
                        message.addRecipient(Message.RecipientType.TO,
                                new InternetAddress(item));
                    }
                }
                // 设置抄送人
                //if(cc!=null){
                //    for (String item : cc) {
                //        message.addRecipient(Message.RecipientType.CC,
                //                new InternetAddress(item));
                //    }
                //}
                // 设置密送人
                //if(bcc!=null){
                //    for (String item : bcc) {
                //        message.addRecipient(Message.RecipientType.BCC,
                //                new InternetAddress(item));
                //    }
                //}
                //邮件标题
                message.setSubject(subject);

                // 创建邮件正文，为了避免邮件正文中文乱码问题，需要使用CharSet=UTF-8指明字符编码
                MimeBodyPart text = new MimeBodyPart();
                text.setContent(content, typeEncode);

                // 创建容器描述数据关系
                MimeMultipart mp = new MimeMultipart();
                mp.addBodyPart(text);

                // 添加附件
                if (files != null && files.size() > 0) {
                    log( "JavaMail files:");
                    for (File f : files) {
                        // 创建邮件附件
                        MimeBodyPart attach = new MimeBodyPart();
                        //DataHandler dh = new DataHandler(new FileDataSource(path + list.get(i)));
                        DataHandler dh = new DataHandler(new FileDataSource(f));// 得到数据源
                        attach.setDataHandler(dh); // 得到附件本身并至入BodyPart
                        attach.setFileName(MimeUtility.encodeText(dh.getName()));// 得到文件名同样至入BodyPart
                        mp.addBodyPart(attach);
                    }
                }
                mp.setSubType("mixed");
                message.setContent(mp);
                message.saveChanges();
                // 将创建的Email写入到E盘存储
                //message.writeTo(new FileOutputStream("C:\\attachMail.eml"));
                if(!draft){
                    log( "JavaMail ->");
                    //2、通过session得到transport对象,以便连接邮箱并发送
                    Transport transport = session.getTransport();
                    //3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给SMTP服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
                    transport.connect(mailServerHost ,"adatatest@163.com", "1111111a");
                    //5、发送邮件消息
                    transport.sendMessage(message, message.getAllRecipients());
                    transport.close();
                    log( "JavaMail send success!");
                }else{
                    log( "JavaMail save success!");
                }

            } catch (AddressException e) {
                log( "JavaMail send failure!"+e.getMessage());//创建带附件的邮件失败
                return 10;
            } catch (MessagingException e) {
                log( "JavaMail send failure!"+e.getMessage());//创建带附件的邮件失败
                return 20;
            } catch (Exception e) {
                log( "JavaMail send failure!"+e.getMessage());//创建带附件的邮件失败
                return 30;
            }
            return 0;

    }

    public static void readMailIMAP() {
        try {
            log("readMailIMAP()-->");
            String duankou = "110"; // 端口号
            String servicePath = "pop3.163.com";   // 服务器地址
            Properties prop = getProperties();
            prop.setProperty("mail.store.protocol", "pop3");       // 使用pop3协议
            prop.setProperty("mail.pop3.port", duankou);           // 端口
            prop.setProperty("mail.pop3.host", servicePath);       // pop3服务器

            Session session = Session.getInstance(prop);
            IMAPStore store = (IMAPStore) session.getStore("imap"); // 使用imap会话机制，连接服务器
            store.connect("adatatest@163.com", "1111111a");
            IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX"); // 收件箱
            folder.open(Folder.READ_WRITE);
            //获取未读邮件
            Message[] messages = folder.getMessages(folder.getMessageCount() - folder.getUnreadMessageCount() + 1, folder.getMessageCount());
            parseMessage(messages); //解析邮件
            //释放资源
            if (folder != null) folder.close(true);
            if (store != null) store.close();
            System.out.println("读取成功。。。。。。。。。。。。");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
    /**
     * 接收邮件
     */
    public static void readOutbox() throws Exception {
        try {
            log("readOutbox()-->");
            /**
             * 因为现在使用的是163邮箱 而163的 pop地址是　pop3.163.com　 端口是　110　　
             * 比如使用好未来企业邮箱 就需要换成 好未来邮箱的 pop服务器地址 pop.263.net  和   端口 110
             */
            String duankou = "110"; // 端口号
            String servicePath = "pop3.163.com";   // 服务器地址
            
            // 准备连接服务器的会话信息
            Properties props = new Properties();
            props.setProperty("mail.store.protocol", "pop3");       // 使用pop3协议
            props.setProperty("mail.pop3.port", duankou);           // 端口
            props.setProperty("mail.pop3.host", servicePath);       // pop3服务器

            // 创建Session实例对象
            Session session = Session.getInstance(props);
            Store store = session.getStore("pop3");
            store.connect("adatatest@163.com", "1111111a"); //163邮箱程序登录属于第三方登录所以这里的密码是163给的授权密码而并非普通的登录密码

            // 获得收件箱
            Folder folder = store.getFolder("Drafts");
            /* Folder.READ_ONLY：只读权限
             * Folder.READ_WRITE：可读可写（可以修改邮件的状态）
             */
            folder.open(Folder.READ_WRITE); //打开收件箱

            // 由于POP3协议无法获知邮件的状态,所以getUnreadMessageCount得到的是收件箱的邮件总数
            log("Drafts未读邮件数: " + folder.getUnreadMessageCount());

            // 由于POP3协议无法获知邮件的状态,所以下面得到的结果始终都是为0
            log("删除邮件数: " + folder.getDeletedMessageCount());
            log("新邮件: " + folder.getNewMessageCount());

            // 获得收件箱中的邮件总数
            log("邮件总数: " + folder.getMessageCount());

            // 得到收件箱中的所有邮件,并解析
            Message[] messages = folder.getMessages();
            parseMessage1(messages);


            //释放资源
            folder.close(true);
            store.close();
        } catch (Exception e) {
            // TODO: handle exception
            log("Error:" + e.getMessage());
        }
    }

    /**
     * 解析邮件
     *
     * @param messages 要解析的邮件列表
     */
    public static void parseMessage1(Message... messages) throws MessagingException, IOException {
        if (messages == null || messages.length < 1)
            throw new MessagingException("未找到要解析的邮件!");

        // 解析所有邮件
        for (int i = 0, count = messages.length; i < count; i++) {
            MimeMessage msg = (MimeMessage) messages[i];
            log("------------------解析第" + msg.getMessageNumber() + "封邮件-------------------- ");
            log("主题: " + getSubject(msg));
            log("发件人: " + getFrom(msg));
            log("收件人：" + getReceiveAddress(msg, null));
            log("发送时间：" + getSentDate(msg, null));
            log("是否已读：" + isSeen(msg));
            log("邮件优先级：" + getPriority(msg));
            log("是否需要回执：" + isReplySign(msg));
            log("邮件大小：" + msg.getSize() * 1024 + "kb");
            boolean isContainerAttachment = isContainAttachment(msg);
            log("是否包含附件：" + isContainerAttachment);
            if (isContainerAttachment) {
                //saveAttachment(msg, saveMailPath + "\\" + msg.getSubject() + "_" + i + "_"); //保存附件
            }
            StringBuffer content = new StringBuffer(30);
            getMailTextContent(msg, content);
            log("邮件正文：" + (content.length() > 100 ? content.substring(0, 100) + "..." : content));
            log("------------------第" + msg.getMessageNumber() + "封邮件解析结束-------------------- ");
            log("");

        }
    }
    /**
     * 接收邮件
     */
    public static void readPop3Mail() throws Exception {
        try {
            log("readPop3Mail()-->");
            /**
             * 因为现在使用的是163邮箱 而163的 pop地址是　pop3.163.com　 端口是　110　　
             * 比如使用好未来企业邮箱 就需要换成 好未来邮箱的 pop服务器地址 pop.263.net  和   端口 110
             */
            String duankou = "110"; // 端口号
            String servicePath = "pop3.163.com";   // 服务器地址


            // 准备连接服务器的会话信息
            Properties props = new Properties();
            props.setProperty("mail.store.protocol", "pop3");       // 使用pop3协议
            props.setProperty("mail.pop3.port", duankou);           // 端口
            props.setProperty("mail.pop3.host", servicePath);       // pop3服务器

            // 创建Session实例对象
            Session session = Session.getInstance(props);
            Store store = session.getStore("pop3");
            store.connect("adatatest@163.com", "1111111a"); //163邮箱程序登录属于第三方登录所以这里的密码是163给的授权密码而并非普通的登录密码


            // 获得收件箱
            Folder folder = store.getFolder("INBOX");
            /* Folder.READ_ONLY：只读权限
             * Folder.READ_WRITE：可读可写（可以修改邮件的状态）
             */
            folder.open(Folder.READ_WRITE); //打开收件箱

            // 由于POP3协议无法获知邮件的状态,所以getUnreadMessageCount得到的是收件箱的邮件总数
            log("未读邮件数: " + folder.getUnreadMessageCount());

            // 由于POP3协议无法获知邮件的状态,所以下面得到的结果始终都是为0
            log("删除邮件数: " + folder.getDeletedMessageCount());
            log("新邮件: " + folder.getNewMessageCount());

            // 获得收件箱中的邮件总数
            log("邮件总数: " + folder.getMessageCount());

            // 得到收件箱中的所有邮件,并解析
            Message[] messages = folder.getMessages();
            parseMessage(messages);

            //得到收件箱中的所有邮件并且删除邮件
            if (needDeleteAfterRead) {
                deleteMessage(messages);
            }

            //释放资源
            folder.close(true);
            store.close();
        } catch (Exception e) {
            // TODO: handle exception
            log("Error:" + e.getMessage());
        }
    }

    /**
     * 解析邮件
     *
     * @param messages 要解析的邮件列表
     */
    public static void parseMessage(Message... messages) throws MessagingException, IOException {
        if (messages == null || messages.length < 1)
            throw new MessagingException("未找到要解析的邮件!");

        // 解析所有邮件
        for (int i = 0, count = messages.length; i < count; i++) {
            MimeMessage msg = (MimeMessage) messages[i];
            log("------------------解析第" + msg.getMessageNumber() + "封邮件-------------------- ");
            log("主题: " + getSubject(msg));
            log("发件人: " + getFrom(msg));
            log("收件人：" + getReceiveAddress(msg, null));
            log("发送时间：" + getSentDate(msg, null));
            log("是否已读：" + isSeen(msg));
            log("邮件优先级：" + getPriority(msg));
            log("是否需要回执：" + isReplySign(msg));
            log("邮件大小：" + msg.getSize() * 1024 + "kb");
            boolean isContainerAttachment = isContainAttachment(msg);
            log("是否包含附件：" + isContainerAttachment);
            if (isContainerAttachment) {
                saveAttachment(msg, saveMailPath + "\\" + msg.getSubject() + "_" + i + "_"); //保存附件
            }
            StringBuffer content = new StringBuffer(30);
            getMailTextContent(msg, content);
            log("邮件正文：" + (content.length() > 100 ? content.substring(0, 100) + "..." : content));
            log("------------------第" + msg.getMessageNumber() + "封邮件解析结束-------------------- ");
            log("");

        }
    }


    /**
     * 解析邮件
     *
     * @param messages 要解析的邮件列表
     */
    public static void deleteMessage(Message... messages) throws MessagingException, IOException {
        if (messages == null || messages.length < 1)
            throw new MessagingException("未找到要解析的邮件!");

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
            throw new MessagingException("没有发件人!");

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
            throw new MessagingException("没有收件人!");
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
            pattern = "yyyy年MM月dd日 E HH:mm ";

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
        String priority = "普通";
        String[] headers = msg.getHeader("X-Priority");
        if (headers != null) {
            String headerPriority = headers[0];
            if (headerPriority.indexOf("1") != -1 || headerPriority.indexOf("High") != -1)
                priority = "紧急";
            else if (headerPriority.indexOf("5") != -1 || headerPriority.indexOf("Low") != -1)
                priority = "低";
            else
                priority = "普通";
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
        System.out.println(text);
        if (fileSave.exists()) {
            write2File(fileSave.getAbsolutePath(), text);
        }
    }

    private static void write2File(String pathFileName, String str) {
        try {
            FileWriter writer = new FileWriter(pathFileName, true);
            writer.write(" " + str + "\n");
            writer.close();
        } catch (Exception e) {
            System.out.println("ERROR: Fail to save log!" + e.getMessage() + pathFileName);
        }
    }
}