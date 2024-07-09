package com.example.user_login.Controllers;

import org.springframework.web.bind.annotation.GetMapping;

public class OAuto2Controller {

    @GetMapping("/oauth2/authorization/google")
    public String googleLogin() {
        return "redirect:/oauth2/authorization/google";
    }

    @GetMapping("/oauth2/authorization/github")
    public String githubLogin() {
        return "redirect:/oauth2/authorization/github";
    }


}
