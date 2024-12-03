package controlate_back.api.controllers;

import controlate_back.api.services.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/translate")
public class ApiController {
    @Autowired
    private ApiService translationService;

    @PostMapping
    public String translate(@RequestParam String text,
                            @RequestParam(defaultValue = "en") String targetLanguage,
                            @RequestParam(defaultValue = "es") String sourceLanguage) {
        return translationService.translate(text, targetLanguage, sourceLanguage);
    }
}
