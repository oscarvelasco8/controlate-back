package controlate_back.api.repositories;

import controlate_back.api.models.UserCaloriesHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserCaloriesHistoryRepository extends JpaRepository<UserCaloriesHistory, Integer> {

    @Query("SELECT u FROM UserCaloriesHistory u WHERE u.username = :username AND u.logDate = :logDate")
    List<UserCaloriesHistory> findByUsernameAndDate(String username, String logDate);
}
