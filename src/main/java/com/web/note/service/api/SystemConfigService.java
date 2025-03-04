package com.web.note.service.api;

import com.web.note.handler.response.FileTypeVO;

import java.util.List;

public interface SystemConfigService {
    List<FileTypeVO> queryFileTypes();

    void updateFileTypes(List<FileTypeVO> fileTypeVOS);
}
