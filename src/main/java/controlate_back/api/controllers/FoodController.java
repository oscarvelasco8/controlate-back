package controlate_back.api.controllers;
import controlate_back.api.services.FoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FoodController {
    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping("/api/search-food-by-name")
    public ResponseEntity<String> searchFood(@RequestParam String searchTerm,
                                             @RequestParam(defaultValue = "0") int pageNumber,
                                             @RequestParam(defaultValue = "10") int maxResults) throws Exception {
        return foodService.getProductsByName(searchTerm, pageNumber, maxResults);
    }

    @GetMapping("/api/search-food-by-id")
    public ResponseEntity<String> searchFoodById(@RequestParam String id) throws Exception {
        return foodService.getProductById(id);
    }
}



