package controlate_back.api.repositories;

import controlate_back.api.models.UserCaloriesHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface UserCaloriesHistoryRepository extends JpaRepository<UserCaloriesHistory, String> {

    @Query("SELECT u FROM UserCaloriesHistory u WHERE u.username = :username AND u.logDate = :logDate")
    List<UserCaloriesHistory> findByUsernameAndDate(@Param("username") String username,
                                                    @Param("logDate") LocalDate logDate);

    @Query("SELECT u FROM UserCaloriesHistory u WHERE u.username = :username AND u.logDate BETWEEN :startDate AND :endDate")
    List<UserCaloriesHistory> findByUsernameAndDateRange(@Param("username") String username,
                                                         @Param("startDate") LocalDate startDate,
                                                         @Param("endDate") LocalDate endDate);
}
