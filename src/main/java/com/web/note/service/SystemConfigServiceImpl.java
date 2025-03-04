package com.web.note.service;

import com.web.note.cache.FileTypeCache;
import com.web.note.dao.FileTypeMapper;
import com.web.note.entity.FileType;
import com.web.note.entity.FileTypeExample;
import com.web.note.handler.response.FileTypeVO;
import com.web.note.service.api.SystemConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.web.note.util.Security.getUid;

@Slf4j
@Service
public class SystemConfigServiceImpl implements SystemConfigService {

    @Autowired
    FileTypeMapper fileTypeMapper;

    @Override
    public List<FileTypeVO> queryFileTypes() {
        FileTypeExample example = new FileTypeExample();
        example.setOrderByClause(" type_name_en ASC");
        List<FileType> fileTypes = fileTypeMapper.selectByExample(example);
        return fileTypes.stream().map(s -> new FileTypeVO(s.getTypeNameEn(),s.getTypeNameCn() , s.getContentType())).collect(Collectors.toList());
    }

    @Override
    public void updateFileTypes(List<FileTypeVO> fileTypeVOS) {
        if(fileTypeVOS != null){
            FileTypeExample example = new FileTypeExample();
            fileTypeMapper.deleteByExample(example);

            List<FileType> collect = fileTypeVOS.stream().map(s -> new FileType(getUid(), s.getTypeField(), s.getTypeName(), s.getContentType())).collect(Collectors.toList());
            collect.forEach(s -> fileTypeMapper.insert(s));

            FileTypeCache.getInstance().update(fileTypeVOS);
        }
    }
}
