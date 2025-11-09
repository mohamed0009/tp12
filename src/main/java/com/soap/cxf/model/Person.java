package com.soap.cxf.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Étape 2 — Créer le modèle JAXB (POJO sérialisable)
 * 
 * Modèle Person annoté JAXB pour la sérialisation XML.
 * JAXB permet la liaison automatique XML ↔ Objet Java.
 */
@XmlRootElement(name = "Person")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "id", "firstName", "lastName", "age" })
public class Person {

    @XmlElement(required = true)
    private String id;

    @XmlElement(name = "firstName")
    private String firstName;

    @XmlElement(name = "lastName")
    private String lastName;

    @XmlElement
    private int age;

    // Constructeur par défaut requis par JAXB
    public Person() {
    }

    // Constructeur avec paramètres
    public Person(String id, String firstName, String lastName, int age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    // Getters et Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                '}';
    }
}
