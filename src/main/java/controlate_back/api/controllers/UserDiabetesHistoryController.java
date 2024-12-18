package controlate_back.api.controllers;

import controlate_back.api.models.UserDiabetesHistory;
import controlate_back.api.services.UserDiabetesHistoryService;
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
    public ResponseEntity<?> createDiabetesHistory(@RequestBody List<UserDiabetesHistory> history) {
        history.forEach(userDiabetesHistory -> userDiabetesHistoryService.saveDiabetesHistory(userDiabetesHistory));
        return ResponseEntity.ok(history);
    }

    // Eliminar un registro de historia de calorías
    @DeleteMapping
    public void deleteHistory(@RequestParam("ids") String ids) {
        List<String> logIds = Arrays.stream(ids.split(","))
                .toList();
        // Lógica para borrar registros por sus IDs
        logIds.forEach(id -> userDiabetesHistoryService.deleteDiabetesHistory(id));
    }

    @GetMapping("/by-date")
    public List<UserDiabetesHistory> getUserDiabetesByDate(
            @RequestParam String username,
            @RequestParam String logDate) {
        if (Integer.parseInt(logDate.split("-")[2]) < 10){
            logDate = logDate.split("-")[0] + "-" + logDate.split("-")[1] + "-0" + logDate.split("-")[2];
        }

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

        if (Integer.parseInt(startDate.split("-")[2]) < 10){
            startDate = startDate.split("-")[0] + "-" + startDate.split("-")[1] + "-0" + startDate.split("-")[2];
        }

        // Define el formato de la fecha
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Parsea la fecha con el formato correcto
        LocalDate parsedStartDate = LocalDate.parse(startDate, formatter);

        return userDiabetesHistoryService.getPortionsForDateRange(username, parsedStartDate);
    }
}
