@echo off
REM Supprimer l'ancien JAR JAXB problématique
if exist target\lib\jaxb-runtime-3.0.0-M5.jar (
    del /F target\lib\jaxb-runtime-3.0.0-M5.jar
)

REM Démarrer le serveur SOAP
java -cp "target\classes;target\lib\*" com.soap.cxf.Server
