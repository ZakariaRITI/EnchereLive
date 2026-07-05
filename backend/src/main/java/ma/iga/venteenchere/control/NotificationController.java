/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ma.iga.venteenchere.control;

import java.util.List;
import java.util.Map;
import ma.iga.venteenchere.model.Notification;
import ma.iga.venteenchere.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author RIKO
 */
@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins="http://localhost:3000")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // POST /api/notifications
    @PostMapping
public ResponseEntity<Notification> creerNotification(
        @RequestBody Map<String, Object> body) {

    Long idUtilisateur = Long.valueOf(body.get("idUtilisateur").toString());
    String message = body.get("message").toString();
    String type = body.get("type").toString();

    Notification notification =
            notificationService.creerNotification(idUtilisateur, message, type);

    return ResponseEntity.status(201).body(notification);
}


    // GET /api/notifications/utilisateur/{idUtilisateur}
    @GetMapping("/utilisateur/{idUtilisateur}")
    public List<Notification> getNotificationsByUtilisateur(@PathVariable Long idUtilisateur) {
        return notificationService.getNotificationsByUtilisateur(idUtilisateur);
    }

    // DELETE /api/notifications/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok(Map.of("message", "Notification supprimée avec succès", "idNotification", id));
    }
}
