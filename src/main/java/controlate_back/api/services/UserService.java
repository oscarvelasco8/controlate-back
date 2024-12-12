package controlate_back.api.services;

import controlate_back.api.models.User;
import controlate_back.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findById(username);
    }
    public Optional<User> getUserByEmail(String email) {
        return this.getAllUsers().stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public User updateUser(String username, User userDetails) {
        return userRepository.findById(username).map(user -> {
            user.setName(userDetails.getName());
            user.setLastname(userDetails.getLastname());
            user.setEmail(userDetails.getEmail());
            user.setAge(userDetails.getAge());
            user.setWeight(userDetails.getWeight());
            user.setHeight(userDetails.getHeight());
            user.setActivityFactor(userDetails.getActivityFactor());
            user.setInsulinaFactor(userDetails.getInsulinaFactor());
            user.setPassword(userDetails.getPassword());
            user.setUsername(userDetails.getUsername());
            user.setGender(userDetails.getGender());
            user.setObjective(userDetails.getObjective());
            user.setIcr(userDetails.getIcr());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    public void deleteUser(String username) {
        userRepository.deleteById(username);
    }

    public float calculateTMB(User user) {
        float tmb;
        if (user.getGender() == User.gender.MALE) {
            tmb = 10 * user.getWeight() + 6.25f * user.getHeight() - 5 * user.getAge() + 5;
        } else {
            tmb = 10 * user.getWeight() + 6.25f * user.getHeight() - 5 * user.getAge() - 161;
        }
        return tmb;
    }

    // Aplicar factor de actividad para obtener el requerimiento calórico diario:
    public float calculateDailyCalories(User user) {
        float tmb = calculateTMB(user);
        return tmb * getActivityFactorValue(user.getActivityFactor());
    }


    private float getActivityFactorValue(User.FactorActividad activityFactor) {
        return switch (activityFactor) {
            case POCO_SEDENTARIO -> 1.375f;
            case MODERADAMENTE_SEDENTARIO -> 1.55f;
            case ACTIVO -> 1.725f;
            case MUY_ACTIVO -> 1.9f;
            default -> 1.2f;
        };
    }

    public void setObjective(User user, User.objective objective) {
        user.setObjective(objective);
    }

    public User.objective getObjective(User user) {
        return user.getObjective();
    }
}

