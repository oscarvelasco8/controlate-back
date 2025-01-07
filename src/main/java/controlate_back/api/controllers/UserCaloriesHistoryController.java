package controlate_back.api.controllers;

import controlate_back.api.models.UserCaloriesHistory;
import controlate_back.api.services.UserCaloriesHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user-calories-history")
public class UserCaloriesHistoryController {

    @Autowired
    private UserCaloriesHistoryService userCaloriesHistoryService;

    // Obtener todos los registros de historia de calorías
    @GetMapping
    public List<UserCaloriesHistory> getAllCaloriesHistory() {
        return userCaloriesHistoryService.getAllCaloriesHistory();
    }

    // Obtener un registro por ID
    @GetMapping("/{logId}")
    public Optional<UserCaloriesHistory> getCaloriesHistoryById(@PathVariable String logId) {
        return userCaloriesHistoryService.getCaloriesHistoryById(logId);
    }

    // Crear un nuevo registro de historia de calorías
    @PostMapping
    public ResponseEntity<?> createCaloriesHistory(@RequestBody List<UserCaloriesHistory> history) {
        history.forEach(userCaloriesHistory -> userCaloriesHistoryService.saveCaloriesHistory(userCaloriesHistory));
        return ResponseEntity.ok(history);
    }

    // Eliminar un registro de historia de calorías
    @DeleteMapping
    public void deleteHistory(@RequestParam("ids") String ids) {
        List<String> logIds = Arrays.stream(ids.split(","))
                .toList();
        // Lógica para borrar registros por sus IDs
        logIds.forEach(id -> userCaloriesHistoryService.deleteCaloriesHistory(id));
    }

    @GetMapping("/by-date")
    public List<UserCaloriesHistory> getUserHistoryByDate(
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
        System.out.println(logDate);
        // Define el formato de la fecha
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Parsea la fecha con el formato correcto
        LocalDate parsedDate = LocalDate.parse(logDate, formatter);
        return userCaloriesHistoryService.getCaloriesHistoryByMeal(username, parsedDate);
    }

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

        return userCaloriesHistoryService.getCaloriesForDateRange(username, parsedStartDate);
    }
}
