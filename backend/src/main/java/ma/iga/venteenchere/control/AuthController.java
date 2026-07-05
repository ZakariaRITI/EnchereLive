/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ma.iga.venteenchere.control;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import ma.iga.venteenchere.model.TypeCompte;
import ma.iga.venteenchere.service.UtilisateurService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author RIKO
 */
@RestController
@CrossOrigin(origins="http://localhost:3000")
@RequestMapping("/api/auth")
public class AuthController {
    private final UtilisateurService utilisateurService;
    public static final String SECRET_KEY = "MA_CLE_SECRETE_TRES_LONGUE_POUR_JWT_AU_MOINS_256_BITS";

    public AuthController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String motDePasse = loginData.get("motDePasse");
        TypeCompte typeCompte = TypeCompte.valueOf(loginData.get("typeCompte").toUpperCase());

        return utilisateurService.authentifier(email, motDePasse, typeCompte)
        .map(user -> {
                // Générer le token
                String token = Jwts.builder()
                    .setSubject(user.getEmail())                     // l'email comme sujet
                    .claim("idUtilisateur", user.getIdUtilisateur()) // on peut mettre des claims
                    .claim("nom", user.getNom())
                    .claim("typeCompte", user.getTypeCompte().name())
                    .setIssuedAt(new Date())
                    .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS))) // 1 jour de validité
                    .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .compact();

                return ResponseEntity.ok(Map.of(
                    "idUtilisateur", user.getIdUtilisateur(),
                    "nom", user.getNom(),
                    "token", token
                ));
            })
            .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                           .body(Map.of("message", "Identifiants invalides")));
    }
   
    // POST /api/auth/logout
@PostMapping("/logout")
public ResponseEntity<?> logout() {
    // Avec JWT stateless, le serveur ne peut pas invalider le token directement
    // Il suffit d'informer le front-end de supprimer le token côté client
    return ResponseEntity.ok(Map.of(
        "message", "Déconnexion réussie, supprimez votre token côté client"
    ));
}

}

