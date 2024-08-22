package testUtilities;

import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.client.MailgunClient;
import com.mailgun.model.message.Message;
import com.mailgun.model.message.MessageResponse;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class SendEmailUtil {

    public static MessageResponse sendMutualFundsReportMailGun(String htmlContent, boolean fundsHealthCheck) {
        Properties prop = GenericUtils.getDataFromPropertyFile("global");
        String apiKey = prop.getProperty("mailgun_api_key");
        String domain = prop.getProperty("mailgun_domain");

        MailgunMessagesApi mailgunMessagesApi = MailgunClient.config(apiKey).createApi(MailgunMessagesApi.class);

        Message message = Message.builder().from("Gitlab Runner <USER@YOURDOMAIN.COM>").to("singh.saurav@icloud.com").subject(getEmailSubject(fundsHealthCheck)).html(htmlContent).build();

        return mailgunMessagesApi.sendMessage(domain, message);
    }

    public static String sendMutualFundsReportSMTP(String htmlContent, boolean fundsHealthCheck) {
        final String username = "singh.raja27@outlook.com";
        final String password = smtpPassword();
        String to = "singh.saurav@icloud.com";
        String host = "smtp.office365.com";
        int port = 587;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            jakarta.mail.Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(jakarta.mail.Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(getEmailSubject(fundsHealthCheck));
            message.setContent(htmlContent, "text/html; charset=utf-8");
            Transport.send(message);
            return "Email sent successfully.";
        } catch (MessagingException e) {
            System.err.println("Email not sent. Error: " + e.getMessage());
            return "Email not sent.";
        }
    }

    private static String getEmailSubject(boolean fundsHealthCheck) {
        return fundsHealthCheck ? "[PASSED] Mutual Fund Tracker: All Funds are beating Category Average and BenchMark Returns" : "[FAILED] Mutual Fund Tracker: One or more funds are not able to beat Category Average and BenchMark Returns";
    }

    public static void saveHtml(String htmlContent) {
        String filePath = "output.html";
        File file = new File(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(htmlContent);
            System.out.println("HTML content saved to file: " + filePath);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String smtpPassword() {
        String password = System.getenv("SMTP_EMAIL_PASSWORD");
        if (password == null) {
            throw new RuntimeException("SMTP password is not available. Please set the environment variable.");
        }
        return password;
    }
}
