EnchèreLive

Plateforme web d'enchères en temps réel .

🚀 Description du projet

EnchèreLive est une application web Full-Stack permettant aux utilisateurs de publier des produits, de participer à des enchères en temps réel et de suivre l'historique des mises. Le projet met l'accent sur la sécurité des transactions et la réactivité de l'interface (Single Page Application).

✨ Fonctionnalités clés

    Authentification sécurisée : Système de connexion basé sur les tokens JWT.

    Gestion des enchères : Publication d'annonces, suivi des enchères en direct et détermination automatique du gagnant.

    Architecture Rôles : Espaces différenciés entre utilisateurs (enchérisseurs) et administrateurs (gestion du catalogue).

    Navigation fluide : Expérience utilisateur optimisée via React Router.

    Notifications : Système d'alerte pour les utilisateurs sur l'état de leurs enchères.

🛠 Stack Technique
Front-End

    Interface : React

    Navigation : React Router

    Communication : Axios (API REST)

    Styling : CSS3

Back-End

    Framework : Spring Boot

    Sécurité : Spring Security & JWT

    Base de données : PostgreSQL

    ORM : JPA/Hibernate

⚙️ Installation

    Cloner le dépôt :
    Bash

    git clone https://github.com/votre-utilisateur/enchere-live.git

    Configuration Back-End :

        Configurez votre base de données PostgreSQL dans application.properties.

        Lancez le projet via votre IDE ou via Maven : mvn spring-boot:run.

    Configuration Front-End :
    Bash

    cd frontend
    npm install
    npm start
