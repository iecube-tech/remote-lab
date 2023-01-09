package com.akehcloud.iecube.module.resource.controller;

import com.akehcloud.iecube.module.resource.dto.ResourceDTO;
import com.akehcloud.iecube.module.resource.service.ResourceService;
import com.akehcloud.iecube.util.DownloadUtils;
import com.akehcloud.util.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 控制层
 *
 * @author wangyaxing
 * @date 2021-05-19
 */
@RestController
@RequestMapping("/resource")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @Value("${resource-location}")
    private String location;


    @PostMapping
    public ResourceDTO upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String key = this.key(originalFilename);
        File target = new File(this.location, key);
        if (!target.exists()) {
            File parentFile = target.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            boolean success = target.createNewFile();
            AssertUtils.state(success, "创建文件失败");
        }
        FileCopyUtils.copy(file.getBytes(), target);
        resourceService.save(this.buildResourceDTO(key, originalFilename));
        return resourceService.get(key);
    }

    private ResourceDTO buildResourceDTO(String key, String originFilename) {
        ResourceDTO resource = new ResourceDTO();
        resource.setOriginFilename(originFilename);
        resource.setFilename(originFilename);
        resource.setKey(key);
        return resource;
    }

    @GetMapping("/{key}")
    public void download(@PathVariable String key, HttpServletResponse response) {
        ResourceDTO resource = resourceService.get(key);
        DownloadUtils.httpDownload(new File(this.location, resource.getKey()), resource.getFilename(), response);
    }

    private String key(String originFilename) {
        return DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss").format(LocalDateTime.now()) + "_" + originFilename;
    }

}
