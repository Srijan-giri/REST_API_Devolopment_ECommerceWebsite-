package com.lcwd.electronic.store.controller;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;



    // create

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto)
    {
        UserDto userDto1 = userService.createUser(userDto);

        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }


    // update

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto,
                                              @PathVariable("userId")String id)
    {
        UserDto updateUser = userService.updateUser(userDto,id);

        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }



    // delete

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable("userId")String id)
    {
        userService.deleteUser(id);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("User is deleted successfully")
                .status(HttpStatus.OK)
                .success(true).build();

        return new ResponseEntity<>(message, HttpStatus.OK);
    }


    // get all

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers()
    {
        List<UserDto> allUsers = userService.getAllUsers();

        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }


    // get single

    @GetMapping("/{userid}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("userid")String id) {
        UserDto userById = userService.getUserById(id);

        return new ResponseEntity<>(userById, HttpStatus.OK);
    }

    // get by email

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable("email") String email)
    {
        UserDto userDto = userService.getUserByEmail(email);

        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }


    // search user

    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable("keywords") String keywords)
    {
        List<UserDto> searchUsers = userService.searchUser(keywords);

        return new ResponseEntity<>(searchUsers,HttpStatus.OK);
    }

}
