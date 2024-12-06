package controlate_back.api.services;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.springframework.stereotype.Service;

@Service
public class ApiTranslateService {
    private static final String MYMEMORY_API_URL = "https://api.mymemory.translated.net/get";

    public String translate(String text, String targetLang, String sourceLang) {
        System.out.println(text);
        System.out.println(targetLang);
        System.out.println(sourceLang);
        System.out.println(sourceLang + "|" + targetLang);
        HttpResponse<JsonNode> response = Unirest.get(MYMEMORY_API_URL)
                .queryString("q", text)
                .queryString("langpair", sourceLang + "|" + targetLang)
                .queryString("de", "oscarvelascoclase@gmail.com")
                .asJson();
        System.out.println(response.getBody());
        if (response.getStatus() == 200) { // Asegúrate de verificar el código de estado
            return response.getBody()
                    .getObject()
                    .getJSONObject("responseData")
                    .getString("translatedText");
        }

        throw new RuntimeException("Error en la API de traducción: " + response.getStatusText());
    }
}
