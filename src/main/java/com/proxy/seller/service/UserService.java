package com.proxy.seller.service;

import com.proxy.seller.data.User;
import com.proxy.seller.data.UserRole;
import com.proxy.seller.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Класс, который содержит методы для регистрации и аутентификации пользователей.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Создает новый объект класса UserService.
     *
     * @param userRepository  Репозиторий для работы с пользователями
     * @param passwordEncoder Кодировщик паролей
     */
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Регистрирует нового пользователя в системе.
     *
     * @param user Данные нового пользователя
     * @return Код успешного выполнения и данные зарегистрированного пользователя
     */
    public ResponseEntity<User> registerUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRole(UserRole.REGISTERED);
        userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    /**
     * Проверяет, совпадают ли данные пользователя введенные при аутентификации с данными пользователя из базы данных.
     *
     * @param user Данные пользователя для аутентификации
     * @return true, если данные совпадают, false в противном случае
     */
    public ResponseEntity<HttpStatus> checkUserLoginWithStatus(User user) {
        if (!checkUserLogin(user)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    /**
     * Проверяет, совпадают ли данные пользователя введенные при аутентификации с данными пользователя из базы данных.
     *
     * @param user Данные пользователя для аутентификации
     * @return Код успешного выполнения, если данные совпадают, либо код ошибки 401, если данные не совпадают
     */
    public boolean checkUserLogin(User user) {
        User storedUser = userRepository.findByUsername(user.getUsername()).orElse(null);
        return storedUser != null && passwordEncoder.matches(user.getPassword(), storedUser.getPassword());
    }
}
