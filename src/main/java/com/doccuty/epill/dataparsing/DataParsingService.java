package com.doccuty.epill.dataparsing;

import com.doccuty.epill.allergy.Allergy;
import com.doccuty.epill.diabetes.Diabetes;
import com.doccuty.epill.diabetes.DiabetesRepository;
import com.doccuty.epill.gender.Gender;
import com.doccuty.epill.gender.GenderRepository;
import com.doccuty.epill.intolerance.Intolerance;
import com.doccuty.epill.intolerance.IntoleranceService;
import com.doccuty.epill.user.User;
import com.doccuty.epill.user.UserService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DataParsingService {

    @Autowired
    private UserService service;

    @Autowired
    private DiabetesRepository diabetesRepository;

    @Autowired
    private GenderRepository genderRepository;

    //the ami keys for data, that can contain more than one entry
    //TODO: do something with "ami.medicationStatement" or "amiref.medicationStatement". They did not update ami.medicationStatement yet...
    //TODO: Parse Names

    public User importCA7UserData(User user) {
        //TODO: Filepath shouldnt be hardcoded in this class.
        //As the path for the Connector must be hardcoded, we can hardcode it here too.
        //TODO: Wieso ist a7id null, username allerdings nicht? (sondern hat die id) -> temp workaround, den sollte ich wohl fixen
        if (user.getA7id().isEmpty() || user.getA7id().equals("")) {
            //TODO: ERROR HANDLING

        }
        //User newData = getCurrentUser();
        User newData = new User();
        String desiredFilePrefix = user.getLastname() + "_" + user.getFirstname();

        List<Path> files = new ArrayList<>();
        File folder = new File("/home/fuji/Documents/Connector/Documents/andaman7/inbox");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                if (listOfFiles[i].getName().startsWith(desiredFilePrefix)) {
                    files.add(Paths.get(listOfFiles[i].getPath()));
                }
            }
        }
        Collections.sort(files, Collections.reverseOrder()); //we want to look at the newest first.
        //TODO: Does it actually sort newest to oldest?
        for (int i = 0; i < files.size(); i++) {
            try {
                // Read CSV file. For each row, instantiate and collect `DailyProduct`.
                BufferedReader reader = Files.newBufferedReader(files.get(i));
                Iterable<CSVRecord> tempRecords = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(reader);
                ArrayList<CSVRecord> records = new ArrayList<>();
                for (CSVRecord record : tempRecords) {
                    records.add(record);
                }
                for (int j = 0; j < records.size(); j++) {
                    CSVRecord record = records.get(j);
                    //ASSUMPTION: parent_id is always the id of the EHR
                    String key = record.get("key");
                    if (key.equals("routing.source")) {
                        if (!record.get("id").equals(user.getA7id())) {
                            break; //should force next file
                        }
                        continue;
                    }
                    if (!record.get("invalidation_date").equals("")) {
                        //its invalidated
                        continue;
                    }
                    //a little bit ugly, but has to be done, to assure some data is not overwritten by older data.
                    else if (key.equals("ami.firstName")) {
                        if (newData.getFirstname() != null) {
                            continue;
                        }
                        String newFirstName = record.get("value");
                        newData.setFirstname(newFirstName);
                    } else if (key.equals("ami.lastName")) {
                        if (newData.getLastname() != null) {
                            continue;
                        }
                        String newLastName = record.get("value");
                        newData.setLastname(newLastName);
                    } else if (key.equals("ami.sex")) {
                        if (newData.getGender() != null) {
                            continue; //next record
                        }
                        Gender newGender = processGender(record.get("value"));
                        // Or something like this? user.setGender(genderRepository.findOne(usr.getGender().getId()));
                        newData.setGender(newGender);
                    } else if (key.equals("ami.smokerFrequency")) {
                        if (newData.getSmoker() != null) {
                            continue;
                        }
                        boolean newSmoker = processSmoker(record.get("value"));
                        newData.setSmoker(newSmoker);
                    } else if (key.equals("ami.diabete")) {
                        if (newData.getDiabetes() != null) {
                            continue;
                        }
                        Diabetes newDiabetes = processDiabetes(record.get("value"), records, record.get("multi_id"));
                        newData.setDiabetes(newDiabetes);
                    } else if (key.equals("ami.birthDate")) {
                        if (newData.getDateOfBirth() != null) {
                            continue;
                        }
                        Date newDateOfBirth = processDateOfBirth(record.get("value"));
                        newData.setDateOfBirth(newDateOfBirth);
                    } else if (Arrays.stream(listAMI).anyMatch(key::equals)) {
                        //TODO: Noch überprüfen ob die Daten nicht invalidiert wurden
                        //Append new information
                        if (!record.get("invalidation_date").equals("")) {
                            continue;
                        }
                        //TODO: Erstmal Attribute für conditions und Intolerances anlegen.
                        //Mache ich 2 separate Attribute? Dann müsste ich auch die multi_ids lesen und darüber emitteln was von beidem es ist.
                        //TODO: Ab hier jetzt die Daten verarbeiten
                        String multiId = record.get("multi_id");
                        //Look for record with id = multiId and check record.get(key).equals(qualifier.type) -> this has li.intolerance or li.allergy
                        String qualifierType = getQualifierType(records, multiId);
                        if (qualifierType.equals("li.intolerance")) {
                            Intolerance newIntolerance = processIntolerance(record.get("value"));
                            newData.withIntolerance(newIntolerance);

                        } else if (qualifierType.equals("li.allergy")) {
                            Allergy newAllergy = new Allergy();
                            newAllergy.setName(record.get("value"));
                            //TODO: wahrscheinlich muss ich noch UUIDs für jede vergeben?
                            // newData.withAllergy(newAllergy);
                        } else {
                            //TODO: Fehlerbehandlung
                        }

                    } else {
                        //TODO: Fehlerbehandlung
                    }
                    //Might be interesting: UUID id = UUID.fromString( record.get( "id" ) );
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                //TODO: proper error handling.
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //TODO: make sure I dont overwrite/lose data when sending back the user
        //Create another user with the user data from the parameter and overwrite with new data from "newData"
        //TODO: Update the current user with newData (there is a method for that)

        //return service.updateUserData(newData);

        User returnUser = service.updateUserData(newData);
        //long id = newData.getId();
        //service.deleteUser(id);
        return returnUser;

    }

    private String getQualifierType(Iterable<CSVRecord> records, String multiId) {
        //let us assume, that the qualifier always comes after the parent
        for (CSVRecord record : records) {
            if (record.get("parent_id").equals(multiId) && record.get("key").equals("qualifier.type")) {
                //we found our qualifier
                return record.get("value");
            }
        }
        return "";
    }

    private Diabetes processDiabetes(String value, ArrayList<CSVRecord> records, String multiId) {
        Diabetes currentDiabetes = new Diabetes();
        if (value.equals("li.yes")) {
            String qualifierType = getQualifierType(records, multiId);
            if (qualifierType.equals("li.type1")) {
                return diabetesRepository.findOne(1);
                //currentDiabetes.setDiabetes("typ1");
                //currentDiabetes.setId(1);
            } else if (qualifierType.equals("li.type2")) {
                currentDiabetes.setDiabetes("type2");
                currentDiabetes.setId(2);
            } else {
                //TODO: Fehlerbehandlung
            }
        } else {
            currentDiabetes.setDiabetes("none"); //TODO: vermutlich irgendwo dokumentieren
            currentDiabetes.setId(3); //muss alles beantwortet werden
        }
        return currentDiabetes;
    }

    private boolean processSmoker(String value) {
        if (value.equals("li.smoker")) {
            return true;
        }
        //there is some further differentiation in A7, which we don't care for.
        return false;
    }

    private Gender processGender(String value) {
        Gender currentGender = new Gender();
        ; // 0 = no info, 1 = male, 2 = female
        //TODO: Does this work with .equals?
        switch (value) {
            case "li.man":
                return genderRepository.findOne(1);
            //currentGender.setGender("male");
            //currentGender.setId(1);
            //break;
            case "li.woman":
                currentGender.setGender("female");
                currentGender.setId(2);
                break;
            default:
                currentGender.setId(0);
        }
        return currentGender;
    }

    private Date processDateOfBirth(String value) throws ParseException {
        SimpleDateFormat ISO8601DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.GERMANY);
        //No definition for epills used date format, therefore I will stick to the displayed yyyy-mm-dd format
        return ISO8601DATEFORMAT.parse(value);
    }

    /**
     * Check if the current user is not authenticated.
     *
     * @return true if the user is not authenticated.
     */
    public boolean isAnonymous() {
        return getCurrentUser().getId() == -1L;
    }

    /**
     * Retrieve the currently active user or null, if no user is logged in.
     *
     * @return the current user.
     */
    public User getCurrentUser() {
        //TODO: unstatic machen nach dem testen
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
