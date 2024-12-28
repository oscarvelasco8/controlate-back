package controlate_back.api.services;

import controlate_back.api.models.UserCaloriesHistory;
import controlate_back.api.models.UserDiabetesHistory;
import controlate_back.api.repositories.UserDiabetesHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserDiabetesHistoryService {
    @Autowired
    private UserDiabetesHistoryRepository userDiabetesHistoryRepository;

    public List<UserDiabetesHistory> getAllDiabetesHistory() {
        return userDiabetesHistoryRepository.findAll();
    }

    public Optional<UserDiabetesHistory> getDiabetesHistoryById(String logId) {
        return userDiabetesHistoryRepository.findById(logId);
    }

    public UserDiabetesHistory saveDiabetesHistory(UserDiabetesHistory history) {
        return userDiabetesHistoryRepository.save(history);
    }

    public void deleteDiabetesHistory(String logId) {
        userDiabetesHistoryRepository.deleteById(logId);
    }

    public List<UserDiabetesHistory> getDiabetesHistoryByMeal(String username, LocalDate logDate) {
        return userDiabetesHistoryRepository.findByUsernameAndDate(username, logDate);
    }

    public Map<LocalDate, Double> getPortionsForDateRange(String username, LocalDate startDate) {
        //System.out.println("START DATE: " + startDate);
        // Calcular la fecha de inicio (7 días antes de la fecha dada)
        LocalDate endDate = startDate.minusDays(7); // 7 días antes de la fecha de inicio

        // Obtener las calorías del repositorio
        List<UserDiabetesHistory> records = userDiabetesHistoryRepository.findByUsernameAndDateRange(username, endDate, startDate);

        // Generar las fechas en el rango (desde endDate hasta startDate)
        List<LocalDate> dateRange = generateDateRange(endDate, startDate);
        //System.out.println(dateRange);

        // Crear un mapa de las calorías por fecha utilizando TreeMap para mantener el orden
        Map<LocalDate, Double> portionsMap = records.stream()
                .collect(Collectors.groupingBy(UserDiabetesHistory::getLogDate,
                        TreeMap::new, // Usar TreeMap para ordenar las fechas
                        Collectors.summingDouble(UserDiabetesHistory::getPortions)));

        // Para cada fecha en el rango, asegurar que exista un valor en el mapa (incluso si es 0)
        Map<LocalDate, Double> result = new TreeMap<>(); // Usar TreeMap también para el resultado
        for (LocalDate date : dateRange) {
            result.put(date, portionsMap.getOrDefault(date, 0.0));
        }

        //System.out.println(result);
        return result;
    }


    private List<LocalDate> generateDateRange(LocalDate startDate, LocalDate endDate) {
        return Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(startDate.until(endDate).getDays() + 1)
                .collect(Collectors.toList());
    }
}
