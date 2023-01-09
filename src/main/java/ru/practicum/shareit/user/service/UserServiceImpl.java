package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDto getUser(int id) {
        return UserMapper.toUserDto(userRepository.getUser(id));
    }

    @Transactional
    @Override
    public UserDto addUser(User user) {
        return UserMapper.toUserDto(userRepository.addUser(user));
    }

    @Transactional
    @Override
    public UserDto updateUser(UserDto user) {
        return UserMapper.toUserDto(userRepository.updateUser(user));
    }

    @Transactional
    @Override
    public List<User> allUsers() {
        return userRepository.allUsers();
    }

    @Transactional
    @Override
    public void deleteUser(int id) {
        userRepository.deleteUser(id);
    }

}
