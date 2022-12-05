package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto getUser(int id) {
        return userRepository.getUser(id);
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        return userRepository.addUser(userDto);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        return userRepository.updateUser(userDto);
    }

    @Override
    public List<UserDto> allUsers() {
        return userRepository.allUsers();
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteUser(id);
    }

    @Override
    public void deleteAllUsers() {

    }
}
