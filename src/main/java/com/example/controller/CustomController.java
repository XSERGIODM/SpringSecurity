package com.example.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@AllArgsConstructor
@RestController
@RequestMapping("v1")
public class CustomController {

    private final SessionRegistry sessionRegistry;

    @GetMapping("/index")
    public String index() {
        return "Hello words";
    }

    @GetMapping("/index2")
    public String index2() {
        return "Hello words not SECURED!";
    }

    @GetMapping("/session")
    public ResponseEntity<?> getDetailsSession() {

        String sessionId = "";
        User userObject = null;

        List<Object> sessions = sessionRegistry.getAllPrincipals();

        for (Object session : sessions) {
            if (session instanceof User) {
                userObject = (User) session;
            }
            List<SessionInformation> sessionInformations = sessionRegistry.getAllSessions(session, false);

            for (SessionInformation sessionInformation : sessionInformations) {
                sessionId = sessionInformation.getSessionId();
            }
        }

        Map<String, Object> respone = new HashMap<>();
        respone.put("response", "hello world");
        respone.put("sessionId", sessionId);
        respone.put("sessionuser", userObject);

        return ResponseEntity.ok(respone);
    }

}
