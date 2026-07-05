/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ma.iga.venteenchere.service;

import java.util.List;
import java.util.Optional;
import ma.iga.venteenchere.model.TypeCompte;
import ma.iga.venteenchere.model.Utilisateur;
import ma.iga.venteenchere.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author RIKO
 */
@Service
public class UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;
    
     @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    // Inscription
    public Utilisateur creerUtilisateur(Utilisateur utilisateur) {
        // Hasher le mot de passe avant l'enregistrement
        String motDePasseHash = passwordEncoder.encode(utilisateur.getMotDePasse());
        utilisateur.setMotDePasse(motDePasseHash);

        return utilisateurRepository.save(utilisateur);
    }

   // Authentification (login)
public Optional<Utilisateur> authentifier(String email, String motDePasse, TypeCompte typeCompte) {
    Optional<Utilisateur> user = utilisateurRepository.findByEmail(email);

    if (user.isPresent()) {
        Utilisateur u = user.get();

        // Vérifier le mot de passe hashé
        boolean motDePasseOk = passwordEncoder.matches(motDePasse, u.getMotDePasse());

        // Vérifier le type de compte
        boolean typeCompteOk = u.getTypeCompte().equals(typeCompte);

        if (motDePasseOk && typeCompteOk) {
            return Optional.of(u);
        }
    }

    return Optional.empty();
}

    // Liste des utilisateurs
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    // Récupérer un utilisateur par ID
    public Optional<Utilisateur> getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id);
    }

    // Modifier un utilisateur
public Utilisateur updateUtilisateur(Long id, Utilisateur utilisateur) {
    // Récupérer l'utilisateur existant
    Optional<Utilisateur> existingUser = utilisateurRepository.findById(id);

    if (existingUser.isPresent()) {
        Utilisateur u = existingUser.get();

        u.setNom(utilisateur.getNom());
        u.setEmail(utilisateur.getEmail());
        u.setTypeCompte(utilisateur.getTypeCompte());

        // Hacher le mot de passe seulement s'il a été modifié / fourni
        if (utilisateur.getMotDePasse() != null && !utilisateur.getMotDePasse().isEmpty()) {
            String motDePasseHash = passwordEncoder.encode(utilisateur.getMotDePasse());
            u.setMotDePasse(motDePasseHash);
        }

        return utilisateurRepository.save(u);
    }

    return null; // ou lever une exception si l'utilisateur n'existe pas
}


    // Supprimer un utilisateur
    public void deleteUtilisateur(Long id) {
        utilisateurRepository.deleteById(id);
    }
}

