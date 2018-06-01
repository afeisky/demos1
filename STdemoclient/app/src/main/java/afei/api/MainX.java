package afei.api;

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
import java.security.Security;
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

/**
 * Created by chaofei on 18-4-18.
 */

public class MainX {
    private static String TAG="MailX";
    private static Session session =null;
    private static IMAPStore store=null;
    private static String username="";
    private static String password="";
    private static boolean isConnecting=false;

    private boolean initVar() {
        //if (isConnecting==true){
        //    return isConnecting;
        //}
        if (session!=null && store!=null) {
            isConnecting=true;
            return true;
        }

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
        try {
            store=(IMAPStore)session.getStore("imap");  // 使用imap会话机制，连接服务器
            store.connect(imaphost,imapport,username,password);
            //store.connect(username,password);
            //LogX.d(TAG,"done");
            if (session!=null && store!=null) {
                isConnecting=true;
                return true;
            }
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return false;
    }
    public int saveMail(String subject,String content,List<File> files,String folderName) {
        if (initVar()) {
            //saveNew((new SimpleDateFormat("yyyy-MM-dd_HHmmss")).format(new Date()), (new SimpleDateFormat("yyyy-MM-dd_HHmmss")).format(new Date()), "now");
            //saveNew(subject, content,files,folderName);
        }
        LogX.d(TAG,"done");
        return 0;
    }
    public ArrayList<String> cmdCheck(MimeMessage msg, String pathDir) {
        boolean isContainerAttachment = isContainAttachment(msg);
        if (isContainerAttachment) {
            LogX.d(TAG,"cmdCheck：has attachment.");
            return saveAttachment(msg, pathDir);
        }else{
            //LogX.d(TAG,"cmdCheck：not found attachment!");
        }
        return null;
    }

    public ArrayList<String> cmdRead(String key,String pathDir) {
        ArrayList<String> filens = new ArrayList<String>();
        if (!initVar()) {
            LogX.e(TAG, "imap connect fail!");
            return null;
        }
        LogX.w(TAG,"cmdRead()" );
        key = "";
        IMAPFolder folder=null;
        try {
            folder = (IMAPFolder) store.getFolder(Global.getNowBKDir());
            folder.open(Folder.READ_WRITE);
            int total = folder.getMessageCount();
            LogX.w(TAG,"---Total：" + total + "");
            //List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            LogX.d(TAG,"not read count：" + folder.getUnreadMessageCount());
            Message[] messages = folder.getMessages();
            if (messages.length > 0) {
                Message msgmax=null;
                String subjectmax="";
                for (int i = 0; i < messages.length; i++) {
                    System.out.println("======================");
                    Message msg = messages[i];
                    String subject = msg.getSubject();
                    LogX.d(TAG,"subject：" + subject);
                    String time=subject;
                    if (subject.indexOf(key)==0) {
                        if (msgmax == null) {
                            msgmax = msg;
                            subjectmax = subject;
                            LogX.d(TAG, " no:1");
                            break;//////
                        } else if (subject.compareTo(subjectmax) > 0) {
                            //if (subject.indexOf("2018-04-19_113010")<0){
                            LogX.d(TAG, " delete ?" + (i - 1));
                            //msgmax.setFlag(Flags.Flag.DELETED, true);
                            //}
                            msgmax = msg;
                            subjectmax = subject;
                        } else {
                            LogX.d(TAG, " delete " + i);
                            //msg.setFlag(Flags.Flag.DELETED, true);
                        }
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
                ////msgmax.setFlag(Flags.Flag.DELETED, true);
                LogX.d(TAG,"999");
            }
        } catch (javax.mail.NoSuchProviderException npe) {
            LogX.e(TAG,npe.getStackTrace()+"");
        } catch (MessagingException me) {
            LogX.e(TAG,me.getStackTrace()+"");
        }finally {
            try {
                if (folder != null) {
                    folder.close(false);
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
            //LogX.d(TAG,"Marked DELETE for message: " + subject);
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
        LogX.w(TAG,destDir +"/"+ fileName);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(new File(destDir +"/"+ fileName)));
        int len = -1;
        while ((len = bis.read()) != -1) {
            bos.write(len);
            bos.flush();
        }
        bos.close();
        bis.close();
        LogX.w(TAG,destDir +"/"+ fileName+"  done!");
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
