package controlate_back.api.services;

import controlate_back.api.models.UserCaloriesHistory;
import controlate_back.api.repositories.UserCaloriesHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserCaloriesHistoryService {

    @Autowired
    private UserCaloriesHistoryRepository userCaloriesHistoryRepository;

    public List<UserCaloriesHistory> getAllCaloriesHistory() {
        return userCaloriesHistoryRepository.findAll();
    }

        public Optional<UserCaloriesHistory> getCaloriesHistoryById(String logId) {
        return userCaloriesHistoryRepository.findById(logId);
    }

    public UserCaloriesHistory saveCaloriesHistory(UserCaloriesHistory history) {
        return userCaloriesHistoryRepository.save(history);
    }

    public void deleteCaloriesHistory(String logId) {
        userCaloriesHistoryRepository.deleteById(logId);
    }

    public List<UserCaloriesHistory> getCaloriesHistoryByMeal(String username, LocalDate logDate) {
        return userCaloriesHistoryRepository.findByUsernameAndDate(username, logDate);
    }

    public Map<LocalDate, Double> getCaloriesForDateRange(String username, LocalDate startDate) {
        // Calcular la fecha de inicio (7 días antes de la fecha dada)
        LocalDate endDate = startDate.minusDays(7); // 7 días antes de la fecha de inicio

        // Obtener las calorías del repositorio
        List<UserCaloriesHistory> records = userCaloriesHistoryRepository.findByUsernameAndDateRange(username, endDate, startDate);

        // Generar las fechas en el rango (desde endDate hasta startDate)
        List<LocalDate> dateRange = generateDateRange(endDate, startDate);
        System.out.println(dateRange);

        // Crear un mapa de las calorías por fecha utilizando TreeMap para mantener el orden
        Map<LocalDate, Double> caloriesMap = records.stream()
                .collect(Collectors.groupingBy(UserCaloriesHistory::getLogDate,
                        TreeMap::new, // Usar TreeMap para ordenar las fechas
                        Collectors.summingDouble(UserCaloriesHistory::getCalories)));

        // Para cada fecha en el rango, asegurar que exista un valor en el mapa (incluso si es 0)
        Map<LocalDate, Double> result = new TreeMap<>(); // Usar TreeMap también para el resultado
        for (LocalDate date : dateRange) {
            result.put(date, caloriesMap.getOrDefault(date, 0.0));
        }


        return result;
    }


    private List<LocalDate> generateDateRange(LocalDate startDate, LocalDate endDate) {
        return Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(startDate.until(endDate).getDays() + 1)
                .collect(Collectors.toList());
    }
}
