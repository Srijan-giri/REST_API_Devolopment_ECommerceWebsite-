package com.lcwd.electronic.store.controller;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.ImageResponse;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.service.FileService;
import com.lcwd.electronic.store.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;


    private Logger logger = LoggerFactory.getLogger(UserController.class);

    // create

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto)
    {
        UserDto userDto1 = userService.createUser(userDto);

        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }


    // update

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto,
                                              @PathVariable("userId")String id)
    {
        UserDto updateUser = userService.updateUser(userDto,id);

        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }



    // delete

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable("userId")String id) throws IOException {
        userService.deleteUser(id);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("User is deleted successfully")
                .status(HttpStatus.OK)
                .success(true).build();

        return new ResponseEntity<>(message, HttpStatus.OK);
    }


    // get all

    @GetMapping
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
            @RequestParam(value="pageNumber",defaultValue = "0" , required = false) int pageNumber,
            @RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue = "name",required = false) String sortBy,
            @RequestParam(value="sortDir",defaultValue = "asc",required = false) String  sortDir
    )
    {
        PageableResponse<UserDto> allUsers = userService.getAllUsers(pageNumber, pageSize, sortBy, sortDir);

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


    // upload user image

    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(
            @RequestParam("userImage")MultipartFile image,
            @PathVariable("userId") String userId
    ) throws IOException
    {
        String imageName = fileService.uploadImage(image, imageUploadPath);

        UserDto userdto = userService.getUserById(userId);

        userdto.setImageName(imageName);

        UserDto userDto = userService.updateUser(userdto, userId);

        ImageResponse imageResponse = ImageResponse.builder()
                .imageName(imageName)
                .message("Image uploaded successfully")
                .status(HttpStatus.CREATED)
                .success(true).build();

        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);

    }


    // serve user image

    @GetMapping("/image/{userId}")
    public void serveUserImage(
            @PathVariable("userId") String userId,
            HttpServletResponse response
    ) throws IOException {
        UserDto userdto = userService.getUserById(userId);
        logger.info("User image name : {}",userdto.getImageName());

        InputStream resource = fileService.getResource(imageUploadPath, userdto.getImageName());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);

        StreamUtils.copy(resource,response.getOutputStream());

    }

}
