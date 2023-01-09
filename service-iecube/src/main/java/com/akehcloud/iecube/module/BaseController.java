package com.akehcloud.iecube.module;

import com.akehcloud.iecube.util.AuthUtils;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

    public Long currentUserId(){
        return AuthUtils.getCurrentUserId();
    }

    public Long currentOrgId(){
        return 0L;
    }

    public Long currentSchoolId(){
        return AuthUtils.getCurrentUserSchoolId();
    }
}
