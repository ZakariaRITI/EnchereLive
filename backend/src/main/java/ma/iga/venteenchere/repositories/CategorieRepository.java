/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ma.iga.venteenchere.repositories;

import ma.iga.venteenchere.model.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author RIKO
 */
public interface CategorieRepository extends JpaRepository<Categorie, Long> {
    // Recherche par nom
    Categorie findByNomCategorie(String nomCategorie);
}
