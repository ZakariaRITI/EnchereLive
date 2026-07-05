/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ma.iga.venteenchere.repositories;

import java.time.LocalDateTime;
import java.util.List;
import ma.iga.venteenchere.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author RIKO
 */
public interface ProduitRepository extends JpaRepository<Produit, Long> {
    // Récupérer tous les produits d’un utilisateur
    List<Produit> findByProprietaireIdUtilisateur(Long idUtilisateur);

    // Récupérer les produits par catégorie
    List<Produit> findByCategorieIdCategorie(Long idCategorie);
    
    List<Produit> findByDateFinEnchereBeforeAndEstTermineeFalse(LocalDateTime date);
    
    List<Produit> findByEstTermineeFalse();
    
    // Récupérer tous les produits dont l'enchère n'est pas terminée
    @Query("SELECT p FROM Produit p WHERE p.estTerminee = false")
    List<Produit> findAllEncours();


}
