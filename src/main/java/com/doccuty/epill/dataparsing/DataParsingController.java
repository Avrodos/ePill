package com.doccuty.epill.dataparsing;


import com.doccuty.epill.user.User;
import de.uniks.networkparser.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
public class DataParsingController {

    @Autowired
    DataParsingService dataParsingService;

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public ResponseEntity<JsonObject> importData(@RequestBody User user) {
        if (dataParsingService.isAnonymous()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        dataParsingService.importCA7UserData(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
