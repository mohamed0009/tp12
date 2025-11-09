@echo off
REM Tester le client SOAP sécurisé
java -cp "target\classes;target\lib\*" com.soap.cxf.client.SecureClientDemo
pause
