package org.sysc.ama.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

@RestController
public class AMAController {

    private Integer amaId = 0;

    @PostMapping("/ama/create")
    public HashMap<String, String> create (
            @RequestParam("userId") Integer userId,
            @RequestParam("name") String name,
            @RequestParam("visibility") Integer visibility
        ) {
        HashMap<String, String> response = new HashMap<String, String>();

        // TODO Create AMA model
        // TODO Check that AMA model is unique
        // TODO Insert AMA model into database
        // TODO Return created AMA

        this.amaId = this.amaId + 1;

        response.put("name", name);
        response.put("id", this.amaId.toString());
        response.put("userId", userId.toString());
        response.put("visibility", visibility.toString());

        return response;
    }


}

