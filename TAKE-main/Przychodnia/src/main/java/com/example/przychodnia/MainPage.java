package com.example.przychodnia;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainPage {

    @GetMapping("/")
    @ResponseBody
    public String welcomeText() {
        return "<h1>Witamy w systemie przychodni!</h1><p>API dostępne pod ścieżkami /pacjenci, /lekarze, /wizyty itd.</p>";
    }
}
