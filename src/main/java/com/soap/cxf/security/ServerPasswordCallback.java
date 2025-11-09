package com.soap.cxf.security;

import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Callback de validation pour WS-Security UsernameToken
 * 
 * Vérifie les credentials (username/password) envoyés dans le header SOAP.
 * Cette classe simule une base de données d'utilisateurs.
 */
public class ServerPasswordCallback implements javax.security.auth.callback.CallbackHandler {

    // Base de données simulée des utilisateurs autorisés
    private static final Map<String, String> users = new HashMap<>();

    static {
        users.put("student", "secret123");
        users.put("admin", "Admin@2024");
        users.put("teacher", "Pass@123");
    }

    @Override
    public void handle(javax.security.auth.callback.Callback[] callbacks)
            throws java.io.IOException, javax.security.auth.callback.UnsupportedCallbackException {

        for (javax.security.auth.callback.Callback callback : callbacks) {
            if (callback instanceof org.apache.wss4j.common.ext.WSPasswordCallback) {
                org.apache.wss4j.common.ext.WSPasswordCallback pc = (org.apache.wss4j.common.ext.WSPasswordCallback) callback;

                String username = pc.getIdentifier();
                String password = users.get(username);

                if (password != null) {
                    pc.setPassword(password);
                    System.out.println("✓ Authentification réussie pour l'utilisateur : " + username);
                } else {
                    System.err.println("✗ Utilisateur inconnu : " + username);
                    throw new java.io.IOException("Utilisateur inconnu : " + username);
                }
            }
        }
    }

    /**
     * Crée un intercepteur WSS4J configuré pour valider UsernameToken
     * 
     * @return Intercepteur de sécurité configuré
     */
    public static WSS4JInInterceptor createSecurityInterceptor() {
        Map<String, Object> inProps = new HashMap<>();

        // Configuration de la sécurité
        inProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
        inProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
        inProps.put(WSHandlerConstants.PW_CALLBACK_CLASS, ServerPasswordCallback.class.getName());

        return new WSS4JInInterceptor(inProps);
    }
}
