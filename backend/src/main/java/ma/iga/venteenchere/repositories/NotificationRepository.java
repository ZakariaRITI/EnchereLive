/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ma.iga.venteenchere.repositories;

import java.util.List;
import ma.iga.venteenchere.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author RIKO
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Récupérer les notifications d’un utilisateur
    List<Notification> findByDestinataireIdUtilisateur(Long idUtilisateur);
}

