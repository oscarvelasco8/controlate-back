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
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    public void deleteUser(String username) {
        userRepository.deleteById(username);
    }
}

