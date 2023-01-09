package com.akehcloud.iecube.module.auth.dto;

import lombok.Data;

/**
 * @author panghaoyue
 */
@Data
public class LoginResultDTO {

    private String accessToken;
    private CurrentUserDTO currentUser;

    public static LoginResultDTO result(String accessToken, CurrentUserDTO currentUser) {
        LoginResultDTO loginResultDTO = new LoginResultDTO();
        loginResultDTO.accessToken = accessToken;
        loginResultDTO.currentUser = currentUser;
        return loginResultDTO;
    }

}
