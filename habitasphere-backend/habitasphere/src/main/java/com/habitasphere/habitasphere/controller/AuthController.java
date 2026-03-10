package com.habitasphere.habitasphere.controller;
import com.habitasphere.habitasphere.dto.AuthRequest;
import com.habitasphere.habitasphere.entity.User;
import com.habitasphere.habitasphere.repository.UserRepository;
import com.habitasphere.habitasphere.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
public String register(@RequestBody User user){

    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setIsActive(true);   // ADD THIS

    userRepository.save(user);

    return "User Registered Successfully";
}

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest request){

        User user = userRepository.findByEmail(request.getEmail());

        if(user == null){
            return "User not found";
        }

        if(passwordEncoder.matches(request.getPassword(), user.getPassword())){
            return jwtService.generateToken(user.getEmail());
        }

        return "Invalid Password";
    }
}