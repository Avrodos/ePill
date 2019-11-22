package com.doccuty.epill.diabetes;

import com.doccuty.epill.model.util.DiabetesCreator;
import com.doccuty.epill.user.UserService;
import de.uniks.networkparser.Deep;
import de.uniks.networkparser.Filter;
import de.uniks.networkparser.IdMap;
import de.uniks.networkparser.json.JsonArray;
import de.uniks.networkparser.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/diabetes")
public class DiabetesController {

    @Autowired
    private UserService userService;

    @Autowired
    private DiabetesService service;

    /**
     * get user profiles of all users
     */

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<JsonObject> getAllDiabetes() {
        // A pragmatic approach to security which does not use much framework-specific magic. While other approaches
        // with annotations, etc. are possible they are much more complex while this is quite easy to understand and
        // extend.
        if (userService.isAnonymous()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        IdMap map = DiabetesCreator.createIdMap("");
        map.withFilter(Filter.regard(Deep.create(2)));

        List<Diabetes> set = service.getAllDiabetes();

        JsonObject json = new JsonObject();
        JsonArray diabetesArray = new JsonArray();

        for (Diabetes diabetes : set) {
            diabetesArray.add(map.toJsonObject(diabetes));
        }

        json.add("value", diabetesArray);

        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    /**
     * get a complete user profile by id
     */

    @RequestMapping(value = {"/{id}"}, method = RequestMethod.GET)
    public ResponseEntity<JsonObject> getUserById(@PathVariable(value = "id") int id) {
        // A pragmatic approach to security which does not use much framework-specific magic. While other approaches
        // with annotations, etc. are possible they are much more complex while this is quite easy to understand and
        // extend.
        if (userService.isAnonymous()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        IdMap map = DiabetesCreator.createIdMap("");
        map.withFilter(Filter.regard(Deep.create(5)));

        Diabetes diabetes = service.getDiabetesById(id);

        JsonObject json = new JsonObject();
        json.add("value", map.toJsonObject(diabetes));

        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    /**
     * save or update a complete user profile
     *
     * @param user
     * @return
     */

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<Object> saveUser(@RequestBody Diabetes diabetes) {

        service.saveDiabetes(diabetes);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * update all parameters from settings section including
     *
     * @param user
     * @return
     */

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<JsonObject> updateUserSettings(@RequestBody Diabetes diabetes) {
        // A pragmatic approach to security which does not use much framework-specific magic. While other approaches
        // with annotations, etc. are possible they are much more complex while this is quite easy to understand and
        // extend.
        if (userService.isAnonymous()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        diabetes = service.saveDiabetes(diabetes);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
