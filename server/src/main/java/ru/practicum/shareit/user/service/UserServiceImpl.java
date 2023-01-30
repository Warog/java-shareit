package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.UserNotFoundException;
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
        if (userRepository.countUserById(id) < 1)
            throw new UserNotFoundException(String.format("Пользователь с ID=%d", id));
        return UserMapper.toUserDto(userRepository.getById(id));
    }

    @Transactional
    @Override
    public UserDto updateUser(User user) {
        User updatedUser = userRepository.getById(user.getId());
        if(user.getName() != null)
            updatedUser.setName(user.getName());
        if(user.getEmail() != null)
            updatedUser.setEmail(user.getEmail());

        return UserMapper.toUserDto(userRepository.save(updatedUser));
    }

    @Override
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public UserDto addUser(User user) {
        return UserMapper.toUserDto(userRepository.save(user));
    }


    @Transactional
    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

}
