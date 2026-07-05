/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ma.iga.venteenchere.control;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.List;
import java.util.Map;
import ma.iga.venteenchere.model.Enchere;
import ma.iga.venteenchere.service.EnchereService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author RIKO
 */
@RestController
@CrossOrigin(origins="http://localhost:3000")
@RequestMapping("/api/encheres")
public class EnchereController {
    private final EnchereService enchereService;

    public EnchereController(EnchereService enchereService) {
        this.enchereService = enchereService;
    }

    // POST /api/encheres
    @PostMapping
public ResponseEntity<?> placerEnchere(
        @RequestBody Enchere enchere,
        @RequestHeader("Authorization") String authHeader
) {
    try {
        String token = authHeader.replace("Bearer ", "");
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(AuthController.SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        Long idUtilisateur = Long.valueOf(claims.get("idUtilisateur").toString());

        Enchere saved = enchereService.placerEnchereSimple(
                enchere.getProduit().getIdProduit(),
                idUtilisateur,
                enchere.getMontant()
        );

        return ResponseEntity.status(201).body(saved);

    } catch (RuntimeException e) {
        return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
    }
}



    // GET /api/encheres/utilisateur/{idUtilisateur}
    @GetMapping("/utilisateur/{idUtilisateur}")
    public List<Enchere> getEncheresByUtilisateur(@PathVariable Long idUtilisateur) {
        return enchereService.getEncheresByUtilisateur(idUtilisateur);
    }

    // GET /api/encheres
    @GetMapping
    public List<Enchere> getAllEncheres() {
        return enchereService.getAllEncheres();
    }

    // GET /api/encheres/gagnant/{idProduit}
    @GetMapping("/gagnant/{idProduit}")
    public ResponseEntity<Enchere> getGagnant(@PathVariable Long idProduit) {
        return enchereService.getGagnant(idProduit)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // GET /api/encheres/utilisateur/{idUtilisateur}/encours
@GetMapping("/utilisateur/{idUtilisateur}/encours")
public List<Enchere> getEncheresEnCours(@PathVariable Long idUtilisateur) {
    return enchereService.getEncheresEnCoursByUtilisateur(idUtilisateur);
}

// GET /api/encheres/utilisateur/{idUtilisateur}/remportees
@GetMapping("/utilisateur/{idUtilisateur}/remportees")
public List<Enchere> getEncheresRemportees(@PathVariable Long idUtilisateur) {
    return enchereService.getEncheresRemporteesByUtilisateur(idUtilisateur);
}

// 🔹 Historique des enchères pour un produit
@GetMapping("/produit/{idProduit}")
public List<Enchere> getEncheresParProduit(@PathVariable Long idProduit) {
    return enchereService.getEncheresByProduit(idProduit);
}

}
