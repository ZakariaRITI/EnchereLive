/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ma.iga.venteenchere.service;

import java.time.LocalDateTime;
import java.util.List;
import ma.iga.venteenchere.model.Notification;
import ma.iga.venteenchere.model.TypeNotification;
import ma.iga.venteenchere.model.Utilisateur;
import ma.iga.venteenchere.repositories.NotificationRepository;
import ma.iga.venteenchere.repositories.UtilisateurRepository;
import org.springframework.stereotype.Service;

/**
 *
 * @author RIKO
 */
@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UtilisateurRepository utilisateurRepository;

    // Créer une notification
     public NotificationService(NotificationRepository notificationRepository,
                               UtilisateurRepository utilisateurRepository) {
        this.notificationRepository = notificationRepository;
        this.utilisateurRepository = utilisateurRepository;
    }
     
     public Notification creerNotification(Long idUtilisateur, String message, String type) {

        Notification notification = new Notification();

        notification.setMessage(message);
        notification.setType(TypeNotification.valueOf(type));

        // ✅ date automatique
        notification.setDateNotification(LocalDateTime.now());

        // ✅ charger l'utilisateur
        Utilisateur utilisateur = utilisateurRepository.findById(idUtilisateur)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        notification.setDestinataire(utilisateur);

        return notificationRepository.save(notification);
    }
     
    // Récupérer les notifications d’un utilisateur
    public List<Notification> getNotificationsByUtilisateur(Long idUtilisateur) {
        return notificationRepository.findByDestinataireIdUtilisateur(idUtilisateur);
    }

    // Supprimer une notification
    public void deleteNotification(Long idNotification) {
        notificationRepository.deleteById(idNotification);
    }
}

