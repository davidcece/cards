package com.cece.cards;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class HomeController {

    @GetMapping(value = "/")
    public RedirectView main() {
        return new RedirectView("/swagger-ui/index.html");
    }
}