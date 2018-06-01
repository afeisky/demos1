package afei.api;

/**
 * Created by chaofei on 18-1-5.
 */

import android.util.Log;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * 以下是利用JavaMail的API来创建和发送邮件
 * 多附件发送邮件类,并且发送邮件给多个接受者、抄送文件
 * @author chaofei.wu
 *
 */
public class MailUtils {
    private String TAG="JavaMailUtils";
    /**
     * 发送邮件的服务器的IP和端口
     */
    private String mailServerHost;
    private String mailServerPort;
    /**
     * 登陆邮件发送服务器的用户名和密码
     */
    private String userName;
    private String passWord;
    /**
     * 是否需要身份验证
     */
    private boolean validate = false;

    private String encode="text/html;charset=UTF-8";

    public MailUtils(String mailServerHost, String mailServerPort, String userName, String passWord, boolean validate){
        this.mailServerHost=mailServerHost;
        this.mailServerPort=mailServerPort;
        this.userName=userName;
        this.passWord=passWord;
        this.validate=validate;
    }

    public Properties getProperties(){
        Properties p = new Properties();
        p.put("mail.smtp.host", this.mailServerHost);
        p.put("mail.smtp.port", this.mailServerPort);
        p.put("mail.transport.protocol", "smtp");
        p.put("mail.smtp.auth", validate ? "true" : "false");
        return p;
    }

    public String getMailServerHost() {
        return mailServerHost;
    }
    public void setMailServerHost(String mailServerHost) {
        this.mailServerHost = mailServerHost;
    }
    public String getMailServerPost() {
        return mailServerPort;
    }
    public void setMailServerPost(String mailServerPost) {
        this.mailServerPort = mailServerPost;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassWord() {
        return passWord;
    }
    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
    public boolean isValidate() {
        return validate;
    }
    public void setValidate(boolean validate) {
        this.validate = validate;
    }
    public String getEncode() {
        return encode;
    }
    public void setEncode(String encode) {this.encode = encode;}
    //----------------------------------------------------
    public int sendMail(String from, List<String> to,String subject,
                        String content, List<File> files, boolean draft) {
        Properties properties = getProperties();
        //1、根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session session  = Session.getDefaultInstance(properties);
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
                    //    LogX.e(TAG, "javamail send fail! to mail address format error!["+item+"]?");//创建带附件的邮件失败
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
            text.setContent(content, encode);

            // 创建容器描述数据关系
            MimeMultipart mp = new MimeMultipart();
            mp.addBodyPart(text);

            // 添加附件
                 if (files != null && files.size() > 0) {
                for (File f : files) {
                    // 创建邮件附件
                    //MimeBodyPart attach = new MimeBodyPart();
                    //DataHandler dh = new DataHandler(new FileDataSource(path + list.get(i)));
                    //DataHandler dh = new DataHandler(new FileDataSource(f));// 得到数据源
                    //attach.setDataHandler(dh); // 得到附件本身并至入BodyPart
                    //attach.setFileName(MimeUtility.encodeText(dh.getName()));// 得到文件名同样至入BodyPart
                    //mp.addBodyPart(attach);
                }
            }
            mp.setSubType("mixed");
            message.setContent(mp);
            message.saveChanges();
            // 将创建的Email写入到E盘存储
            //message.writeTo(new FileOutputStream("C:\\attachMail.eml"));
            if(!draft){
                //2、通过session得到transport对象,以便连接邮箱并发送
                Transport transport = session.getTransport();//session.getTransport("smtp");
                //3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给SMTP服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
                transport.connect(mailServerHost ,userName, passWord);
                //5、发送邮件消息
                transport.sendMessage(message, message.getAllRecipients());
                transport.close();
                LogX.d(TAG, "JavaMail send success!");
            }else{
                LogX.d(TAG, "JavaMail save success!");
            }

        } catch (AddressException e) {
            LogX.e(TAG, "JavaMail send failure!"+e.getMessage());//创建带附件的邮件失败
            return 10;
        } catch (MessagingException e) {
            LogX.e(TAG, "JavaMail send failure!"+e.getMessage());//创建带附件的邮件失败
            return 20;
        } catch (Exception e) {
            LogX.e(TAG, "JavaMail send failure!"+e.getMessage());//创建带附件的邮件失败
            return 30;
        }
        return 0;
    }

/* ---stdemo usage:--
    private void seendMail(){
        // 发送文件到邮箱(调用系统的软件)
        //sendFileIntent();
        // 发送文件到邮箱(使用JavaMail)
        new Thread() {public void run() {sendJavaMail();}}.start();
    }

    private void sendJavaMail(){
        String subject="title";
        String content="content";
        final String mailserver="smtp.163.com";
        final String mailport="25";
        final String mailac="test1@163.com";
        final String mailpa="123456";
        boolean validate=true;
        //
        List<String> to=new ArrayList<String>();
        to.add(mailac);
        ArrayList<File> attachment=new ArrayList<File>();
        attachment.add(new File(workDir + "/stdaily20180108.zip"));
        if (attachment!=null){
            StringBuilder sb= new StringBuilder("");//sb.setLength(0);//sb.delete( 0, sb.length() );
            for (File f:attachment){
                sb.append(f.getName());
                if (attachment.size()>1) {
                    sb.append(",");
                }
            }
            content=sb.toString();
            subject=attachment.get(0).getName();
        }
        boolean draft=false;
        MailUtils senMail = new MailUtils(mailserver,mailport,mailac,mailpa,validate);//这个类用来发送邮件
        senMail.sendMail(mailac,to,subject,content,attachment,draft);
    }
*/
}

