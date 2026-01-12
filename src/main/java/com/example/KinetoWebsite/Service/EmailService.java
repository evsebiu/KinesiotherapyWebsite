package com.example.KinetoWebsite.Service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {

    // --- Injectare variabile din application.properties ---
    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private String port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.admin.email}")
    private String adminEmail;

    /**
     * Metoda principala care trebuie apelata din Controller.
     * Trimite confirmare la client si notificare la admin.
     */
    public void processAppointment(String clientEmail, String clientName, String date) {
        // 1. Trimite email catre CLIENT
        String clientSubject = "Confirmare Programare - PhysioVanu";
        String clientBody = buildClientEmailBody(clientName, date);
        sendEmailInternal(clientEmail, clientSubject, clientBody);

        // 2. Trimite email catre ADMIN (Terapeut)
        String adminSubject = "Programare Noua - " + clientName;
        String adminBody = buildAdminEmailBody(clientName, clientEmail, date);
        // Folosim adresa de admin incarcata din fisier
        sendEmailInternal(adminEmail, adminSubject, adminBody);
    }

    /**
     * Metoda generica de trimitere email (privata, folosita doar intern)
     */
    private void sendEmailInternal(String to, String subject, String htmlBody) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", port);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            // Setam un nume prietenos "PhysioVanu" ca sa nu apara doar adresa de email
            message.setFrom(new InternetAddress(adminEmail, "PhysioVanu"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setContent(htmlBody, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("Email trimis cu succes catre: " + to);

        } catch (Exception e) {
            System.err.println("EROARE la trimiterea emailului catre " + to + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- Metode ajutatoare pentru construirea textului HTML ---

    private String buildClientEmailBody(String name, String date) {
        return "<div style='font-family: Arial, sans-serif; color: #333;'>"
                + "<h2>Salut " + name + ",</h2>"
                + "<p>Programarea ta a fost înregistrată cu succes.</p>"
                + "<p><b>Data:</b> " + date + "</p>"
                + "<br>"
                + "<p>Iti  multumesc!</p>"
                + "<p><i>Echipa PhysioVanu</i></p>"
                + "</div>";
    }

    private String buildAdminEmailBody(String clientName, String clientEmail, String date) {
        return "<div style='font-family: Arial, sans-serif; color: #333; border: 1px solid #ccc; padding: 10px;'>"
                + "<h2 style='color: #d9534f;'>Programare Nouă!</h2>"
                + "<p>Un client nou s-a programat pe site:</p>"
                + "<ul>"
                + "<li><b>Nume:</b> " + clientName + "</li>"
                + "<li><b>Email:</b> " + clientEmail + "</li>"
                + "<li><b>Data:</b> " + date + "</li>"
                + "</ul>"
                + "<p>Verifică agenda pentru detalii.</p>"
                + "</div>";
    }
}