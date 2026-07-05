/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ma.iga.venteenchere.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import ma.iga.venteenchere.model.Enchere;
import ma.iga.venteenchere.model.Produit;
import ma.iga.venteenchere.model.Utilisateur;
import ma.iga.venteenchere.repositories.EnchereRepository;
import ma.iga.venteenchere.repositories.ProduitRepository;
import ma.iga.venteenchere.repositories.UtilisateurRepository;
import org.springframework.stereotype.Service;

/**
 *
 * @author RIKO
 */
@Service
public class EnchereService {
    private final EnchereRepository enchereRepository;
    private final ProduitRepository produitRepository;
    private final UtilisateurRepository utilisateurRepository;
    
     public EnchereService(
            EnchereRepository enchereRepository,
            ProduitRepository produitRepository,
            UtilisateurRepository utilisateurRepository
    ) {
        this.enchereRepository = enchereRepository;
        this.produitRepository = produitRepository;
        this.utilisateurRepository = utilisateurRepository;
    }
    
    // Placer une enchère
    public Enchere placerEnchereSimple(Long idProduit, Long idUtilisateur, Double montant) {

    Produit produit = produitRepository.findById(idProduit)
            .orElseThrow(() -> new RuntimeException("Produit introuvable"));

    Utilisateur utilisateur = utilisateurRepository.findById(idUtilisateur)
            .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

    // 🔴 RÈGLE 1 : enchère > prix initial
    if (montant <= produit.getPrixInitial()) {
        throw new RuntimeException(
                "Le montant doit être supérieur au prix initial (" + produit.getPrixInitial() + ")"
        );
    }

    // 🔴 RÈGLE 2 : enchère > meilleure enchère actuelle
    Enchere meilleure = enchereRepository
            .findTopByProduitIdProduitOrderByMontantDesc(idProduit);

    if (meilleure != null && montant <= meilleure.getMontant()) {
        throw new RuntimeException(
                "Le montant doit être supérieur à l'enchère actuelle (" + meilleure.getMontant() + ")"
        );
    }

    // ✅ Création de l'enchère
    Enchere enchere = new Enchere();
    enchere.setProduit(produit);
    enchere.setUtilisateur(utilisateur);
    enchere.setMontant(montant);
    enchere.setDateEnchere(LocalDateTime.now());

    return enchereRepository.save(enchere);
}


    // Récupérer les enchères d’un utilisateur
    public List<Enchere> getEncheresByUtilisateur(Long idUtilisateur) {
        return enchereRepository.findByUtilisateurIdUtilisateur(idUtilisateur);
    }

    // Récupérer toutes les enchères (admin)
    public List<Enchere> getAllEncheres() {
        return enchereRepository.findAll();
    }

    // Récupérer les enchères d’un produit
    public List<Enchere> getEncheresByProduit(Long idProduit) {
        return enchereRepository.findByProduitIdProduit(idProduit);
    }

    // Vérifier le gagnant (meilleure enchère)
    public Optional<Enchere> getGagnant(Long idProduit) {
        return Optional.ofNullable(enchereRepository.findTopByProduitIdProduitOrderByMontantDesc(idProduit));
    }
    
    // Enchères en cours pour un utilisateur
    public List<Enchere> getEncheresEnCoursByUtilisateur(Long idUtilisateur) {
        List<Enchere> toutes = enchereRepository.findByUtilisateurIdUtilisateur(idUtilisateur);
        return toutes.stream()
                .filter(e -> e.getProduit().getDateFinEnchere().isAfter(LocalDateTime.now()))
                .toList();
    }

    // Enchères remportées pour un utilisateur
    public List<Enchere> getEncheresRemporteesByUtilisateur(Long idUtilisateur) {
        List<Enchere> toutes = enchereRepository.findByUtilisateurIdUtilisateur(idUtilisateur);
        return toutes.stream()
                .filter(e -> e.getProduit().getDateFinEnchere().isBefore(LocalDateTime.now())) // enchère terminée
                .filter(e -> {
                    Enchere gagnant = enchereRepository
                            .findTopByProduitIdProduitOrderByMontantDesc(e.getProduit().getIdProduit());
                    return gagnant != null && gagnant.getUtilisateur().getIdUtilisateur().equals(idUtilisateur);
                })
                .toList();
    }
}

