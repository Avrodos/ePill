# DB tables
- DRUG: Medikamente
- DRUG_PHARMACEUTICAL_FORM, PHARMACEUTICAL_FORM, INTAKE_INFORMATION:
  Default Einnahmemenge: Vorgabe ist medikamentenspezifisch aber unabhängig von Größe, Gewicht, Alter, Geschlecht
- DRUG_ADVERSE_EFFECT, ADVERSE_EFFECT: Nebenwirkung
  Wird voraussichtlich nicht benötigt, Beispiele?
- INTERACTION, INTERACTION_DRUG, DRUG_INTERACTION: Wechselwirkungen zwischen Medikamenten?
  ist das nicht falsch abgebildet?
  das scheint gefaked zu sein
  INTERACTION_DRUG: sollte 3 Felder haben
  IDINTERACTION, IDDRUG1, IDDRUG2
- DRUG_FEATURE: Medikamenten-Eigenschaften: z.B. vegan, ohne Tierversuche, ohne Alkohol, halal + MIN/MAX Alter
- DRUG_FEATURE_GENDER: geschlechtsspezifische Medikamenten-Eigenschaften, z.B. bei Schwangerschaft nicht einnehmen

- DISEASE: Krankheit, z.B. Schnupfen, Grippe... -> hier fehlt: Bluthochdruck oder ...

- DRUG_ACTIVE_SUBSTANCE, ACTIVE_SUBSTANCE, SUBSTANCE_GROUP: Wirkstoffe

- PACKAGING_SECTION, PACKAGING_TOPIC, TAILORED_TEXT (Beipackzettel?)

- GENDER: Geschlecht
- LANGUAGE: Sprache

User spezifisch:
- USER
- USER_SIMPLE: Anwenderdaten / Patientendaten -> hier könnte man das Gewicht ergänzen
  unklar: warum nicht in USER-Tabelle?
- USER_QUERY: anwenderspezifische Anfragen -> 
- USER_DRUG_TAKING:    Einnahme Medikamentenliste 
- USER_DRUG_REMEMBER:  Merkliste Medikamente


Unklar:
- ITEM_INVOCATION


TODO: Erweiterung Datenstruktur / DB
X Medikament = SimpleDrug erweitern um int halfTimePeriod
  (Annahme: jedes Medikament hat nur eine wesentliche aktive Substanz) = erledigt
-   
  
- Medikament erweitern um Liste von Texten / Strings takingInformation -> 1:n Beziehung
  prüfen, ob das auf der DB bereits vorhanden ist
- Medikament erweitern um default Dosis pro Tag -> defaultDosePerDay
  Klären: muss das pro Wirkstoff aufgeschlüsselt werden?
- Interaktionen als eigene Tabelle: neu modellieren
  Tabelle / Klasse INTERACTION kann bleiben,  
  -> INTERACTION_DRUG und DRUG_INTERACTION scheint falsch modelliert zu sein: Löschen?
  -> ersetzen durch einen Klasse / Tabelle DRUG_INTERACTION mit Feldern IDDRUG1, IDDRUG2, IDINTERACTION
- Medikament erweitern um Schwangerschaft, AlkoholVerbot, Fahrtuechtigkeit, sollte aus Tabelle drug_feature entfernt werden
- 


INSERT INTO drug_feature (id, drug_feature, min_age, max_age) VALUES (1,'ohne Tierversuche', 0, 0);
INSERT INTO drug_feature (id, drug_feature) VALUES (2,'halal');
INSERT INTO drug_feature (id, drug_feature, min_age, max_age) VALUES (3,'beeinträchtigt die Fahrtüchtigkeit', 16, 0);
INSERT INTO drug_feature (id, drug_feature) VALUES (4,'verschreibungspflichtig');
INSERT INTO drug_feature (id, drug_feature) VALUES (5,'ohne Alkohol');
INSERT INTO drug_feature (id, drug_feature) VALUES (6,'homöopathisch');
INSERT INTO drug_feature (id, drug_feature) VALUES (7,'glutenfrei');
INSERT INTO drug_feature (id, drug_feature) VALUES (8,'Für Schwangere ungeeignet.');
INSERT INTO drug_feature (id, drug_feature) VALUES (9,'vegan');
INSERT INTO drug_feature (id, drug_feature) VALUES (10,'lactosefrei');


HOWTO:
- Bedingte Anzeigen / Rendern: https://reactjs.org/docs/conditional-rendering.html
  {Bedingung && <Html....>}