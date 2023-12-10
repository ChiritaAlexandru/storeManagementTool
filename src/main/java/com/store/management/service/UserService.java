package com.store.management.service;

import com.store.management.exceptions.ResourceNotFoundException;
import com.store.management.model.User;
import com.store.management.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public ResponseEntity<?> addNewUser(User user) throws ResourceNotFoundException {
        Long userId = userRepository.save(user).getIdUser();
        if (userId != null) {
            log.info(format("User added successfully %d with name %s", userId, user.getName()));
            return new ResponseEntity<>("Successfully user added", HttpStatus.CREATED);
        } else {
            log.error(format("Error adding a new product. User name %s .", user.getName()));
            throw new ResourceNotFoundException("User");
        }
    }

    public ResponseEntity<?> updateUser(User user, long userId) throws ResourceNotFoundException {
        var currentUser = userRepository.findById(userId);
        if (currentUser.isPresent()) {
            var newUser = mappedUser(userId, user);
            userRepository.save(newUser);
            log.info(format("User information updated successfully. User id %d .", userId));
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } else
            throw new ResourceNotFoundException("User");
    }

    public User getUserById(long id) throws ResourceNotFoundException {
        log.info(format("Retrieved  user with ID %d from the database.", id));
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public List<User> getUsers() {
        log.info("Retrieved  users from the database.");
        return userRepository.findAll();
    }

    public ResponseEntity<?> deleteUserById(Long userId) {
        var user = userRepository.findById(userId);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            log.info(format("User deleted successfully. User id %d", userId));
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } else {
            log.error(format("Failed to delete user with id %d", userId));
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

    }

    private User mappedUser(long userId, User user) {
        var newUser = new User();
        newUser.setIdUser(userId);
        newUser.setName(user.getName());
        newUser.setPassword(user.getPassword());
        newUser.setEmail(user.getEmail());
        newUser.setUserType(user.getUserType());
        return newUser;
    }


}
