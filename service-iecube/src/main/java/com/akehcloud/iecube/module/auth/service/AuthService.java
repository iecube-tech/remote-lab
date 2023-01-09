package com.akehcloud.iecube.module.auth.service;

import com.akehcloud.iecube.module.auth.dto.LoginDTO;
import com.akehcloud.iecube.module.auth.dto.LoginResultDTO;

public interface AuthService {

    LoginResultDTO login(LoginDTO loginDTO);

}
