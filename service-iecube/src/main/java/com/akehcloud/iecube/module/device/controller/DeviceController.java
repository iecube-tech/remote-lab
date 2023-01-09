package com.akehcloud.iecube.module.device.controller;

import com.akehcloud.iecube.module.BaseController;
import com.akehcloud.iecube.module.device.dto.DeviceDTO;
import com.akehcloud.iecube.module.device.qo.DeviceQO;
import com.akehcloud.iecube.module.device.service.DeviceService;
import com.akehcloud.iecube.util.AuthUtils;
import com.akehcloud.model.PageTuple;
import com.akehcloud.model.ResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/device")
public class DeviceController extends BaseController {

    @Value("${business.signal-server.url}")
    private String signalServerUrl;

    @Autowired
    private DeviceService deviceService;

    @PostMapping
    public void save(@RequestBody DeviceDTO dto) {
        dto.setCreatorId(AuthUtils.getCurrentUserId());
        dto.setLastOperatorId(AuthUtils.getCurrentUserId());
        dto.setSchoolId(currentSchoolId());
        deviceService.save(dto);
    }

    @PutMapping
    public void modify(@RequestBody DeviceDTO dto) {
        dto.setLastOperatorId(currentUserId());
        deviceService.modify(dto);
    }

    @PostMapping("/query")
    public PageTuple<DeviceDTO> query(@RequestBody DeviceQO qo) {
        qo.setSchoolId(AuthUtils.getCurrentUserSchoolId());
        qo.setCreatorId(AuthUtils.getCurrentUserId());
        return deviceService.query(qo);
    }

    @GetMapping("/{id}")
    public DeviceDTO get(@PathVariable Long id) {
        return deviceService.get(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        deviceService.delete(id);
    }

    @GetMapping(value = "/signal-server-url")
    public ResultModel<String> getSignalServerUrl() {
        return ResultModel.model(this.signalServerUrl);
    }

}
