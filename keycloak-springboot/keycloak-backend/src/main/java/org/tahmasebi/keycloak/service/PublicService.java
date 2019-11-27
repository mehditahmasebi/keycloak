package org.tahmasebi.keycloak.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/public")
public class PublicService {

    @GetMapping("login/{username}/{password}")
    public ResponseEntity<Map<String, String>> login(@PathVariable String username, @PathVariable String password , HttpServletRequest request)
    {
        System.out.println("Login : "+username+":" +password);
        ResponseEntity<Map<String, String>> finalResult = new ResponseEntity<>(null, HttpStatus.OK);
        return finalResult;
    }

    @GetMapping
    public ResponseEntity<String> helloPublic(HttpServletRequest request)
    {
        ResponseEntity<String> finalResult = new ResponseEntity<String>("Hello World!", HttpStatus.OK);
        return finalResult;
    }

}
