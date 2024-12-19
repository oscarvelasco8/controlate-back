package controlate_back.api.controllers;

import controlate_back.api.models.User;
import controlate_back.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Obtener todos los usuarios
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    //Obtener usuario
    @GetMapping("/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        User user = userService.getUserByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("Usuario no encontrado.");
        }
        return ResponseEntity.ok(user);
    }

    // Obtener un usuario por nombre de usuario
    @GetMapping("/login")
    public ResponseEntity<?> getUserByUsername(
            @RequestParam String username,
            @RequestParam String password
    ) {
        User user = userService.getUserByUsername(username).orElse(null);

        if (user != null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            // Comparar la contraseña proporcionada con el hash almacenado
            if (passwordEncoder.matches(password, user.getPassword())) {
                return ResponseEntity.ok(user);
            }
        }
        return ResponseEntity.badRequest().body("Usuario o contraseña incorrectos.");
    }


    // Crear un nuevo usuario
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (userService.getUserByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("El nombre de usuario ya está en uso.");
        }
        if (userService.getUserByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("El correo electrónico ya está en uso.");
        }

        // Hashear la contraseña antes de guardarla
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userService.createUser(user);
        return ResponseEntity.ok(user);
    }


    // Actualizar un usuario existente
    @PutMapping("/{username}")
    public ResponseEntity<?> updateUser(
            @PathVariable String username,
            @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(username, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Eliminar un usuario por nombre de usuario
    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.ok("Usuario eliminado con éxito.");
    }

    @GetMapping("/tmb/{username}")
    public float calculateTMB(@PathVariable String username) {
        User user = userService.getUserByUsername(username).orElse(null);
        if (user != null) {
            return userService.calculateTMB(user);
        }
        return 0;
    }

    @GetMapping("/daily-calories/{username}")
    public float calculateDailyCalories(@PathVariable String username) {
        User user = userService.getUserByUsername(username).orElse(null);
        if (user != null) {
            return userService.calculateDailyCalories(user);
        }
        return 0;
    }

    @PatchMapping("/objective/{username}")
    public void updateObjective(@PathVariable String username, @RequestBody String objective) {
        User user = userService.getUserByUsername(username).orElse(null);
        if (user != null) {
            user.setObjective(User.objective.valueOf(objective));
            userService.updateUser(username, user);
        }
    }

    @GetMapping("/objective/{username}")
    public String getObjective(@PathVariable String username) {
        User user = userService.getUserByUsername(username).orElse(null);
        if (user != null) {
            return user.getObjective().toString();
        }
        return null;
    }
}

