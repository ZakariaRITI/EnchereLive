/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ma.iga.venteenchere.service;

import java.time.LocalDateTime;
import java.util.List;
import ma.iga.venteenchere.model.Enchere;
import ma.iga.venteenchere.model.Produit;
import ma.iga.venteenchere.repositories.EnchereRepository;
import ma.iga.venteenchere.repositories.ProduitRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
/**
 *
 * @author RIKO
 */
@Service
public class EnchereSchedulerService {

    private final ProduitRepository produitRepository;
    private final EnchereRepository enchereRepository;
    private final NotificationService notificationService;

    public EnchereSchedulerService(ProduitRepository produitRepository,
                                   EnchereRepository enchereRepository,
                                   NotificationService notificationService) {
        this.produitRepository = produitRepository;
        this.enchereRepository = enchereRepository;
        this.notificationService = notificationService;
    }

    @Scheduled(fixedRate = 60000) // toutes les 60 secondes
    public void verifierEncheresTerminees() {
        LocalDateTime maintenant = LocalDateTime.now();
        List<Produit> produitsTermines = produitRepository
                .findByDateFinEnchereBeforeAndEstTermineeFalse(maintenant);

        for (Produit produit : produitsTermines) {
            Enchere meilleureEnchere = enchereRepository
                    .findTopByProduitIdProduitOrderByMontantDesc(produit.getIdProduit());

            if (meilleureEnchere != null) {
                Long gagnantId = meilleureEnchere.getUtilisateur().getIdUtilisateur();
                notificationService.creerNotification(
                        gagnantId,
                        "Vous avez remporté l'enchère sur le produit " + produit.getNom(),
                        "VICTOIRE"
                );
            }

            // Marquer le produit comme traité
            produit.setEstTerminee(true);
            produitRepository.save(produit);
        }
    }
}
