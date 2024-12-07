package controlate_back.api.services;

import controlate_back.api.models.UserCaloriesHistory;
import controlate_back.api.repositories.UserCaloriesHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserCaloriesHistoryService {

    @Autowired
    private UserCaloriesHistoryRepository userCaloriesHistoryRepository;

    public List<UserCaloriesHistory> getAllCaloriesHistory() {
        return userCaloriesHistoryRepository.findAll();
    }

    public Optional<UserCaloriesHistory> getCaloriesHistoryById(Integer logId) {
        return userCaloriesHistoryRepository.findById(logId);
    }

    public UserCaloriesHistory saveCaloriesHistory(UserCaloriesHistory history) {
        return userCaloriesHistoryRepository.save(history);
    }

    public void deleteCaloriesHistory(Integer logId) {
        userCaloriesHistoryRepository.deleteById(logId);
    }

    public List<UserCaloriesHistory> getCaloriesHistoryByMeal(String username, String logDate) {
        return userCaloriesHistoryRepository.findByUsernameAndDate(username, logDate);
    }
}
