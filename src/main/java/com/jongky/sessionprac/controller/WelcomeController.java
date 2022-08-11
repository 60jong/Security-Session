package com.jongky.sessionprac.controller;

import com.jongky.sessionprac.model.User;
import com.jongky.sessionprac.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class WelcomeController {
    private final SessionRegistry sessionRegistry;
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public WelcomeController(SessionRegistry sessionRegistry, UserDao userDao, PasswordEncoder passwordEncoder) {
        this.sessionRegistry = sessionRegistry;
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "index.html";
    }

    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public String loginPage() {
        return "login-page.html";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String joinPage() {
        return "join-page.html";
    }

    @RequestMapping(value = "/join", method = RequestMethod.POST)
    public String join(User user) {
        System.out.println(user);
        user.setRole("ROLE_USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (userDao.join(user) == 1)
            return "redirect:/signin";
        else
            return "redirect:/signup";
    }

    @RequestMapping(value = "/admin/sessions", method = RequestMethod.GET)
    @ResponseBody
    public List<SessionInformation> getSessions() {
        List principals = sessionRegistry.getAllPrincipals();

        if (principals != null) {
            List<SessionInformation> sessionInformations = new ArrayList<>();
            for (Object principal : principals) {
                sessionInformations.addAll(sessionRegistry.getAllSessions(principal, false));
            }
            return sessionInformations;
        }

        return Collections.EMPTY_LIST;
    }
}
