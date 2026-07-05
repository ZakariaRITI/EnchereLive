/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ma.iga.venteenchere.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import ma.iga.venteenchere.model.Produit;
import ma.iga.venteenchere.repositories.CategorieRepository;
import ma.iga.venteenchere.repositories.ProduitRepository;
import ma.iga.venteenchere.repositories.UtilisateurRepository;
import org.springframework.stereotype.Service;

/**
 *
 * @author RIKO
 */
@Service
public class ProduitService {
    private final ProduitRepository produitRepository;
    private final CategorieRepository categorieRepository;
    private final UtilisateurRepository utilisateurRepository;
    
     public ProduitService(ProduitRepository produitRepository,
                          CategorieRepository categorieRepository,
                          UtilisateurRepository utilisateurRepository) {
        this.produitRepository = produitRepository;
        this.categorieRepository = categorieRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    // Ajouter un produit
    public Produit ajouterProduit(Produit produit) {
        return produitRepository.save(produit);
    }
    
    
    // Méthode simple pour ajouter un produit
    public Produit ajouterProduitSimple(Produit produit, Long idCategorie, Long idUtilisateur, String dateFinStr) {

        // Convertir la date si elle est envoyée en String "dd/MM/yyyy HH:mm"
        if (dateFinStr != null && !dateFinStr.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            produit.setDateFinEnchere(LocalDateTime.parse(dateFinStr, formatter));
        }

        // Assigner catégorie et propriétaire
        produit.setCategorie(categorieRepository.findById(idCategorie).orElse(null));
        produit.setProprietaire(utilisateurRepository.findById(idUtilisateur).orElse(null));

        return produitRepository.save(produit);
    }
    
    // Liste des produits
    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    // Récupérer un produit par ID
    public Optional<Produit> getProduitById(Long id) {
        return produitRepository.findById(id);
    }

    // Produits d’un utilisateur
    public List<Produit> getProduitsByUtilisateur(Long idUtilisateur) {
        return produitRepository.findByProprietaireIdUtilisateur(idUtilisateur);
    }

    // Modifier un produit
   // Modifier un produit correctement avec image
public Produit updateProduitSimple(Long id, Produit produit, Long idCategorie, Long idUtilisateur, String dateFinStr, String imageName) {
    // Récupérer le produit existant
    Produit existing = produitRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

    // Mettre à jour les champs
    existing.setNom(produit.getNom());
    existing.setDescription(produit.getDescription());
    existing.setPrixInitial(produit.getPrixInitial());

    // Date fin enchère
    if (dateFinStr != null && !dateFinStr.isEmpty()) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        existing.setDateFinEnchere(LocalDateTime.parse(dateFinStr, formatter));
    }

    // Catégorie et propriétaire
    existing.setCategorie(categorieRepository.findById(idCategorie).orElse(null));
    existing.setProprietaire(utilisateurRepository.findById(idUtilisateur).orElse(null));

    // Si une nouvelle image est fournie
    if (imageName != null && !imageName.isEmpty()) {
        existing.setImage(imageName);
    }

    return produitRepository.save(existing);
}


    // Supprimer un produit
    public void deleteProduit(Long id) {
        produitRepository.deleteById(id);
    }
    
    public List<Produit> getProduitsEncours(Long idCategorie, String tri) {
    List<Produit> produits;

    // Si une catégorie est précisée, on filtre par catégorie
    if (idCategorie != null) {
        produits = produitRepository.findByCategorieIdCategorie(idCategorie)
                    .stream()
                    .filter(p -> !p.isEstTerminee()) // On garde seulement les produits en cours
                    .toList();
    } else {
        // Sinon on prend tous les produits en cours
        produits = produitRepository.findByEstTermineeFalse();
    }

    // Tri si demandé
    if ("dateFin".equals(tri)) {
        produits = produits.stream()
                .sorted(Comparator.comparing(Produit::getDateFinEnchere))
                .toList();
    }

    return produits;
}

    
    
}

