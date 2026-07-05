/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ma.iga.venteenchere.repositories;

import java.util.Optional;
import ma.iga.venteenchere.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author RIKO
 */
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    // Trouver un utilisateur par email (utile pour login)
    Optional<Utilisateur> findByEmail(String email);

    // Vérifier si un email existe déjà (pour inscription)
    boolean existsByEmail(String email);
}

