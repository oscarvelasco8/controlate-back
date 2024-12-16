package controlate_back.api.controllers;

import controlate_back.api.models.UserCaloriesHistory;
import controlate_back.api.services.UserCaloriesHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    public UserCaloriesHistory createCaloriesHistory(@RequestBody UserCaloriesHistory history) {

        return userCaloriesHistoryService.saveCaloriesHistory(history);
    }

    // Eliminar un registro de historia de calorías
    @DeleteMapping("/{logId}")
    public void deleteCaloriesHistory(@PathVariable String logId) {
        userCaloriesHistoryService.deleteCaloriesHistory(logId);
    }

    @GetMapping("/by-date")
    public List<UserCaloriesHistory> getUserHistoryByDate(
            @RequestParam String username,
            @RequestParam String logDate) {
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

        // Define el formato de la fecha
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Parsea la fecha con el formato correcto
        LocalDate parsedStartDate = LocalDate.parse(startDate, formatter);

        return userCaloriesHistoryService.getCaloriesForDateRange(username, parsedStartDate);
    }
}
