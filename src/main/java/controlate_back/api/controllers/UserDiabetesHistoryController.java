package controlate_back.api.controllers;

import controlate_back.api.models.UserDiabetesHistory;
import controlate_back.api.services.UserDiabetesHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user-diabetes-history")
public class UserDiabetesHistoryController {
    @Autowired
    private UserDiabetesHistoryService userDiabetesHistoryService;

    // Obtener todos los registros de historia de calorías
    @GetMapping
    public List<UserDiabetesHistory> getAllDiabetesHistory() {
        return userDiabetesHistoryService.getAllDiabetesHistory();
    }

    // Obtener un registro por ID
    @GetMapping("/{logId}")
    public Optional<UserDiabetesHistory> getDiabetesHistoryById(@PathVariable String logId) {
        return userDiabetesHistoryService.getDiabetesHistoryById(logId);
    }

    // Crear un nuevo registro de historia de calorías
    @PostMapping
    public UserDiabetesHistory createDiabetesHistory(@RequestBody UserDiabetesHistory history) {
        return userDiabetesHistoryService.saveDiabetesHistory(history);
    }

    // Eliminar un registro de historia de calorías
    @DeleteMapping("/{logId}")
    public void deleteDiabetesHistory(@PathVariable String logId) {
        userDiabetesHistoryService.deleteDiabetesHistory(logId);
    }

    @GetMapping("/by-date")
    public List<UserDiabetesHistory> getUserDiabetesByDate(
            @RequestParam String username,
            @RequestParam String logDate) {
        // Define el formato de la fecha
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Parsea la fecha con el formato correcto
        LocalDate parsedDate = LocalDate.parse(logDate, formatter);
        return userDiabetesHistoryService.getDiabetesHistoryByMeal(username, parsedDate);
    }

    @GetMapping("last-7days")
    public Map<LocalDate, Double> getUserHistoryByDateRange(
            @RequestParam String username,
            @RequestParam String startDate) {

        // Define el formato de la fecha
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Parsea la fecha con el formato correcto
        LocalDate parsedStartDate = LocalDate.parse(startDate, formatter);

        return userDiabetesHistoryService.getPortionsForDateRange(username, parsedStartDate);
    }
}
