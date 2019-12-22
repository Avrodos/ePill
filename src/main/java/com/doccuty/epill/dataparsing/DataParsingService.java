package com.doccuty.epill.dataparsing;

import com.doccuty.epill.allergy.Allergy;
import com.doccuty.epill.allergy.AllergyService;
import com.doccuty.epill.condition.Condition;
import com.doccuty.epill.condition.ConditionService;
import com.doccuty.epill.diabetes.Diabetes;
import com.doccuty.epill.diabetes.DiabetesService;
import com.doccuty.epill.gender.Gender;
import com.doccuty.epill.gender.GenderService;
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
    private DiabetesService diabetesService;

    @Autowired
    private GenderService genderService;

    @Autowired
    private AllergyService allergyService;

    @Autowired
    private IntoleranceService intoleranceService;

    @Autowired
    private ConditionService conditionService;

    public User importCA7UserData(User user) {

        //As the path for the Connector must be hardcoded, we can hardcode it here too.
        if (user.getA7id().isEmpty() || user.getA7id().equals("")) {
            return null;
        }
        //User newData = getCurrentUser();
        User oldData = service.getUserById(getCurrentUser().getId());
        //By creating a temporary User, we can make sure, that we are only taking the newest Data by taking single field information only once, from newest files to oldest.
        //As updates in the profile are reflected by new files, i.e., older files contain outdated information.
        User newData = new User();
        String desiredFilePrefix = user.getLastname() + "_" + user.getFirstname();

        //The connector setup forces an absolute path for configuration
        //Therefore, might as well use a hardcoded file path here
        //Adjust as needed.
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
                    String key = record.get("key");
                    if (key.equals("routing.source")) {
                        if (!record.get("id").equals(user.getA7id())) {
                            break; //should force next file
                        }
                        continue;
                    }
                    if (!record.get("invalidation_date").equals("")) {
                        //information is invalidated
                        continue;
                    }
                    else if (key.equals("ami.firstName")) {
                        if (newData.getFirstname() != null || (oldData.getFirstname() != null && oldData.getOverwriteOnImport() != null && !oldData.getOverwriteOnImport())) {
                            continue;
                        }
                        String newFirstName = record.get("value");
                        newData.setFirstname(newFirstName);
                    } else if (key.equals("ami.lastName")) {
                        if (newData.getLastname() != null || (oldData.getLastname() != null && oldData.getOverwriteOnImport() != null && !oldData.getOverwriteOnImport())) {
                            continue;
                        }
                        String newLastName = record.get("value");
                        newData.setLastname(newLastName);
                    } else if (key.equals("ami.sex")) {
                        if (newData.getGender() != null || ((oldData.getGender() != null && oldData.getGender().getId() != 0) && oldData.getOverwriteOnImport() != null && !oldData.getOverwriteOnImport())) {
                            continue; //next record
                        }
                        Gender newGender = processGender(record.get("value"));
                        newData.setGender(newGender);
                    } else if (key.equals("ami.smokerFrequency")) {
                        if (newData.getSmoker() != null || (oldData.getSmoker() != null && oldData.getOverwriteOnImport() != null && !oldData.getOverwriteOnImport())) {
                            continue;
                        }
                        boolean newSmoker = processSmoker(record.get("value"));
                        newData.setSmoker(newSmoker);
                    } else if (key.equals("ami.diabete")) {
                        if (newData.getDiabetes() != null || ((oldData.getDiabetes() != null && oldData.getDiabetes().getId() != 0) && oldData.getOverwriteOnImport() != null && !oldData.getOverwriteOnImport())) {
                            continue;
                        }
                        Diabetes newDiabetes = processDiabetes(record.get("value"), records, record.get("multi_id"));
                        if (newDiabetes == null) {
                            continue;
                        }
                        newData.setDiabetes(newDiabetes);
                    } else if (key.equals("ami.birthDate")) {
                        if (newData.getDateOfBirth() != null || (oldData.getDateOfBirth() != null && oldData.getOverwriteOnImport() != null && !oldData.getOverwriteOnImport())) {
                            continue;
                        }
                        Date newDateOfBirth = processDateOfBirth(record.get("value"));
                        newData.setDateOfBirth(newDateOfBirth);
                    } else if (key.equals("ami.condition")) {
                        //some conditions are in the past. We do not need them.
                        if (hasEndDate(records, record.get("multi_id"))) {
                            continue;
                        }
                        Condition newCondition = processCondition(record.get("value"));
                        newData.withCondition(newCondition);
                    } else if (key.equals("ami.allergyAndIntolerance")) {
                        //Append new information
                        String multiId = record.get("multi_id");
                        //Look for record with id = multiId and check record.get(key).equals(qualifier.type) -> this has li.intolerance or li.allergy
                        String qualifierType = getQualifierType(records, multiId);
                        if (qualifierType.equals("li.intolerance")) {
                            Intolerance newIntolerance = processIntolerance(record.get("value"));
                            newData.withIntolerance(newIntolerance);

                        } else if (qualifierType.equals("li.allergy")) {
                            Allergy newAllergy = processAllergy(record.get("value"));
                            newData.withAllergy(newAllergy);
                        } else {
                            continue;
                        }

                    } else {
                        continue;
                    }
                    //Might be interesting: UUID id = UUID.fromString( record.get( "id" ) );
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            } catch (ParseException e) {
                e.printStackTrace();
                break;
            }
        }
        if (files.size() > 0) {
            User result = service.updateUserData(newData);
            return result;
        }
        return null;
    }

    private Allergy processAllergy(String name) {
        if (!allergyService.allergyExistsByName(name)) {
            Allergy newAllergy = new Allergy();
            newAllergy.setName(name);
            allergyService.addAllergy(newAllergy);
            return newAllergy;
        }
        return null;
    }

    private Intolerance processIntolerance(String name) {
        if (!intoleranceService.intoleranceExistsByName(name)) {
            Intolerance newIntolerance = new Intolerance();
            newIntolerance.setName(name);
            intoleranceService.addIntolerance(newIntolerance);
            return newIntolerance;
        }
        return null;
    }

    private Condition processCondition(String name) {
        if (!conditionService.conditionExistsByName(name)) {
            Condition newCondition = new Condition();
            newCondition.setName(name);
            conditionService.addCondition(newCondition);
            return newCondition;
        }
        return null;
    }

    private boolean hasEndDate(Iterable<CSVRecord> records, String multiId) {
        //since we do not receive a guarantee for its position, we will have to look through it all
        for (CSVRecord record : records) {
            if (record.get("parent_id").equals(multiId) && record.get("key").equals("qualifier.endDate")) {
                //we found our qualifier
                return true;
            }
        }
        return false;
    }

    private String getQualifierType(Iterable<CSVRecord> records, String multiId) {
        //since we do not receive a guarantee for its position, we will have to look through it all
        for (CSVRecord record : records) {
            if (record.get("parent_id").equals(multiId) && record.get("key").equals("qualifier.type")) {
                //we found our qualifier
                return record.get("value");
            }
        }
        return "";
    }

    private Diabetes processDiabetes(String value, ArrayList<CSVRecord> records, String multiId) {
        //type 1 = id(1), type 2 = id(2), none = id(3)
        if (value.equals("li.yes")) {
            String qualifierType = getQualifierType(records, multiId);
            if (qualifierType.equals("li.type1")) {
                return diabetesService.getDiabetesById(1);
            } else if (qualifierType.equals("li.type2")) {
                return diabetesService.getDiabetesById(2);
            } else {
                return null;
            }
        } else {
            return diabetesService.getDiabetesById(3);
        }
    }

    private boolean processSmoker(String value) {
        return value.equals("li.smoker");
        //there is some further differentiation in A7, which we don't care for.
    }

    private Gender processGender(String value) {
        Gender currentGender = new Gender();
        // 0 = no info, 1 = male, 2 = female
        switch (value) {
            case "li.man":
                return genderService.getGenderById(1);
            case "li.woman":
                return genderService.getGenderById(2);
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
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
