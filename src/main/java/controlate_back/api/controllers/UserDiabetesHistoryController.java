package controlate_back.api.controllers;

import controlate_back.api.models.UserCaloriesHistory;
import controlate_back.api.models.UserDiabetesHistory;
import controlate_back.api.services.UserDiabetesHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Controlador del historial de diabetes", description = "Operaciones relacionadas con el historial de diabetes de un usuario")

@RestController
@RequestMapping("/api/user-diabetes-history")
public class UserDiabetesHistoryController {
    @Autowired
    private UserDiabetesHistoryService userDiabetesHistoryService;

    @Operation(
            summary = "Obtener todos los registros del historial de diabetes",
            description = "Devuelve todos los registros del historial de diabetes de todos los usuarios",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Historial de diabetes obtenido",
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
            }
    )

    // Obtener todos los registros de historia de calorías
    @GetMapping
    public List<UserDiabetesHistory> getAllDiabetesHistory() {
        return userDiabetesHistoryService.getAllDiabetesHistory();
    }

    @Operation(
            summary = "Obtener un registro del historial de diabetes por su ID",
            description = "Devuelve un registro del historial de diabetes por su ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Historial de diabetes obtenido",
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
                    @Parameter(
                            name = "logId",
                            description = "ID del registro de historial de diabetes",
                            required = true,
                            in = ParameterIn.PATH
                    )
            }
    )

    // Obtener un registro por ID
    @GetMapping("/{logId}")
    public Optional<UserDiabetesHistory> getDiabetesHistoryById(@PathVariable String logId) {
        return userDiabetesHistoryService.getDiabetesHistoryById(logId);
    }

    @Operation(
            summary = "Crear un nuevo registro del historial de diabetes",
            description = "Crea un nuevo registro del historial de diabetes",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Historial de diabetes creado",
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
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Lista de registros del historial de diabetes a insertar",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserDiabetesHistory.class))
                    )
            )
    )

    // Crear un nuevo registro de historia de calorías
    @PostMapping
    public ResponseEntity<?> createDiabetesHistory(@RequestBody List<UserDiabetesHistory> history) {
        history.forEach(userDiabetesHistory -> userDiabetesHistoryService.saveDiabetesHistory(userDiabetesHistory));
        return ResponseEntity.ok(history);
    }

    @Operation(
            summary = "Eliminar un registro del historial de diabetes",
            description = "Elimina un registro del historial de diabetes",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Historial de diabetes eliminado",
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
                    @Parameter(
                            name = "ids",
                            description = "IDs de los registros a eliminar",
                            required = true
                    )
            }
    )

    // Eliminar un registro de historia de calorías
    @DeleteMapping
    public void deleteHistory(@RequestParam("ids") String ids) {
        List<String> logIds = Arrays.stream(ids.split(","))
                .toList();
        // Lógica para borrar registros por sus IDs
        logIds.forEach(id -> userDiabetesHistoryService.deleteDiabetesHistory(id));
    }

    @Operation(
            summary = "Obtener el historial de diabetes de un usuario por fecha",
            description = "Devuelve el historial de diabetes de un usuario por fecha",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Historial de diabetes obtenido",
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
                    @Parameter(
                            name = "username",
                            description = "Nombre de usuario",
                            example = "juanperez",
                            required = true
                    ),
                    @Parameter(
                            name = "logDate",
                            description = "Fecha del registro",
                            example = "2023-06-01",
                            required = true
                    )
            }
    )

    @GetMapping("/by-date")
    public List<UserDiabetesHistory> getUserDiabetesByDate(
            @RequestParam String username,
            @RequestParam String logDate) {
        String[] dateParts = logDate.split("-"); // Dividir la fecha una vez
        String year = dateParts[0];
        String month = dateParts[1];
        String day = dateParts[2];

        // Asegurarse de que el mes y el día tienen dos dígitos
        if (month.length() == 1) {
            month = "0" + month;
        }
        if (day.length() == 1) {
            day = "0" + day;
        }

        // Reconstruir la fecha con los ceros añadidos si es necesario
        logDate = year + "-" + month + "-" + day;

        // Define el formato de la fecha
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Parsea la fecha con el formato correcto
        LocalDate parsedDate = LocalDate.parse(logDate, formatter);
        return userDiabetesHistoryService.getDiabetesHistoryByMeal(username, parsedDate);
    }


    @Operation(
            summary = "Obtener el historial de diabetes de un usuario por rango de fechas",
            description = "Devuelve el historial de diabetes de un usuario por rango de fechas",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Historial de diabetes obtenido",
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
                    @Parameter(
                            name = "username",
                            description = "Nombre de usuario",
                            example = "juanperez",
                            required = true
                    ),
                    @Parameter(
                            name = "startDate",
                            description = "Fecha de inicio del rango",
                            example = "2023-06-01",
                            required = true
                    )
            }
    )


    @GetMapping("last-7days")
    public Map<LocalDate, Double> getUserHistoryByDateRange(
            @RequestParam String username,
            @RequestParam String startDate) {

        String[] dateParts = startDate.split("-"); // Dividir la fecha una vez
        String year = dateParts[0];
        String month = dateParts[1];
        String day = dateParts[2];

        // Asegurarse de que el mes y el día tienen dos dígitos
        if (month.length() == 1) {
            month = "0" + month;
        }
        if (day.length() == 1) {
            day = "0" + day;
        }

        // Reconstruir la fecha con los ceros añadidos si es necesario
        startDate = year + "-" + month + "-" + day;

        // Define el formato de la fecha
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Parsea la fecha con el formato correcto
        LocalDate parsedStartDate = LocalDate.parse(startDate, formatter);

        return userDiabetesHistoryService.getPortionsForDateRange(username, parsedStartDate);
    }
}
