package com.store.management.service;

import com.store.management.exceptions.ResourceNotFoundException;
import com.store.management.model.User;
import com.store.management.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testAddUser() throws ResourceNotFoundException {
        var user = new User();
        user.setIdUser(1L);
        user.setName("user");
        user.setPassword("1234");

        var expectedResult = ResponseEntity.ok("Successfully user added");

        when(userRepository.save(user)).thenReturn(user);

        var actualResult = userService.addNewUser(user);

        assertEquals(expectedResult.getBody(), actualResult.getBody());

        verify(userRepository).save(any());
    }

    @Test
    void testAddUser_NotFound() {
        var user = new User();
        user.setIdUser(1L);
        user.setName("user");
        user.setPassword("1234");
        when(userRepository.save(user)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> userService.addNewUser(user));

        verify(userRepository).save(any());
    }

    @Test
    void testUpdateUser() throws ResourceNotFoundException {
        var user = new User();
        user.setIdUser(1L);
        user.setName("user");
        user.setPassword("1234");

        var expectedResult = ResponseEntity.ok(user);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        ResponseEntity<User> actualResult = (ResponseEntity<User>) userService.updateUser(user, 1L);
        assertEquals(Objects.requireNonNull(expectedResult.getBody()).getIdUser(), Objects.requireNonNull(actualResult.getBody()).getIdUser());

        verify(userRepository).findById(anyLong());
        verify(userRepository).save(any());
    }

    @Test
    void testUpdateUser_NotFound() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(new User(), 1L));

        verify(userRepository).findById(anyLong());
    }

    @Test
    void testGetUserById() throws ResourceNotFoundException {
        var expectedUser = new User();
        expectedUser.setIdUser(1L);
        expectedUser.setName("expectedUser");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(expectedUser));

        var actualResult = userService.getUserById(anyLong());

        assertEquals(expectedUser.getIdUser(), actualResult.getIdUser());

        verify(userRepository).findById(anyLong());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(anyLong()));

        verify(userRepository).findById(anyLong());
    }

    @Test
    void testGetUsers() {
        var user = new User();
        user.setIdUser(1L);
        user.setName("user");
        var expectedResult = List.of(user);
        when(userRepository.findAll()).thenReturn(expectedResult);
        var actualResult = userService.getUsers();

        assertEquals(expectedResult, actualResult);

        verify(userRepository).findAll();
    }

    @Test
    void testDeleteUserById() {
        var expectedResult = ResponseEntity.ok("User deleted successfully");
        var user = new User();
        user.setIdUser(1L);
        user.setName("user");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        var actualResult = userService.deleteUserById(anyLong());

        assertEquals(expectedResult.getBody(), actualResult.getBody());
        assertEquals(expectedResult.getStatusCode().value(), actualResult.getStatusCode().value());

        verify(userRepository).findById(anyLong());
        verify(userRepository).delete(any());
    }


    @Test
    void testDeleteUserById_NotFound() {
        var expectedResult = ResponseEntity.of(Optional.of("User not found"));

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        var actualResult = userService.deleteUserById(anyLong());


        assertEquals(expectedResult.getBody(), actualResult.getBody());


    }
}
