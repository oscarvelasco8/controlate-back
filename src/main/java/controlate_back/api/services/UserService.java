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

    //Metodo para obtener todos los usuarios

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //Metodo para obtener un usuario por su username

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findById(username);
    }

    //Metodo para obtener un usuario por su email
    public Optional<User> getUserByEmail(String email) {
        return this.getAllUsers().stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }

    //Metodo para crear un nuevo usuario

    public void createUser(User user) {
        user.setObjective(User.objective.MANTENIMIENTO);
        userRepository.save(user);
    }

    //Metodo para actualizar un usuario

    public User updateUser(String username, User userDetails) {
        //Recogemos los datos del usuario siempre y cuando no sean nulos
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

    //Metodo para actualizar el objetivo del usuario
    public void updateObjective(String username, String objective) {
        User user = userRepository.findById(username).orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        user.setObjective(User.objective.valueOf(objective));
        userRepository.save(user);
    }

    //Metodo para eliminar un usuario
    public void deleteUser(String username) {
        userRepository.deleteById(username);
    }

    //Metodo para calcular el TMB del usuario

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

    // Obtener el valor del factor de actividad
    private float getActivityFactorValue(User.FactorActividad activityFactor) {
        return switch (activityFactor) {
            case POCO_SEDENTARIO -> 1.375f;
            case MODERADAMENTE_SEDENTARIO -> 1.55f;
            case ACTIVO -> 1.725f;
            case MUY_ACTIVO -> 1.9f;
            default -> 1.2f;
        };
    }

    // Obtener el valor del objetivo
    private float getObjectiveValue(User.objective objective) {
        return switch (objective) {
            case BAJAR_LIGERO -> 0.9f;
            case BAJAR_MODERADO -> 0.8f;
            case SUBIR_MODERADO -> 1.2f;
            case SUBIR_LIGERO -> 1.3f;
            default -> 1.0f;
        };
    }

    // Actualizar el objetivo del usuario

    public void setObjective(User user, User.objective objective) {
        user.setObjective(objective);
    }

    // Obtener el objetivo del usuario
    public User.objective getObjective(User user) {
        return user.getObjective();
    }
}

