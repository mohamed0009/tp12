# 🧪 Guide de Test - Services SOAP CXF

## ⚠️ **Instructions importantes**

Le serveur **ne peut pas** être lancé en arrière-plan avec PowerShell de manière fiable.  
**Il faut utiliser 2 terminaux séparés** : un pour le serveur, un pour le client.

---

## 📋 **Prérequis**

1. ✅ Projet compilé : `mvn clean package -DskipTests -s .mvn\settings.xml`
2. ✅ Port 8080 libre
3. ✅ Fichier `jaxb-runtime-3.0.0-M5.jar` supprimé de `target\lib\`

---

## 🚀 **Test 1 : Client Simple (SimpleClient)**

### **Terminal 1 : Démarrer le serveur**

```powershell
# Naviguer vers le projet
cd C:\Users\HP\Downloads\tp2

# Supprimer l'ancien JAR JAXB (si présent)
Remove-Item target\lib\jaxb-runtime-3.0.0-M5.jar -ErrorAction SilentlyContinue

# Démarrer le serveur
java -cp "target\classes;target\lib\*" com.soap.cxf.Server
```

**Sortie attendue** :
```
============================================================
Démarrage du serveur SOAP CXF...
============================================================
INFO: Creating Service {http://api.cxf.soap.com/}HelloServiceService
INFO: Setting the server's publish address to be http://localhost:8080/services/hello
INFO: Started ServerConnector@...{HTTP/1.1, (http/1.1)}{localhost:8080}
INFO: Started o.e.j.s.h.ContextHandler@...{/services,null,AVAILABLE}
✓ Service SOAP démarré avec succès !

Informations de connexion :
  • Endpoint : http://localhost:8080/services/hello
  • WSDL     : http://localhost:8080/services/hello?wsdl

Opérations disponibles :
  1. sayHello      - Salutation personnalisée
  2. findPerson    - Recherche par ID
  3. getAllPersons - Liste complète

Serveur en attente de requêtes...
```

### **Terminal 2 : Tester le client**

```powershell
# Ouvrir un NOUVEAU terminal PowerShell
cd C:\Users\HP\Downloads\tp2

# Exécuter le client simple
java -cp "target\classes;target\lib\*" com.soap.cxf.client.SimpleClient
```

**Sortie attendue** :
```
=== Client SOAP Minimal ===

✓ Réponse : Bonjour, Étudiant !
```

### **Vérifications supplémentaires**

Dans **Terminal 1** (serveur), vous devriez voir les logs de requête :
```
INFO: Inbound Message
----------------------------
Endpoint Address: http://localhost:8080/services/hello
Content-Type: text/xml
...
```

---

## 🔐 **Test 2 : Client Sécurisé (SecureClientDemo)**

### **Terminal 1 : Démarrer le serveur sécurisé**

```powershell
# Arrêter le serveur précédent (Ctrl+C dans Terminal 1)

# Démarrer le serveur sécurisé
java -cp "target\classes;target\lib\*" com.soap.cxf.SecureServer
```

**Sortie attendue** :
```
============================================================
Démarrage du serveur SOAP CXF SÉCURISÉ (WS-Security)
============================================================
INFO: Creating Service {http://api.cxf.soap.com/}HelloServiceService
INFO: Started ServerConnector@...{HTTP/1.1, (http/1.1)}{localhost:8080}
✓ Service SOAP sécurisé démarré avec succès !

Endpoint : http://localhost:8080/services/hello-secure
Sécurité : WS-Security UsernameToken
Credentials valides :
  • Username : student
  • Password : secret123

Serveur en attente de requêtes sécurisées...
```

### **Terminal 2 : Tester le client sécurisé**

```powershell
# Exécuter le client sécurisé
java -cp "target\classes;target\lib\*" com.soap.cxf.client.SecureClientDemo
```

**Sortie attendue** :
```
============================================================
Client SOAP Sécurisé - WS-Security UsernameToken
============================================================

✓ Connexion sécurisée établie
  WSDL       : http://localhost:8080/services/hello-secure?wsdl
  Username   : student
  Password   : ********

───────────────────────────────────────────────────────────
Test 1 : sayHello("Student")
───────────────────────────────────────────────────────────
✓ Réponse : Bonjour, Student !

───────────────────────────────────────────────────────────
Test 2 : findPerson("P-001")
───────────────────────────────────────────────────────────
✓ Personne trouvée :
  • ID         : P-001
  • Prénom     : Alice
  • Nom        : Martin
  • Âge        : 28

───────────────────────────────────────────────────────────
Test 3 : getAllPersons()
───────────────────────────────────────────────────────────
✓ 4 personnes trouvées :

  1. Person{id='P-001', firstName='Alice', lastName='Martin', age=28}
  2. Person{id='P-002', firstName='Bob', lastName='Dupont', age=35}
  3. Person{id='P-003', firstName='Charlie', lastName='Bernard', age=42}
  4. Person{id='P-004', firstName='Diana', lastName='Lefebvre', age=29}

============================================================
✓ Tous les tests passés avec succès !
============================================================
```

---

## 🌐 **Test 3 : Vérifier le WSDL dans le navigateur**

### **Pendant que le serveur tourne (Terminal 1)**

Ouvrir dans votre navigateur :
- **Service simple** : http://localhost:8080/services/hello?wsdl
- **Service sécurisé** : http://localhost:8080/services/hello-secure?wsdl

**Contenu attendu** :
```xml
<?xml version="1.0" encoding="UTF-8"?>
<wsdl:definitions targetNamespace="http://api.cxf.soap.com/" 
                  xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/"
                  xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/">
  <wsdl:types>
    <xs:schema targetNamespace="http://api.cxf.soap.com/">
      <xs:complexType name="Person">
        <xs:sequence>
          <xs:element name="id" type="xs:string"/>
          <xs:element name="firstName" type="xs:string"/>
          <xs:element name="lastName" type="xs:string"/>
          <xs:element name="age" type="xs:int"/>
        </xs:sequence>
      </xs:complexType>
    </xs:schema>
  </wsdl:types>
  
  <wsdl:message name="sayHello">
    <wsdl:part name="name" type="xs:string"/>
  </wsdl:message>
  
  <wsdl:portType name="HelloService">
    <wsdl:operation name="sayHello">
      <wsdl:input message="tns:sayHello"/>
      <wsdl:output message="tns:sayHelloResponse"/>
    </wsdl:operation>
    <!-- ... autres opérations ... -->
  </wsdl:portType>
  
  <wsdl:service name="HelloServiceService">
    <wsdl:port name="HelloServicePort" binding="tns:HelloServiceSoapBinding">
      <soap:address location="http://localhost:8080/services/hello"/>
    </wsdl:port>
  </wsdl:service>
</wsdl:definitions>
```

---

## 🛠️ **Test 4 : SoapUI (optionnel)**

### **Installation**
Télécharger SoapUI : https://www.soapui.org/downloads/soapui/

### **Étapes**
1. **Créer un nouveau projet SOAP**
   - File → New SOAP Project
   - Project Name : `HelloServiceTest`
   - Initial WSDL : `http://localhost:8080/services/hello?wsdl`
   - Cliquer sur **OK**

2. **Tester sayHello**
   - Développer : `HelloServiceSoapBinding` → `sayHello` → `Request 1`
   - Modifier le XML :
   ```xml
   <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/1.1/" 
                     xmlns:api="http://api.cxf.soap.com/">
      <soapenv:Header/>
      <soapenv:Body>
         <api:sayHello>
            <name>SoapUI User</name>
         </api:sayHello>
      </soapenv:Body>
   </soapenv:Envelope>
   ```
   - Cliquer sur le bouton **▶ (Play)**

3. **Réponse attendue**
   ```xml
   <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/1.1/">
      <soap:Body>
         <ns2:sayHelloResponse xmlns:ns2="http://api.cxf.soap.com/">
            <return>Bonjour, SoapUI User !</return>
         </ns2:sayHelloResponse>
      </soap:Body>
   </soap:Envelope>
   ```

4. **Tester findPerson**
   ```xml
   <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/1.1/" 
                     xmlns:api="http://api.cxf.soap.com/">
      <soapenv:Header/>
      <soapenv:Body>
         <api:findPerson>
            <id>P-001</id>
         </api:findPerson>
      </soapenv:Body>
   </soapenv:Envelope>
   ```

5. **Réponse attendue**
   ```xml
   <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/1.1/">
      <soap:Body>
         <ns2:findPersonResponse xmlns:ns2="http://api.cxf.soap.com/">
            <return>
               <id>P-001</id>
               <firstName>Alice</firstName>
               <lastName>Martin</lastName>
               <age>28</age>
            </return>
         </ns2:findPersonResponse>
      </soap:Body>
   </soap:Envelope>
   ```

---

## ❌ **Dépannage**

### **Problème : "Connection refused"**

**Cause** : Le serveur n'est pas démarré ou s'est arrêté.

**Solution** :
```powershell
# Vérifier si le serveur tourne
netstat -ano | findstr :8080

# Si rien n'apparaît, redémarrer le serveur dans Terminal 1
java -cp "target\classes;target\lib\*" com.soap.cxf.Server
```

### **Problème : "ClassNotFoundException: jakarta.xml.bind.Validator"**

**Cause** : L'ancien JAR JAXB 3.0.0-M5 est présent.

**Solution** :
```powershell
# Supprimer l'ancien JAR
Remove-Item target\lib\jaxb-runtime-3.0.0-M5.jar -Force

# Vérifier la suppression
Test-Path target\lib\jaxb-runtime-3.0.0-M5.jar
# Doit retourner : False

# Recompiler si nécessaire
mvn clean package -DskipTests -s .mvn\settings.xml
```

### **Problème : "Port 8080 already in use"**

**Cause** : Une autre application utilise le port 8080.

**Solution** :
```powershell
# Trouver le processus
netstat -ano | findstr :8080
# Note le PID (dernière colonne)

# Tuer le processus (remplacer 1234 par le PID)
taskkill /PID 1234 /F

# OU changer le port dans Server.java
# Modifier : SERVICE_URL = "http://localhost:9090/services/hello"
```

### **Problème : Le serveur s'arrête immédiatement**

**Cause** : Lancé en arrière-plan avec `Start-Job` ou script batch.

**Solution** : **Ne PAS** lancer en arrière-plan. Utiliser 2 terminaux séparés.

---

## 📊 **Checklist de test complète**

- [ ] **Compilation** : `mvn clean package -DskipTests -s .mvn\settings.xml`
- [ ] **Suppression JAXB** : `Remove-Item target\lib\jaxb-runtime-3.0.0-M5.jar`
- [ ] **Serveur simple** : `java -cp "target\classes;target\lib\*" com.soap.cxf.Server`
- [ ] **Client simple** : `java -cp "target\classes;target\lib\*" com.soap.cxf.client.SimpleClient`
- [ ] **WSDL accessible** : http://localhost:8080/services/hello?wsdl
- [ ] **Serveur sécurisé** : `java -cp "target\classes;target\lib\*" com.soap.cxf.SecureServer`
- [ ] **Client sécurisé** : `java -cp "target\classes;target\lib\*" com.soap.cxf.client.SecureClientDemo`
- [ ] **Test SoapUI** : Projet créé avec WSDL, requêtes testées

---

## 🎯 **Résumé : Commandes rapides**

### **Option A : Scripts batch (recommandé pour tests multiples)**

**Terminal 1** :
```batch
.\start-server.bat
```

**Terminal 2** :
```batch
.\test-client.bat
```

### **Option B : Commandes directes**

**Terminal 1** :
```powershell
Remove-Item target\lib\jaxb-runtime-3.0.0-M5.jar -ErrorAction SilentlyContinue
java -cp "target\classes;target\lib\*" com.soap.cxf.Server
```

**Terminal 2** :
```powershell
java -cp "target\classes;target\lib\*" com.soap.cxf.client.SimpleClient
```

---

**✅ Bon test !**
