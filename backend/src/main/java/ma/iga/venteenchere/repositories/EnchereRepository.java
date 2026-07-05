/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ma.iga.venteenchere.repositories;

import java.util.List;
import ma.iga.venteenchere.model.Enchere;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author RIKO
 */
public interface EnchereRepository extends JpaRepository<Enchere, Long> {
    // Récupérer les enchères d’un utilisateur
    List<Enchere> findByUtilisateurIdUtilisateur(Long idUtilisateur);

    // Récupérer les enchères d’un produit
    List<Enchere> findByProduitIdProduit(Long idProduit);

    // Trouver la meilleure enchère pour un produit
    Enchere findTopByProduitIdProduitOrderByMontantDesc(Long idProduit);
}

