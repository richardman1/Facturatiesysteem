::
:: Batch bestand voor het starten van de IVH5 LibraryClient.
::
:: Voer dit batchbestand uit vanuit de IVH5\client directory, of pas de paden naar
:: de classes hieronder aan.
:: Zorg ervoor dat de registry en de LibraryServer gestart zijn.
::

:: java -cp .\bin;.\libraries\log4j-1.2.17.jar facturatie.client.Client localhost
java -cp .\bin;.\libraries\log4j-1.2.17.jar facturatie.client.Client 192.168.2.7

:: Wanneer je securityproblemen wilt oplossen, voeg dan onderstaande optie aan het command toe.
:: Hiermee krijg je inzicht in alle security instellingen.
::
:: 		-D java.security.debug=access,failure

@pause