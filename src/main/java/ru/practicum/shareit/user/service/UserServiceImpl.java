package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDto getUser(int id) {
        return userRepository.getUser(id);
    }

    @Transactional
    @Override
    public UserDto addUser(UserDto userDto) {
        return userRepository.addUser(userDto);
    }

    @Transactional
    @Override
    public UserDto updateUser(UserDto userDto) {
        return userRepository.updateUser(userDto);
    }

    @Transactional
    @Override
    public List<UserDto> allUsers() {
        return userRepository.allUsers();
    }

    @Transactional
    @Override
    public void deleteUser(int id) {
        userRepository.deleteUser(id);
    }

}
