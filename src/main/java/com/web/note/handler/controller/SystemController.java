package com.web.note.handler.controller;

import com.web.note.exception.InvalidInputException;
import com.web.note.handler.response.FileTypeVO;
import com.web.note.handler.response.ResultEntity;
import com.web.note.security.RequireRole;
import com.web.note.security.Role;
import com.web.note.service.api.SystemConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Validated
@RestController()
@RequestMapping("/system")
public class SystemController {

    @Autowired
    SystemConfigService systemConfigService;

    @RequireRole(Role.SYSTEM_ADMIN)
    @PostMapping("/config/fileTypes/query")
    public ResultEntity<List<FileTypeVO>> queryFileTypeData(){
        List<FileTypeVO> data = systemConfigService.queryFileTypes();
        return ResultEntity.resultOKWithData(data);
    }

    @RequireRole(Role.SYSTEM_ADMIN)
    @PostMapping("/config/fileTypes/update")
    public ResultEntity<?> updateFileTypeData(@RequestBody @Validated List<FileTypeVO> fileTypeVOS) throws InvalidInputException {

        if(fileTypeVOS == null){
            throw new InvalidInputException();
        }
        boolean b = fileTypeVOS.stream().allMatch(s -> s.getTypeField() != null && s.getTypeField().length() > 0 && s.getTypeField().length() < 200 &&
                s.getContentType() != null && s.getContentType().length() > 0 && s.getContentType().length() < 200 &&
                s.getTypeName() != null && s.getTypeName().length() > 0 && s.getTypeName().length() < 200);

        if(!b){
            throw new InvalidInputException("参数值不能为空,且长度不能大于200字符");
        }


        systemConfigService.updateFileTypes(fileTypeVOS);
        return ResultEntity.resultOK();
    }


}
