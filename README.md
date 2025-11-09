# TP 12 : Service SOAP avec Apache CXF (JAX-WS, JAXB, WSDL, WS-Security)

 Vue d'ensemble

Projet complet démontrant la création d'un service SOAP avec Apache CXF, incluant :
- **JAX-WS** : Exposition de services web SOAP
- **JAXB** : Sérialisation/désérialisation XML vers Java
- **WSDL** : Génération automatique du contrat (code-first)
- **WS-Security** : Authentification via UsernameToken (WSS4J)
- **Clients Java** : Consommation du service (standard et sécurisé)

---

Architecture du projet

```
tp2/
├── pom.xml                                    # Configuration Maven avec CXF 4.0.3
├── README.md                                  # Ce fichier
└── src/main/java/com/soap/cxf/
    ├── model/
    │   └── Person.java                        # POJO avec annotations JAXB
    ├── api/
    │   └── HelloService.java                  # Interface JAX-WS (@WebService)
    ├── impl/
    │   └── HelloServiceImpl.java              # Implémentation du service
    ├── security/
    │   └── ServerPasswordCallback.java        # Validation des credentials
    ├── client/
    │   ├── ClientDemo.java                    # Client SOAP standard
    │   └── SecureClientDemo.java              # Client avec WS-Security
    ├── Server.java                            # Serveur non sécurisé
    └── SecureServer.java                      # Serveur avec WS-Security
```

---

Démarrage rapide

1. Prérequis

- **Java 17** (ou 11+)
- **Maven 3.8+**
- **Port 8080** libre
- **SoapUI** (optionnel, pour tester graphiquement)

2. Installation des dépendances

```powershell
mvn clean install
```

3. Lancer le serveur (choix)

Option A : Serveur simple (sans sécurité)

```powershell
mvn exec:java -Dexec.mainClass="com.soap.cxf.Server"
```

Endpoints disponibles :
- Service : `http://localhost:8080/services/hello`
- WSDL : `http://localhost:8080/services/hello?wsdl`

#### Option B : Serveur sécurisé (avec WS-Security)

```powershell
mvn exec:java -Dexec.mainClass="com.soap.cxf.SecureServer"
```

**Endpoints disponibles :**
- Service non sécurisé : `http://localhost:8080/services/hello?wsdl`
- Service sécurisé : `http://localhost:8080/services/hello-secure?wsdl`



## Tests

### Avec le client Java (serveur simple)

```powershell
# Dans un nouveau terminal (serveur doit être démarré)
mvn exec:java -Dexec.mainClass="com.soap.cxf.client.ClientDemo"
```

**Résultat attendu :**
```
Test 1 : sayHello
  Requête  : sayHello("Lachgar")
  Réponse  : Bonjour, Lachgar ! Bienvenue au service SOAP CXF.

Test 2 : findPerson
  Requête  : findPerson("P-001")
  Réponse  : Person trouvée
    • ID        : P-001
    • Prénom    : Mohammed
    • Nom       : Lachgar
    • Âge       : 35

Test 3 : getAllPersons
  Requête  : getAllPersons()
  Réponse  : 4 personne(s) trouvée(s)
```

### Avec le client Java sécurisé

```powershell
# Serveur sécurisé doit être démarré
mvn exec:java -Dexec.mainClass="com.soap.cxf.client.SecureClientDemo"
```

### Avec SoapUI (Étape 7)

#### Configuration du projet

1. **Créer un nouveau projet SOAP** :
   - File → New SOAP Project
   - Project Name : `CXF-Test`
   - Initial WSDL : `http://localhost:8080/services/hello?wsdl`
   - Create Requests

2. **Tester sayHello** :
   - Ouvrir Request1 sous `sayHello`
   - Modifier le XML :
   ```xml
   <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                     xmlns:api="http://api.cxf.soap.com/">
      <soapenv:Header/>
      <soapenv:Body>
         <api:sayHello>
            <name>Lachgar</name>
         </api:sayHello>
      </soapenv:Body>
   </soapenv:Envelope>
   ```
   - Cliquer sur le bouton Submit

3. **Tester findPerson** :
   ```xml
   <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                     xmlns:api="http://api.cxf.soap.com/">
      <soapenv:Header/>
      <soapenv:Body>
         <api:findPerson>
            <id>P-001</id>
         </api:findPerson>
      </soapenv:Body>
   </soapenv:Envelope>
   ```

#### Configuration WS-Security (service sécurisé)

1. **Ajouter le WSDL sécurisé** :
   - File → New SOAP Project
   - Initial WSDL : `http://localhost:8080/services/hello-secure?wsdl`

2. **Configurer WS-Security** :
   - Clic droit sur le projet → Show Project View
   - Onglet **WS-Security Configurations**
   - Sous **Outgoing WS-Security Configurations** → Add
   - Nom : `StudentAuth`

3. **Ajouter Username Token** :
   - Sélectionner `StudentAuth` → Add → Username
   - Username : `student`
   - Password : `secret123`
   - Password Type : `PasswordText`

4. **Appliquer à la requête** :
   - Ouvrir Request1
   - En bas à gauche : Onglet **Auth**
   - Outgoing WSS : Sélectionner `StudentAuth`
   - Envoyer la requête

**Résultat attendu :**
- Réponse SOAP valide avec les données
- Sans credentials : erreur SOAP Fault avec message d'erreur sécurité

---

## Comprendre le WSDL (Étape 6)

Accéder au WSDL : `http://localhost:8080/services/hello?wsdl`

### Structure du WSDL

```xml
<definitions>
  <!-- 1. TYPES : Définitions XSD -->
  <types>
    <xsd:complexType name="Person">
      <xsd:sequence>
        <xsd:element name="id" type="xsd:string"/>
        <xsd:element name="firstName" type="xsd:string"/>
        <xsd:element name="lastName" type="xsd:string"/>
        <xsd:element name="age" type="xsd:int"/>
      </xsd:sequence>
    </xsd:complexType>
  </types>
  
  <!-- 2. MESSAGES : Requêtes/Réponses -->
  <message name="sayHello">
    <part name="parameters" element="tns:sayHello"/>
  </message>
  <message name="sayHelloResponse">
    <part name="parameters" element="tns:sayHelloResponse"/>
  </message>
  
  <!-- 3. PORT TYPE : Interface logique -->
  <portType name="HelloService">
    <operation name="sayHello">
      <input message="tns:sayHello"/>
      <output message="tns:sayHelloResponse"/>
    </operation>
    <operation name="findPerson">...</operation>
    <operation name="getAllPersons">...</operation>
  </portType>
  
  <!-- 4. BINDING : Protocole SOAP/HTTP -->
  <binding name="HelloServicePortBinding" type="tns:HelloService">
    <soap:binding transport="http://schemas.xmlsoap.org/soap/http"/>
    ...
  </binding>
  
  <!-- 5. SERVICE : Endpoint réel -->
  <service name="HelloService">
    <port name="HelloServicePort" binding="tns:HelloServicePortBinding">
      <soap:address location="http://localhost:8080/services/hello"/>
    </port>
  </service>
</definitions>
```

### Utilisation pratique

1. **Générer des stubs clients** (optionnel) :
   ```powershell
   mvn cxf-codegen:wsdl2java -Dwsdl=http://localhost:8080/services/hello?wsdl
   ```

2. **Importer dans SoapUI** : Voir section précédente

3. **Interopérabilité** : Le WSDL permet à des clients hétérogènes (Java, .NET, Python, PHP) de consommer le service

---

## WS-Security en détail (Étape 10)

### Architecture de sécurité

```
Client                              Serveur
  │                                    │
  │ 1. Requête SOAP avec Header       │
  │    WS-Security (UsernameToken)    │
  ├───────────────────────────────────>│
  │                                    │ 2. WSS4JInInterceptor
  │                                    │    valide le token
  │                                    │
  │                                    │ 3. ServerPasswordCallback
  │                                    │    vérifie username/password
  │                                    │
  │                                    │ 4. Si OK → traitement métier
  │ 5. Réponse SOAP                   │    Si KO → SOAPFault
  │<───────────────────────────────────┤
```

### Exemple de message SOAP avec UsernameToken

**Requête client :**
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
  <soapenv:Header>
    <wsse:Security xmlns:wsse="...">
      <wsse:UsernameToken>
        <wsse:Username>student</wsse:Username>
        <wsse:Password Type="PasswordText">secret123</wsse:Password>
      </wsse:UsernameToken>
    </wsse:Security>
  </soapenv:Header>
  <soapenv:Body>
    <api:sayHello>
      <name>Test</name>
    </api:sayHello>
  </soapenv:Body>
</soapenv:Envelope>
```

### Recommandations de sécurité

| Configuration            | Sécurité | Usage                          |
|--------------------------|----------|--------------------------------|
| PasswordText + HTTP      | Faible   | Développement uniquement       |
| PasswordText + HTTPS     | Correct  | Production acceptable          |
| PasswordDigest + HTTPS   | Bien     | Recommandé production          |
| Signature + Chiffrement  | Maximum  | Haute sécurité                 |

---

## Dépannage (Étape 11)

### Problème : WSDL introuvable

**Symptôme :** `404 Not Found` sur `?wsdl`

**Solutions :**
```powershell
# Vérifier que le serveur est démarré
# Vérifier l'URL exacte (avec ou sans -secure)
# Tester avec curl
curl http://localhost:8080/services/hello?wsdl
```

### Problème : Port 8080 occupé

**Symptôme :** `Address already in use`

**Solution :**
```powershell
# Trouver le processus
netstat -ano | findstr :8080

# Tuer le processus (remplacer PID)
taskkill /PID <PID> /F
```

### Problème : Classes jakarta.* manquantes

**Symptôme :** `ClassNotFoundException: jakarta.xml.ws.*`

**Solution :**
- CXF 4.x utilise Jakarta EE (jakarta.*)
- CXF 3.x utilise Java EE (javax.*)
- Vérifier la version dans `pom.xml`

### Problème : UsernameToken rejeté

**Symptôme :** `SOAPFault: Security processing failed`

**Vérifications :**
1. Username/password corrects
2. PasswordType correspond (Text vs Digest)
3. Activer les logs :
   ```powershell
   mvn exec:java -Dorg.apache.cxf.Logger=org.apache.cxf.common.logging.Slf4jLogger -Dlog4j.logger.org.apache.cxf=DEBUG
   ```

---

## Concepts clés (Étape 0)

### Apache CXF
Framework Apache pour services web SOAP (JAX-WS) et REST (JAX-RS). Gère :
- Publication d'endpoints
- Transport HTTP/HTTPS
- Génération WSDL
- Intégration WS-Security

### JAX-WS (Java API for XML Web Services)
Standard Java pour exposer des services SOAP via annotations.

**Annotations principales :**
- `@WebService` : Définit une interface/implémentation de service
- `@WebMethod` : Expose une méthode comme opération SOAP
- `@WebParam` : Nomme les paramètres dans le WSDL
- `@WebResult` : Nomme le résultat dans le WSDL

### JAXB (Java Architecture for XML Binding)
Standard Java pour liaison XML ↔ Objet.

**Annotations principales :**
- `@XmlRootElement` : Définit l'élément racine XML
- `@XmlElement` : Mappe un champ vers un élément XML
- `@XmlAccessorType` : Définit la stratégie d'accès (FIELD, PROPERTY)
- `@XmlType` : Contrôle l'ordre des éléments

### WSDL (Web Services Description Language)
Contrat XML du service décrivant :
- types : Schémas XSD des données
- messages : Requêtes et réponses
- portType : Interface logique (opérations)
- binding : Protocole (SOAP/HTTP)
- service : Endpoint physique (URL)

### WS-Security (WSS4J)
Sécurité au niveau message SOAP. Supporte :
- UsernameToken : Authentification par username/password
- Signature : Garantit l'intégrité et la non-répudiation
- Chiffrement : Garantit la confidentialité
- Timestamp : Prévient les attaques par rejeu

### UDDI (Étape 9 - Culture SOA)
Registre historique "publier–trouver–lier" pour services web.
- Peu utilisé aujourd'hui (remplacé par API Gateways, Service Mesh)
- Utile pour comprendre l'architecture SOA classique

---

## Checklist de validation

- [ ] WSDL accessible et parsable (`?wsdl`)
- [ ] `sayHello` fonctionne (SoapUI + client Java)
- [ ] `findPerson` retourne un objet Person sérialisé JAXB
- [ ] `getAllPersons` retourne le tableau complet
- [ ] Endpoint sécurisé rejette les requêtes sans token
- [ ] Endpoint sécurisé accepte `student/secret123`
- [ ] Code organisé en packages (api, impl, model, security, client)

---

Aller plus loin (Étape 12)

 1. Intégration Spring Boot

```xml
<dependency>
    <groupId>org.apache.cxf</groupId>
    <artifactId>cxf-spring-boot-starter-jaxws</artifactId>
</dependency>
```

 2. Passer à PasswordDigest

Modifier `ServerPasswordCallback.java` :
```java
inProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_DIGEST);
```

 3. Signature et chiffrement

```java
inProps.put(WSHandlerConstants.ACTION, 
    WSHandlerConstants.USERNAME_TOKEN + " " + 
    WSHandlerConstants.SIGNATURE + " " + 
    WSHandlerConstants.ENCRYPT);
```

 4. HTTPS avec Jetty

```java
Server server = new Server(8443);
SslContextFactory sslContextFactory = new SslContextFactory();
sslContextFactory.setKeyStorePath("keystore.jks");
sslContextFactory.setKeyStorePassword("password");
// ...
```
 5. Tests d'intégration

```java
@Test
public void testSecuredEndpoint() {
    HelloService proxy = createSecuredProxy();
    String result = proxy.sayHello("Test");
    assertEquals("Bonjour, Test !", result);
}
```

### 6. Approche contract-first

1. Créer un WSDL maître
2. Générer les classes avec `wsdl2java`
3. Implémenter l'interface générée

## Support

Pour toute question ou problème :
1. Consulter la section Dépannage
2. Vérifier les logs du serveur
3. Utiliser l'onglet "Raw" de SoapUI pour voir le XML exact

## Licence
```

