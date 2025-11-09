package com.soap.cxf.api;

import com.soap.cxf.model.Person;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;

/**
 * Étape 3 — Définir le contrat JAX-WS (interface de service)
 * 
 * Interface du service SOAP exposant le contrat.
 * Les annotations JAX-WS permettent la génération automatique du WSDL.
 * 
 * @WebService : définit une interface de service SOAP
 * @WebMethod : expose une méthode comme opération SOAP
 * @WebParam : nomme les paramètres dans le WSDL
 * @WebResult : nomme le résultat dans le WSDL
 */
@WebService(name = "HelloService", targetNamespace = "http://api.cxf.soap.com/")
public interface HelloService {

    /**
     * Opération simple : salutation personnalisée
     * 
     * @param name Le nom de la personne à saluer
     * @return Un message de salutation
     */
    @WebMethod(operationName = "sayHello")
    @WebResult(name = "greeting")
    String sayHello(@WebParam(name = "name") String name);

    /**
     * Opération complexe : recherche d'une personne par ID
     * 
     * @param id L'identifiant de la personne
     * @return Un objet Person sérialisé via JAXB
     */
    @WebMethod(operationName = "findPerson")
    @WebResult(name = "person")
    Person findPerson(@WebParam(name = "id") String id);

    /**
     * Opération complexe : récupération de toutes les personnes
     * 
     * @return Tableau d'objets Person
     */
    @WebMethod(operationName = "getAllPersons")
    @WebResult(name = "persons")
    Person[] getAllPersons();
}
