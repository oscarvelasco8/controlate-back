package controlate_back.api.controllers;

import controlate_back.api.services.ApiTranslateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/translate")
public class ApiTranslateController {
    @Autowired
    private ApiTranslateService translationService;

    @Operation(
            summary = "Traducir texto",
            description = "Devuelve un texto traducido.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Texto traducido en formato plano",
                            content = @Content(mediaType = "text/plain")
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "No Autorizado",
                            content = @Content(mediaType = "text/plain")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @Content(mediaType = "text/plain")
                    )
            },
            security = {
                    @io.swagger.v3.oas.annotations.security.SecurityRequirement(
                            name = "BearerAuth",
                            scopes = {"read"}
                    )
            },
            parameters = {
                    @Parameter(name = "text", description = "Texto a traducir", required = true),
                    @Parameter(name = "targetLanguage", description = "Idioma de destino (código ISO-639-1, por ejemplo, 'en')", required = false, example = "en"),
                    @Parameter(name = "sourceLanguage", description = "Idioma de origen (código ISO-639-1, por ejemplo, 'es')", required = false, example = "es")
            },
            tags = {"Traductor de palabras"}
    )

    @GetMapping
    public String translate(
            @RequestParam String text,
            @RequestParam(defaultValue = "en") String targetLanguage,
            @RequestParam(defaultValue = "es") String sourceLanguage
    ) {
        return translationService.translate(text, targetLanguage, sourceLanguage);
    }
}
