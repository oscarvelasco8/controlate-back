package controlate_back.api.repositories;

import controlate_back.api.models.UserDiabetesHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserDiabetesHistoryRepository extends JpaRepository<UserDiabetesHistory, String> {
    @Query("SELECT u FROM UserDiabetesHistory u WHERE u.username = :username AND u.logDate = :logDate")
    List<UserDiabetesHistory> findByUsernameAndDate(String username, String logDate);
}
