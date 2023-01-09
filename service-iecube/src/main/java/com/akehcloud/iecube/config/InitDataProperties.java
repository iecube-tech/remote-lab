package com.akehcloud.iecube.config;

import com.akehcloud.iecube.config.model.RoleConfigModel;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "init-data")
public class InitDataProperties {

    private List<RoleConfigModel> schoolRoles;
    private List<RoleConfigModel> organizationRoles;

}
