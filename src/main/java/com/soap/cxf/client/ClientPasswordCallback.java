package com.soap.cxf.client;

import org.apache.wss4j.common.ext.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;

/**
 * Callback pour fournir le mot de passe côté client
 */
public class ClientPasswordCallback implements CallbackHandler {

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (Callback callback : callbacks) {
            if (callback instanceof WSPasswordCallback) {
                WSPasswordCallback pc = (WSPasswordCallback) callback;

                // Fournir le mot de passe pour l'utilisateur
                if ("student".equals(pc.getIdentifier())) {
                    pc.setPassword("secret123");
                }
            }
        }
    }
}
