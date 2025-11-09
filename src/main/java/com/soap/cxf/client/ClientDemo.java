package com.soap.cxf.client;

import com.soap.cxf.api.HelloService;
import com.soap.cxf.model.Person;
import jakarta.xml.ws.Service;

import javax.xml.namespace.QName;
import java.net.URL;

/**
 * Étape 8 — Écrire un client Java minimal
 * 
 * Client SOAP qui consomme le service JAX-WS.
 * 
 * Fonctionnement :
 * 1. Lit le WSDL pour découvrir le contrat
 * 2. Crée un proxy dynamique du service
 * 3. Invoque les opérations comme des méthodes Java
 * 
 * Le client envoie des messages SOAP (envelope) sur HTTP.
 * JAXB sérialise/désérialise automatiquement les objets.
 */
public class ClientDemo {

    private static final String WSDL_URL = "http://localhost:8080/services/hello?wsdl";
    private static final String NAMESPACE = "http://api.cxf.soap.com/";
    private static final String SERVICE_NAME = "HelloService";

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("Client SOAP - Démonstration");
        System.out.println("=".repeat(60));
        System.out.println();

        try {
            // Création du service à partir du WSDL
            URL wsdlURL = new URL(WSDL_URL);
            QName qname = new QName(NAMESPACE, SERVICE_NAME);
            Service service = Service.create(wsdlURL, qname);

            // Obtention du proxy du service
            HelloService proxy = service.getPort(HelloService.class);

            System.out.println("✓ Connexion au service établie");
            System.out.println("  WSDL : " + WSDL_URL);
            System.out.println();

            // Test 1 : sayHello (opération simple)
            testSayHello(proxy);

            // Test 2 : findPerson (opération complexe avec JAXB)
            testFindPerson(proxy);

            // Test 3 : getAllPersons (tableau d'objets)
            testGetAllPersons(proxy);

            System.out.println("=".repeat(60));
            System.out.println("✓ Tous les tests réussis !");
            System.out.println("=".repeat(60));

        } catch (Exception e) {
            System.err.println("✗ Erreur lors de l'exécution du client :");
            System.err.println("  " + e.getMessage());
            System.err.println();
            System.err.println("Vérifications :");
            System.err.println("  • Le serveur est-il démarré ?");
            System.err.println("  • L'URL du WSDL est-elle accessible ?");
            e.printStackTrace();
        }
    }

    /**
     * Test de l'opération sayHello
     */
    private static void testSayHello(HelloService proxy) {
        System.out.println("─".repeat(60));
        System.out.println("Test 1 : sayHello");
        System.out.println("─".repeat(60));

        String response = proxy.sayHello("Lachgar");
        System.out.println("Requête  : sayHello(\"Lachgar\")");
        System.out.println("Réponse  : " + response);
        System.out.println();
    }

    /**
     * Test de l'opération findPerson
     */
    private static void testFindPerson(HelloService proxy) {
        System.out.println("─".repeat(60));
        System.out.println("Test 2 : findPerson");
        System.out.println("─".repeat(60));

        Person person = proxy.findPerson("P-001");
        System.out.println("Requête  : findPerson(\"P-001\")");

        if (person != null) {
            System.out.println("Réponse  : Person trouvée");
            System.out.println("  • ID        : " + person.getId());
            System.out.println("  • Prénom    : " + person.getFirstName());
            System.out.println("  • Nom       : " + person.getLastName());
            System.out.println("  • Âge       : " + person.getAge());
        } else {
            System.out.println("Réponse  : Aucune personne trouvée");
        }
        System.out.println();
    }

    /**
     * Test de l'opération getAllPersons
     */
    private static void testGetAllPersons(HelloService proxy) {
        System.out.println("─".repeat(60));
        System.out.println("Test 3 : getAllPersons");
        System.out.println("─".repeat(60));

        Person[] persons = proxy.getAllPersons();
        System.out.println("Requête  : getAllPersons()");
        System.out.println("Réponse  : " + persons.length + " personne(s) trouvée(s)");
        System.out.println();

        for (int i = 0; i < persons.length; i++) {
            Person p = persons[i];
            System.out.println("  [" + (i + 1) + "] " + p.getId() + " - "
                    + p.getFirstName() + " " + p.getLastName() + " (" + p.getAge() + " ans)");
        }
        System.out.println();
    }
}
