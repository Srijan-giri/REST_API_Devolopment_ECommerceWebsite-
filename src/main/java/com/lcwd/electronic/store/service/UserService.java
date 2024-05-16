package com.lcwd.electronic.store.service;

import com.lcwd.electronic.store.dtos.UserDto;

import java.util.List;

public interface UserService {

    // create

    UserDto createUser(UserDto userDto);

    // update

    UserDto updateUser(UserDto userDto,String userId);

    // delete

    void deleteUser(String userId);

    // get all users

    List<UserDto> getAllUsers();

    // get single user by id

    UserDto getUserById(String userId);

    // get single user by email

    UserDto getUserByEmail(String email);

    // search user

    List<UserDto>  searchUser(String keyword);


    // other user specific features
}
