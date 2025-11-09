@echo off
REM Tester le client SOAP simple
java -cp "target\classes;target\lib\*" com.soap.cxf.client.SimpleClient
pause
