package com.soap.cxf.impl;

import com.soap.cxf.api.HelloService;
import com.soap.cxf.model.Person;
import jakarta.jws.WebService;

import java.util.HashMap;
import java.util.Map;

/**
 * Étape 4 — Implémenter la logique métier
 * 
 * Implémentation du service HelloService.
 * La logique métier reste simple et testable.
 * 
 * @WebService : lie l'implémentation à l'interface
 *             - endpointInterface : référence l'interface du contrat
 *             - serviceName : nom du service dans le WSDL
 *             - portName : nom du port dans le WSDL
 *             - targetNamespace : namespace du service
 */
@WebService(endpointInterface = "com.soap.cxf.api.HelloService", serviceName = "HelloService", portName = "HelloServicePort", targetNamespace = "http://api.cxf.soap.com/")
public class HelloServiceImpl implements HelloService {

    // Base de données simulée (en mémoire)
    private static final Map<String, Person> personDatabase = new HashMap<>();

    // Initialisation de données de test
    static {
        personDatabase.put("P-001", new Person("P-001", "Mohammed", "Lachgar", 35));
        personDatabase.put("P-002", new Person("P-002", "Sarah", "Martin", 28));
        personDatabase.put("P-003", new Person("P-003", "Ahmed", "Benali", 42));
        personDatabase.put("P-004", new Person("P-004", "Fatima", "Zahra", 31));
    }

    /**
     * Implémentation de l'opération sayHello
     * 
     * @param name Le nom de la personne
     * @return Message de salutation personnalisé
     */
    @Override
    public String sayHello(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Bonjour, invité !";
        }
        return "Bonjour, " + name + " ! Bienvenue au service SOAP CXF.";
    }

    /**
     * Implémentation de l'opération findPerson
     * 
     * @param id L'identifiant de la personne
     * @return L'objet Person correspondant ou null
     */
    @Override
    public Person findPerson(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }

        Person person = personDatabase.get(id);

        if (person != null) {
            System.out.println("Person trouvée : " + person);
        } else {
            System.out.println("Aucune personne trouvée avec l'ID : " + id);
        }

        return person;
    }

    /**
     * Implémentation de l'opération getAllPersons
     * 
     * @return Tableau de toutes les personnes
     */
    @Override
    public Person[] getAllPersons() {
        System.out.println("Récupération de toutes les personnes (" + personDatabase.size() + " enregistrements)");
        return personDatabase.values().toArray(new Person[0]);
    }
}
