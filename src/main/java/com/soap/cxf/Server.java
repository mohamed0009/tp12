package com.soap.cxf;

import com.soap.cxf.impl.HelloServiceImpl;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

/**
 * Étape 5 — Publier le service avec CXF (serveur embarqué)
 * 
 * Serveur CXF embarqué qui expose le service SOAP.
 * Le service est accessible à :
 * - Endpoint : http://localhost:8080/services/hello
 * - WSDL : http://localhost:8080/services/hello?wsdl
 * 
 * Points d'attention :
 * - Le port 8080 doit être libre
 * - Le WSDL est généré automatiquement (code-first)
 * - CXF gère le transport HTTP et la sérialisation JAXB
 */
public class Server {

    private static final String SERVICE_URL = "http://localhost:8080/services/hello";

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("Démarrage du serveur SOAP CXF...");
        System.out.println("=".repeat(60));

        try {
            // Publication du service avec JaxWsServerFactoryBean
            JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();
            factory.setServiceClass(com.soap.cxf.api.HelloService.class);
            factory.setAddress(SERVICE_URL);
            factory.setServiceBean(new HelloServiceImpl());

            // Démarrer le serveur
            org.apache.cxf.endpoint.Server server = factory.create();

            System.out.println("✓ Service SOAP démarré avec succès !");
            System.out.println();
            System.out.println("Informations de connexion :");
            System.out.println("  • Endpoint : " + SERVICE_URL);
            System.out.println("  • WSDL     : " + SERVICE_URL + "?wsdl");
            System.out.println();
            System.out.println("Opérations disponibles :");
            System.out.println("  1. sayHello      - Salutation personnalisée");
            System.out.println("  2. findPerson    - Recherche par ID");
            System.out.println("  3. getAllPersons - Liste complète");
            System.out.println();
            System.out.println("=".repeat(60));
            System.out.println("Testez avec SoapUI ou le client Java fourni.");
            System.out.println("Appuyez sur Ctrl+C pour arrêter le serveur.");
            System.out.println("=".repeat(60));

            // Garder le serveur actif
            System.out.println("\nServeur en attente de requêtes...\n");

            // Boucle infinie pour garder le serveur actif
            while (true) {
                try {
                    Thread.sleep(10000); // Sleep 10 secondes
                } catch (InterruptedException e) {
                    System.out.println("\nArrêt du serveur...");
                    break;
                }
            }

        } catch (Exception e) {
            System.err.println("✗ Erreur lors du démarrage du serveur :");
            System.err.println("  " + e.getMessage());
            System.err.println();
            System.err.println("Vérifications :");
            System.err.println("  • Le port 8080 est-il libre ?");
            System.err.println("  • Les dépendances Maven sont-elles installées ?");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
