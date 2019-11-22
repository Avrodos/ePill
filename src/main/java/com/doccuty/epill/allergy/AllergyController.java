package com.doccuty.epill.allergy;


import com.doccuty.epill.model.util.AllergyCreator;
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

import java.util.HashSet;

/**
 * HTTP endpoint for a post-related HTTP requests.
 */
@RestController
public class AllergyController {

    @Autowired
    private AllergyService service;

    @Autowired
    private UserService userService;

    @RequestMapping("/allergy/all/")
    public ResponseEntity<JsonObject> getAllAllergies() {

        HashSet<Allergy> set = service.getAllAllergies();

        IdMap map = AllergyCreator.createIdMap("");
        map.withFilter(Filter.regard(Deep.create(2)));

        JsonObject json = new JsonObject();
        JsonArray allergyArray = new JsonArray();

        for (Allergy allergy : set) {
            allergyArray.add(map.toJsonObject(allergy));
        }

        json.add("value", allergyArray);


        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @RequestMapping(value = {"/allergy/{id}/"}, method = RequestMethod.GET)
    public ResponseEntity<JsonObject> getAllergyById(@PathVariable(value = "id") int id) {

        Allergy allergy = service.getAllergyById(id);

        IdMap map = AllergyCreator.createIdMap("");
        map.withFilter(Filter.regard(Deep.create(2)));

        JsonObject json = new JsonObject();
        json.add("value", map.toJsonObject(allergy));

        return new ResponseEntity<>(json, HttpStatus.OK);
    }


    @RequestMapping(value = "/allergy/save/")
    public ResponseEntity<Object> addAllergy(@RequestParam("name") String name) {
        // A pragmatic approach to security which does not use much framework-specific magic. While other approaches
        // with annotations, etc. are possible they are much more complex while this is quite easy to understand and
        // extend.
        if (userService.isAnonymous()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Allergy allergy = new Allergy();
        allergy.setName(name);

        service.addAllergy(allergy);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}