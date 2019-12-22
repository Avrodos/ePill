package com.doccuty.epill.intolerance;


import com.doccuty.epill.model.util.IntoleranceCreator;
import com.doccuty.epill.user.User;
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
@RequestMapping("/intolerance")
public class IntoleranceController {

    @Autowired
    private IntoleranceService service;

    @Autowired
    private UserService userService;

    @RequestMapping("/all")
    public ResponseEntity<JsonObject> getAllIntolerances() {

        HashSet<Intolerance> set = service.getAllIntolerances();

        IdMap map = IntoleranceCreator.createIdMap("");
        map.withFilter(Filter.regard(Deep.create(2)));

        JsonObject json = new JsonObject();
        JsonArray intoleranceArray = new JsonArray();

        for (Intolerance intolerance : set) {
            intoleranceArray.add(map.toJsonObject(intolerance));
        }

        json.add("value", intoleranceArray);


        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @RequestMapping(value = {"/{id}"}, method = RequestMethod.GET)
    public ResponseEntity<JsonObject> getIntoleranceById(@PathVariable(value = "id") int id) {

        Intolerance intolerance = service.getIntoleranceById(id);

        IdMap map = IntoleranceCreator.createIdMap("");
        map.withFilter(Filter.regard(Deep.create(2)));

        JsonObject json = new JsonObject();
        json.add("value", map.toJsonObject(intolerance));

        return new ResponseEntity<>(json, HttpStatus.OK);
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<Object> addIntolerance(@RequestBody String name) {
        // A pragmatic approach to security which does not use much framework-specific magic. While other approaches
        // with annotations, etc. are possible they are much more complex while this is quite easy to understand and
        // extend.
        if (userService.isAnonymous()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        //the name always ends on "=", which is not desired.
        if (name != null && name.length() > 0 && name.charAt(name.length() - 1) == '=') {
            name = name.substring(0, name.length() - 1);
        }

        Intolerance repoInt = service.getIntoleranceByName(name);
        User user = userService.getUserById(userService.getCurrentUser().getId());

        if (repoInt == null) {
            repoInt = new Intolerance();
            repoInt.setName(name);
            service.addIntolerance(repoInt);
        }

        user.withIntolerance(repoInt);
        userService.updateUserData(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}