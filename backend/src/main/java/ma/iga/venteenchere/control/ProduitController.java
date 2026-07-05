/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ma.iga.venteenchere.control;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import ma.iga.venteenchere.model.Produit;
import ma.iga.venteenchere.service.ProduitService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author RIKO
 */
@RestController
@CrossOrigin(origins="http://localhost:3000")
@RequestMapping("/api/produits")
public class ProduitController {
    private final ProduitService produitService;

    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    // GET /api/produits
    @GetMapping
    public List<Produit> getAllProduits() {
        return produitService.getAllProduits();
    }

    // POST /api/produits
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<Produit> ajouterProduit(
        @RequestHeader("Authorization") String authHeader,
        @RequestParam String nom,
        @RequestParam String description,
        @RequestParam Double prixInitial,
        @RequestParam String dateFinEnchere,
        @RequestParam Long idCategorie,
        @RequestParam MultipartFile image
) {
    try {
        Produit produit = new Produit();
        produit.setNom(nom);
        produit.setDescription(description);
        produit.setPrixInitial(prixInitial);

        // 🔐 Extraire le token
        String token = authHeader.replace("Bearer ", "");

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(AuthController.SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        Long idUtilisateur = Long.valueOf(claims.get("idUtilisateur").toString());

        // 📁 Sauvegarde de l'image
        String imageName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path imagePath = Paths.get("uploads").resolve(imageName);
        Files.createDirectories(imagePath.getParent());
        Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

        produit.setImage(imageName);

        // 💾 Sauvegarde produit
        Produit savedProduit = produitService.ajouterProduitSimple(
                produit,
                idCategorie,
                idUtilisateur,
                dateFinEnchere
        );

        return ResponseEntity.status(201).body(savedProduit);

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(null);
    }
}


// PUT /api/produits/{id}
@PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<Produit> updateProduit(
        @PathVariable Long id,
        @RequestHeader("Authorization") String authHeader,
        @RequestParam String nom,
        @RequestParam String description,
        @RequestParam Double prixInitial,
        @RequestParam String dateFinEnchere,
        @RequestParam Long idCategorie,
        @RequestParam(required = false) MultipartFile image
) {
    try {
        Produit produit = new Produit();
        produit.setNom(nom);
        produit.setDescription(description);
        produit.setPrixInitial(prixInitial);

        // Décoder le token pour récupérer l'utilisateur
        String token = authHeader.replace("Bearer ", "");
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(AuthController.SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
        Long idUtilisateur = Long.valueOf(claims.get("idUtilisateur").toString());

        // Gestion de l'image
        String imageName = null;
        if (image != null && !image.isEmpty()) {
            imageName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path imagePath = Paths.get("uploads").resolve(imageName);
            Files.createDirectories(imagePath.getParent());
            Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
        }

        // Appel du service pour mettre à jour
        Produit updatedProduit = produitService.updateProduitSimple(
                id, produit, idCategorie, idUtilisateur, dateFinEnchere, imageName
        );

        return ResponseEntity.ok(updatedProduit);

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(null);
    }
}





    // GET /api/produits/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Produit> getProduitById(@PathVariable Long id) {
        return produitService.getProduitById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/produits/utilisateur/{id}
    @GetMapping("/utilisateur/{idUtilisateur}")
    public List<Produit> getProduitsByUtilisateur(@PathVariable Long idUtilisateur) {
        return produitService.getProduitsByUtilisateur(idUtilisateur);
    }

   

    // DELETE /api/produits/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduit(@PathVariable Long id) {
        produitService.deleteProduit(id);
        return ResponseEntity.ok(Map.of("message", "Produit supprimé avec succès", "idProduit", id));
    }
    
    @GetMapping("/encours")
public List<Produit> getProduitsEncours(
        @RequestParam(required = false) Long categorie,  // Filtrer par catégorie
        @RequestParam(required = false) String tri       // Tri optionnel : "dateFin"
) {
    return produitService.getProduitsEncours(categorie, tri);
}

    
    

}

