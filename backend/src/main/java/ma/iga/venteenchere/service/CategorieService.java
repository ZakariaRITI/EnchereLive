/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ma.iga.venteenchere.service;

import java.util.List;
import java.util.Optional;
import ma.iga.venteenchere.model.Categorie;
import ma.iga.venteenchere.repositories.CategorieRepository;
import org.springframework.stereotype.Service;

/**
 *
 * @author RIKO
 */
@Service
public class CategorieService {
    private final CategorieRepository categorieRepository;

    public CategorieService(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    // Créer une catégorie
    public Categorie creerCategorie(Categorie categorie) {
        return categorieRepository.save(categorie);
    }

    // Liste des catégories
    public List<Categorie> getAllCategories() {
        return categorieRepository.findAll();
    }

    // Récupérer une catégorie par ID
    public Optional<Categorie> getCategorieById(Long id) {
        return categorieRepository.findById(id);
    }

    // Modifier une catégorie
    public Categorie updateCategorie(Long id, Categorie categorie) {
        categorie.setIdCategorie(id);
        return categorieRepository.save(categorie);
    }

    // Supprimer une catégorie
    public void deleteCategorie(Long id) {
        categorieRepository.deleteById(id);
    }
}

