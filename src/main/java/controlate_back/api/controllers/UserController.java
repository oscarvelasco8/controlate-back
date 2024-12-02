package controlate_back.api.controllers;

import controlate_back.api.models.User;
import controlate_back.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    // Obtener un usuario por nombre de usuario
    @GetMapping("/login")
    public ResponseEntity<?> getUserByUsername(
            @RequestParam String username,
            @RequestParam String password
    ) {
        User user = userService.getUserByUsername(username).orElse(null);
        if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().body("Usuario no encontrado.");
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> checkUserLogging(@PathVariable String username) {
        User user = userService.getUserByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("Usuario no encontrado.");
        }
        return ResponseEntity.ok(user);
    }

    // Crear un nuevo usuario
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (userService.getUserByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("El nombre de usuario ya está en uso.");
        }
        if (userService.getUserByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("El correo electrónico ya está en uso.");
        }
        userService.createUser(user);
        return ResponseEntity.ok(user);
    }

    // Actualizar un usuario existente
    @PutMapping("/{username}")
    public ResponseEntity<User> updateUser(
            @PathVariable String username,
            @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(username, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar un usuario por nombre de usuario
    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.ok("Usuario eliminado con éxito.");
    }
}

