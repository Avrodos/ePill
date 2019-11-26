package com.doccuty.epill.condition;


import com.doccuty.epill.model.util.ConditionCreator;
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
public class ConditionController {

    @Autowired
    private ConditionService service;

    @Autowired
    private UserService userService;

    @RequestMapping("/condition/all/")
    public ResponseEntity<JsonObject> getAllAllergies() {

        HashSet<Condition> set = service.getAllAllergies();

        IdMap map = ConditionCreator.createIdMap("");
        map.withFilter(Filter.regard(Deep.create(2)));

        JsonObject json = new JsonObject();
        JsonArray conditionArray = new JsonArray();

        for (Condition condition : set) {
            conditionArray.add(map.toJsonObject(condition));
        }

        json.add("value", conditionArray);


        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @RequestMapping(value = {"/condition/{id}/"}, method = RequestMethod.GET)
    public ResponseEntity<JsonObject> getConditionById(@PathVariable(value = "id") int id) {

        Condition condition = service.getConditionById(id);

        IdMap map = ConditionCreator.createIdMap("");
        map.withFilter(Filter.regard(Deep.create(2)));

        JsonObject json = new JsonObject();
        json.add("value", map.toJsonObject(condition));

        return new ResponseEntity<>(json, HttpStatus.OK);
    }


    @RequestMapping(value = "/condition/save/")
    public ResponseEntity<Object> addCondition(@RequestParam("name") String name) {
        // A pragmatic approach to security which does not use much framework-specific magic. While other approaches
        // with annotations, etc. are possible they are much more complex while this is quite easy to understand and
        // extend.
        if (userService.isAnonymous()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Condition condition = new Condition();
        condition.setName(name);

        service.addCondition(condition);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}