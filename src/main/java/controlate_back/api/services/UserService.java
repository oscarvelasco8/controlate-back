package controlate_back.api.services;

import controlate_back.api.models.User;
import controlate_back.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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
        user.setObjective(User.objective.MANTENIMIENTO);
        userRepository.save(user);
    }

    public User updateUser(String username, User userDetails) {
        //System.out.println(userDetails.getPassword());
        //System.out.println(userDetails.getPassword() != null);
        return userRepository.findById(username).map(user -> {
            if (userDetails.getName() != null) {
                user.setName(userDetails.getName());
            }
            if (userDetails.getLastname() != null) {
                user.setLastname(userDetails.getLastname());
            }
            if (userDetails.getEmail() != null) {
                user.setEmail(userDetails.getEmail());
            }
            if (userDetails.getAge() != null) {
                user.setAge(userDetails.getAge());
            }
            if (userDetails.getWeight() != null) {
                user.setWeight(userDetails.getWeight());
            }
            if (userDetails.getHeight() != null) {
                user.setHeight(userDetails.getHeight());
            }
            if (userDetails.getActivityFactor() != null) {
                user.setActivityFactor(userDetails.getActivityFactor());
            }
            if (userDetails.getInsulinaFactor() != null) {
                user.setInsulinaFactor(userDetails.getInsulinaFactor());
            }
            if (userDetails.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            }
            if (userDetails.getUsername() != null) {
                user.setUsername(userDetails.getUsername());
            }
            if (userDetails.getGender() != null) {
                user.setGender(userDetails.getGender());
            }
            if (userDetails.getObjective() != null) {
                user.setObjective(userDetails.getObjective());
            }
            if (userDetails.getIcr() != null) {
                user.setIcr(userDetails.getIcr());
            }
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    public void updateObjective(String username, String objective) {
        User user = userRepository.findById(username).orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        user.setObjective(User.objective.valueOf(objective));
        userRepository.save(user);
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
        return tmb * getActivityFactorValue(user.getActivityFactor()) * getObjectiveValue(user.getObjective());
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

    private float getObjectiveValue(User.objective objective) {
        return switch (objective) {
            case BAJAR_LIGERO -> 0.9f;
            case BAJAR_MODERADO -> 0.8f;
            case SUBIR_MODERADO -> 1.2f;
            case SUBIR_LIGERO -> 1.3f;
            default -> 1.0f;
        };
    }

    public void setObjective(User user, User.objective objective) {
        user.setObjective(objective);
    }

    public User.objective getObjective(User user) {
        return user.getObjective();
    }
}

