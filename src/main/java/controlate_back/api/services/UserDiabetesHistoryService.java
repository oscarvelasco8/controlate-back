package controlate_back.api.services;

import controlate_back.api.models.UserDiabetesHistory;
import controlate_back.api.repositories.UserDiabetesHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<UserDiabetesHistory> getDiabetesHistoryByMeal(String username, String logDate) {
        return userDiabetesHistoryRepository.findByUsernameAndDate(username, logDate);
    }
}
