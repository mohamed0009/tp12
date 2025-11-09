package com.soap.cxf.client;

import com.soap.cxf.api.HelloService;
import com.soap.cxf.model.Person;
import jakarta.xml.ws.Service;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;

import javax.xml.namespace.QName;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Étape 10 (suite) — Client sécurisé avec WS-Security
 * 
 * Client qui envoie des credentials UsernameToken dans le header SOAP.
 * 
 * Fonctionnement :
 * 1. Crée un proxy du service comme un client normal
 * 2. Configure un intercepteur OUT WSS4J pour ajouter le UsernameToken
 * 3. Envoie le username/password dans le header WS-Security
 * 
 * Le serveur valide les credentials via ServerPasswordCallback.
 */
public class SecureClientDemo {

    private static final String WSDL_URL = "http://localhost:8080/services/hello-secure?wsdl";
    private static final String NAMESPACE = "http://api.cxf.soap.com/";
    private static final String SERVICE_NAME = "HelloService";

    // Credentials de test
    private static final String USERNAME = "student";
    private static final String PASSWORD = "secret123";

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("Client SOAP Sécurisé - WS-Security UsernameToken");
        System.out.println("=".repeat(60));
        System.out.println();

        try {
            // Création du service à partir du WSDL
            URL wsdlURL = new URL(WSDL_URL);
            QName qname = new QName(NAMESPACE, SERVICE_NAME);
            Service service = Service.create(wsdlURL, qname);

            // Obtention du proxy du service
            HelloService proxy = service.getPort(HelloService.class);

            // Configuration de la sécurité côté client
            Client client = ClientProxy.getClient(proxy);
            client.getOutInterceptors().add(createSecurityInterceptor());

            System.out.println("✓ Connexion sécurisée établie");
            System.out.println("  WSDL       : " + WSDL_URL);
            System.out.println("  Username   : " + USERNAME);
            System.out.println("  Password   : " + "•".repeat(PASSWORD.length()));
            System.out.println();

            // Test avec authentification
            testSecuredOperations(proxy);

            System.out.println("=".repeat(60));
            System.out.println("✓ Tous les tests sécurisés réussis !");
            System.out.println("=".repeat(60));

        } catch (Exception e) {
            System.err.println("✗ Erreur lors de l'exécution du client sécurisé :");
            System.err.println("  " + e.getMessage());
            System.err.println();
            System.err.println("Causes possibles :");
            System.err.println("  • Le serveur sécurisé n'est pas démarré");
            System.err.println("  • Credentials incorrects");
            System.err.println("  • Configuration WS-Security invalide");
            e.printStackTrace();
        }
    }

    /**
     * Crée un intercepteur WSS4J pour ajouter le UsernameToken
     */
    private static WSS4JOutInterceptor createSecurityInterceptor() {
        Map<String, Object> outProps = new HashMap<>();

        // Configuration de la sécurité sortante
        outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
        outProps.put(WSHandlerConstants.USER, USERNAME);
        outProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
        outProps.put(WSHandlerConstants.PW_CALLBACK_CLASS, ClientPasswordCallback.class.getName());

        return new WSS4JOutInterceptor(outProps);
    }

    /**
     * Teste les opérations sécurisées
     */
    private static void testSecuredOperations(HelloService proxy) {
        System.out.println("─".repeat(60));
        System.out.println("Test 1 : sayHello (avec authentification)");
        System.out.println("─".repeat(60));

        String greeting = proxy.sayHello("Étudiant Sécurisé");
        System.out.println("Requête  : sayHello(\"Étudiant Sécurisé\")");
        System.out.println("Réponse  : " + greeting);
        System.out.println();

        System.out.println("─".repeat(60));
        System.out.println("Test 2 : findPerson (avec authentification)");
        System.out.println("─".repeat(60));

        Person person = proxy.findPerson("P-001");
        System.out.println("Requête  : findPerson(\"P-001\")");

        if (person != null) {
            System.out.println("Réponse  : " + person.getFirstName() + " " + person.getLastName());
        }
        System.out.println();
    }

    /**
     * Callback pour fournir le mot de passe au client
     */
    public static class ClientPasswordCallback implements javax.security.auth.callback.CallbackHandler {

        @Override
        public void handle(javax.security.auth.callback.Callback[] callbacks)
                throws java.io.IOException, javax.security.auth.callback.UnsupportedCallbackException {

            for (javax.security.auth.callback.Callback callback : callbacks) {
                if (callback instanceof org.apache.wss4j.common.ext.WSPasswordCallback) {
                    org.apache.wss4j.common.ext.WSPasswordCallback pc = (org.apache.wss4j.common.ext.WSPasswordCallback) callback;

                    // Fournir le mot de passe pour l'utilisateur
                    if (USERNAME.equals(pc.getIdentifier())) {
                        pc.setPassword(PASSWORD);
                    }
                }
            }
        }
    }
}
