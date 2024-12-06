package controlate_back.api.controllers;

import controlate_back.api.services.ApiTranslateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/translate")
public class ApiTranslateController {
    @Autowired
    private ApiTranslateService translationService;

    @GetMapping
    public String translate(@RequestParam String text,
                            @RequestParam(defaultValue = "en") String targetLanguage,
                            @RequestParam(defaultValue = "es") String sourceLanguage) {
        return translationService.translate(text, targetLanguage, sourceLanguage);
    }
}
