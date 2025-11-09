package com.soap.cxf;

import com.soap.cxf.impl.HelloServiceImpl;
import com.soap.cxf.security.ServerPasswordCallback;
import jakarta.xml.ws.Endpoint;
import org.apache.cxf.jaxws.EndpointImpl;

/**
 * Étape 10 — Ajouter la sécurité WS-Security (UsernameToken)
 * 
 * Serveur SOAP sécurisé avec authentification UsernameToken.
 * 
 * Ce serveur expose DEUX endpoints :
 * 1. Non sécurisé : http://localhost:8080/services/hello
 * 2. Sécurisé : http://localhost:8080/services/hello-secure
 * 
 * Le endpoint sécurisé utilise WS-Security avec UsernameToken :
 * - Les clients doivent fournir username/password dans le header SOAP
 * - WSS4J valide les credentials via ServerPasswordCallback
 * - PasswordType = Text (recommandé : Digest + HTTPS en production)
 * 
 * Utilisateurs autorisés :
 * - student / secret123
 * - admin / Admin@2024
 * - teacher / Pass@123
 */
public class SecureServer {

    private static final String SERVICE_URL = "http://localhost:8080/services/hello";
    private static final String SECURE_SERVICE_URL = "http://localhost:8080/services/hello-secure";

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("Démarrage du serveur SOAP CXF avec WS-Security");
        System.out.println("=".repeat(60));
        System.out.println();

        try {
            // 1. Publication du service NON sécurisé (pour comparaison)
            Endpoint.publish(SERVICE_URL, new HelloServiceImpl());
            System.out.println("✓ Service NON sécurisé démarré");
            System.out.println("  • Endpoint : " + SERVICE_URL);
            System.out.println("  • WSDL     : " + SERVICE_URL + "?wsdl");
            System.out.println();

            // 2. Publication du service SÉCURISÉ avec WS-Security
            EndpointImpl secureEndpoint = (EndpointImpl) Endpoint.publish(
                    SECURE_SERVICE_URL,
                    new HelloServiceImpl());

            // Configuration de la sécurité (intercepteur IN pour valider le token)
            secureEndpoint.getInInterceptors().add(
                    ServerPasswordCallback.createSecurityInterceptor());

            System.out.println("✓ Service SÉCURISÉ démarré (WS-Security UsernameToken)");
            System.out.println("  • Endpoint : " + SECURE_SERVICE_URL);
            System.out.println("  • WSDL     : " + SECURE_SERVICE_URL + "?wsdl");
            System.out.println();

            System.out.println("Configuration de sécurité :");
            System.out.println("  • Type      : WS-Security UsernameToken");
            System.out.println("  • Password  : PasswordText (⚠ Utiliser HTTPS en production)");
            System.out.println();

            System.out.println("Utilisateurs autorisés :");
            System.out.println("  1. student  / secret123");
            System.out.println("  2. admin    / Admin@2024");
            System.out.println("  3. teacher  / Pass@123");
            System.out.println();

            System.out.println("=".repeat(60));
            System.out.println("Tests recommandés :");
            System.out.println("  • SoapUI : Importer le WSDL et configurer WS-Security");
            System.out.println("           (Project → WS-Security Configurations → ");
            System.out.println("            Outgoing → Username → student / secret123)");
            System.out.println();
            System.out.println("  • Client Java : Utiliser SecureClientDemo");
            System.out.println();
            System.out.println("Appuyez sur Ctrl+C pour arrêter le serveur.");
            System.out.println("=".repeat(60));

            // Garder le serveur actif
            Thread.currentThread().join();

        } catch (Exception e) {
            System.err.println("✗ Erreur lors du démarrage du serveur :");
            System.err.println("  " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
