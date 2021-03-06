package com.ssafy.obosa.controller;

import com.ssafy.obosa.model.dto.SignupFormDto;
import com.ssafy.obosa.repository.UserRepository;
import com.ssafy.obosa.service.SignUpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("api/signup")
public class SignUpController
{
    @Autowired
    UserRepository userRepository;

    @Autowired
    SignUpService signUpService;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("duplicateEmail")
    public ResponseEntity duplicateUserEmail(@RequestParam String email)
    {
        return new ResponseEntity(signUpService.duplicateEmail(email), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("duplicateNickname")
    public ResponseEntity duplicateUserNickname(@RequestParam String nickname)
    {
        return new ResponseEntity(signUpService.duplicateNickname(nickname), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping
    public ResponseEntity signup(SignupFormDto signupFormDto)
    {
//        System.out.println("[ --> profile : " + profileImgFile.toString() + " ]");
        return new ResponseEntity(signUpService.newUser(signupFormDto), HttpStatus.OK);
    }

    @GetMapping("/confirm/{token}")
    public ResponseEntity verifyEmail(@PathVariable String token) {
        return new ResponseEntity(signUpService.confirmEmail(token), HttpStatus.OK);
    }
}
