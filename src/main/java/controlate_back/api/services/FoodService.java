package controlate_back.api.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class FoodService {
    @Value("${consumer.key}")
    private String consumerKey;
    @Value("${consumer.secret}")
    private String consumerSecret;
    //    private String baseUrl = "https://platform.fatsecret.com/rest/foods/search/v1";
    private String baseUrl = "https://platform.fatsecret.com/rest";
    private String searchByNameUrl = "/foods/search/v1";
    private String searchByIdUrl = "/food/v4";
    private long lastTimestamp = 0;

    private String generateNonce(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder nonce = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            nonce.append(chars.charAt(random.nextInt(chars.length())));
        }
        return nonce.toString();
    }

    private long getTimestamp() {
        long currentTimestamp = System.currentTimeMillis() / 1000;
        lastTimestamp = Math.max(lastTimestamp, currentTimestamp);
        return lastTimestamp;
    }

    private String createSignatureBaseString(String method, String url, Map<String, String> params) {
        String sortedParams = params.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> encode(e.getKey()) + "=" + encode(e.getValue()))
                .collect(Collectors.joining("&"));
        return method.toUpperCase() + "&" + encode(url) + "&" + encode(sortedParams);
    }

    private String calculateSignature(String baseString, String consumerSecret, String tokenSecret) throws Exception {
        String key = encode(consumerSecret) + "&" + (tokenSecret.isEmpty() ? "" : encode(tokenSecret));
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec secret = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), mac.getAlgorithm());
        mac.init(secret);
        byte[] hash = mac.doFinal(baseString.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    private Map<String, String> createOAuthParams() {
        Map<String, String> oauthParams = new TreeMap<>();
        oauthParams.put("oauth_consumer_key", consumerKey);
        oauthParams.put("oauth_signature_method", "HMAC-SHA1");
        oauthParams.put("oauth_timestamp", Long.toString(getTimestamp()));
        oauthParams.put("oauth_nonce", generateNonce(16));
        oauthParams.put("oauth_version", "1.0");
        return oauthParams;
    }

    private String generateAuthorizationHeader(Map<String, String> oauthParams, String signature) {
        oauthParams.put("oauth_signature", signature);
        return "OAuth " + oauthParams.entrySet().stream()
                .map(e -> encode(e.getKey()) + "=\"" + encode(e.getValue()) + "\"")
                .collect(Collectors.joining(","));
    }

    private String encode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8)
                    .replace("%7E", "~")
                    .replace("%20", "+");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<String> getProductsByName(String searchTerm, int pageNumber, int maxResults) throws Exception {
        Map<String, String> queryParams = Map.of(
                "search_expression", searchTerm,
                "format", "json",
                "page_number", Integer.toString(pageNumber),
                "max_results", Integer.toString(maxResults)
        );

        Map<String, String> oauthParams = createOAuthParams();
        String baseString = createSignatureBaseString("GET", baseUrl.concat(this.searchByNameUrl), new TreeMap<>(queryParams) {{
            putAll(oauthParams);
        }});
        String signature = calculateSignature(baseString, consumerSecret, "");

        String authorizationHeader = generateAuthorizationHeader(oauthParams, signature);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(baseUrl.concat(this.searchByNameUrl) + "?search_expression=" + encode(searchTerm) + "&format=json&page_number=" + pageNumber + "&max_results=" + maxResults, HttpMethod.GET, entity, String.class);
    }

    public ResponseEntity<String> getProductById(String id) throws Exception {
        Map<String, String> queryParams = Map.of(
                "food_id", id,
                "format", "json"
        );

        Map<String, String> oauthParams = createOAuthParams();
        String baseString = createSignatureBaseString("GET", baseUrl.concat(this.searchByIdUrl), new TreeMap<>(queryParams) {{
            putAll(oauthParams);
        }});
        String signature = calculateSignature(baseString, consumerSecret, "");

        String authorizationHeader = generateAuthorizationHeader(oauthParams, signature);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(baseUrl.concat(this.searchByIdUrl) + "?food_id=" + encode(id) + "&format=json", HttpMethod.GET, entity, String.class);
    }
}
