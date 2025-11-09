package com.soap.cxf.client;

import com.soap.cxf.api.HelloService;
import jakarta.xml.ws.Service;

import javax.xml.namespace.QName;
import java.net.URL;

/**
 * Client SOAP minimal - Version simplifiée
 */
public class SimpleClient {

    public static void main(String[] args) {
        try {
            System.out.println("=== Client SOAP Minimal ===\n");

            // Connexion au service via WSDL
            URL wsdlURL = new URL("http://localhost:8080/services/hello?wsdl");
            QName qname = new QName("http://api.cxf.soap.com/", "HelloServiceService");
            Service service = Service.create(wsdlURL, qname);
            HelloService proxy = service.getPort(HelloService.class);

            // Test simple
            String result = proxy.sayHello("Étudiant");
            System.out.println("✓ Réponse : " + result);

        } catch (Exception e) {
            System.err.println("✗ Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
