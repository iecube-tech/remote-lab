package com.akehcloud.iecube.module.auth.controller;

import com.akehcloud.iecube.module.auth.dto.CurrentUserDTO;
import com.akehcloud.iecube.module.auth.dto.LoginDTO;
import com.akehcloud.iecube.module.auth.dto.LoginResultDTO;
import com.akehcloud.iecube.module.auth.service.AuthService;
import com.akehcloud.iecube.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class AuthController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/login")
    public LoginResultDTO login(@RequestBody LoginDTO loginDTO) {
        LoginResultDTO loginResultDTO = authService.login(loginDTO);
        AuthUtils.cache(loginResultDTO.getCurrentUser(), stringRedisTemplate);
        return loginResultDTO;
    }

    @DeleteMapping(value = "/logout")
    public void logout() {
        AuthUtils.rm(stringRedisTemplate);
    }

    @GetMapping(value = "/current-user")
    public CurrentUserDTO currentUser() {
        return AuthUtils.getCurrentUser();
    }

}
