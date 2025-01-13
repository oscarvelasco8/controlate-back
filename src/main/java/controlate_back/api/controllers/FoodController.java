package controlate_back.api.controllers;
import controlate_back.api.services.FoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Controlador de Alimentos", description = "Operaciones relacionadas con alimentos desde la API de FatSecret.")
@RestController
public class FoodController {
    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @Operation(
            summary = "Buscar alimento por nombre",
            description = "Devuelve alimentos de la API de FatSecret.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Array de alimentos",
                            content = @Content(mediaType = "application/json")
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
            parameters = {
                    @Parameter(name = "searchTerm", description = "Término a buscar", required = true),
                    @Parameter(name = "pageNumber", description = "Alimentos por página", required = false, example = "0"),
                    @Parameter(name = "maxResults", description = "Numero máximo de resultados", required = false, example = "2")
            }
    )

    @GetMapping("/api/search-food-by-name")
    public ResponseEntity<String> searchFood(@RequestParam String searchTerm,
                                             @RequestParam(defaultValue = "0") int pageNumber,
                                             @RequestParam(defaultValue = "2") int maxResults) throws Exception {
        return foodService.getProductsByName(searchTerm, pageNumber, maxResults);
    }

    @Operation(
            summary = "Buscar alimento por ID",
            description = "Devuelve un alimento de la API de FatSecret.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Alimento buscado",
                            content = @Content(mediaType = "application/json")
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
            parameters = {
                    @Parameter(name = "id", description = "ID del alimento a buscar", required = true, example = "1")
            }
    )

    @GetMapping("/api/search-food-by-id")
    public ResponseEntity<String> searchFoodById(@RequestParam String id) throws Exception {
        return foodService.getProductById(id);
    }
}



