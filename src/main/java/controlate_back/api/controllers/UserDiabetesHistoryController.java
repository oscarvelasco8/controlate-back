package controlate_back.api.controllers;

import controlate_back.api.models.UserDiabetesHistory;
import controlate_back.api.services.UserDiabetesHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        return userDiabetesHistoryService.getDiabetesHistoryByMeal(username, logDate);
    }
}
