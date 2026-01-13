package com.example.KinetoWebsite.Service;

import com.example.KinetoWebsite.Model.DTO.AppointmentDTO;
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
    public void sendAppointmentConfirmation(AppointmentDTO appointment) {
        // 1. Trimite email catre CLIENT
        String clientSubject = "Confirmare Programare - PhysioVanu";
        String clientBody = buildClientEmailBody(appointment);
        sendEmailInternal(appointment.getCustomerEmail(), clientSubject, clientBody);

        // 2. Trimite email catre ADMIN (Terapeut)
        String adminSubject = "Programare Nouă (" + appointment.getServiceName() + ") - " + appointment.getPatientName();
        String adminBody = buildAdminEmailBody(appointment);
        sendEmailInternal(adminEmail, adminSubject, adminBody);
    }

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

    // --- HTML PENTRU CLIENT (Design curat) ---
    private String buildClientEmailBody(AppointmentDTO app) {
        String serviceName = app.getServiceName() != null ? app.getServiceName() : "Nespecificat";
        String date = app.getDate() != null ? app.getDate().toString() : "Nespecificat";
        String phone = app.getPhoneNumber();
        String message = app.getAdditionalInfo() != null ? app.getAdditionalInfo() : "-";

        return """
            <div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; color: #333;'>
                <div style='background-color: #667eea; color: white; padding: 20px; text-align: center; border-radius: 8px 8px 0 0;'>
                    <h1>Confirmare Programare</h1>
                </div>
                <div style='border: 1px solid #ddd; padding: 20px; border-top: none;'>
                    <p>Salut <strong>%s</strong>,</p>
                    <p>Îți mulțumim că ai ales PhysioVanu! Am înregistrat solicitarea ta cu următoarele detalii:</p>
                    
                    <table style='width: 100%%; border-collapse: collapse; margin: 20px 0;'>
                        <tr>
                            <td style='padding: 8px; border-bottom: 1px solid #eee;'><strong>Serviciu:</strong></td>
                            <td style='padding: 8px; border-bottom: 1px solid #eee;'>%s</td>
                        </tr>
                        <tr>
                            <td style='padding: 8px; border-bottom: 1px solid #eee;'><strong>Data preferată:</strong></td>
                            <td style='padding: 8px; border-bottom: 1px solid #eee;'>%s</td>
                        </tr>
                        <tr>
                            <td style='padding: 8px; border-bottom: 1px solid #eee;'><strong>Telefon contact:</strong></td>
                            <td style='padding: 8px; border-bottom: 1px solid #eee;'>%s</td>
                        </tr>
                        <tr>
                            <td style='padding: 8px; border-bottom: 1px solid #eee;'><strong>Mesaj/Detalii:</strong></td>
                            <td style='padding: 8px; border-bottom: 1px solid #eee;'>%s</td>
                        </tr>
                    </table>

                    <p style='background-color: #f9f9f9; padding: 10px; border-left: 4px solid #667eea;'>
                        <strong>Notă:</strong> Te voi contacta telefonic în curând pentru a stabili ora exactă.
                    </p>
                    
                    <br>
                    <p>Cu respect,<br>Echipa PhysioVanu</p>
                </div>
            </div>
            """.formatted(app.getPatientName(), serviceName, date, phone, message);
    }

    // --- HTML PENTRU ADMIN (Include toate detaliile tehnice) ---
    private String buildAdminEmailBody(AppointmentDTO app) {
        String serviceName = app.getServiceName() != null ? app.getServiceName() : "Nespecificat";
        String extraInfo = app.getAdditionalInfo() != null ? app.getAdditionalInfo() : "Fără mesaj adițional";

        return """
            <div style='font-family: Arial, sans-serif; color: #333; border: 1px solid #ccc; padding: 20px; max-width: 600px;'>
                <h2 style='color: #d9534f; border-bottom: 2px solid #d9534f; padding-bottom: 10px;'>🔔 Programare Nouă!</h2>
                
                <p>Un client nou a completat formularul pe site.</p>
                
                <h3>Detalii Client:</h3>
                <ul>
                    <li><b>Nume:</b> %s</li>
                    <li><b>Telefon:</b> <a href='tel:%s'>%s</a></li>
                    <li><b>Email:</b> <a href='mailto:%s'>%s</a></li>
                </ul>

                <h3>Detalii Serviciu:</h3>
                <ul>
                    <li><b>Serviciu ales:</b> %s</li>
                    <li><b>Data preferată:</b> %s</li>
                </ul>

                <div style='background-color: #f8d7da; padding: 15px; margin-top: 15px; border-radius: 5px;'>
                    <b>Mesaj client / Detalii extra:</b><br>
                    <i>%s</i>
                </div>

                <p style='margin-top: 20px;'>Te rog să contactezi clientul pentru confirmare.</p>
            </div>
            """.formatted(
                app.getPatientName(),
                app.getPhoneNumber(), app.getPhoneNumber(),
                app.getCustomerEmail(), app.getCustomerEmail(),
                serviceName,
                app.getDate().toString(),
                extraInfo
        );
    }
}