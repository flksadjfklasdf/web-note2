package com.web.note.cache;

import com.web.note.handler.response.FileTypeVO;
import com.web.note.service.api.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Component
public class FileTypeCache {
    private static final FileTypeCache fileTypeCache= new FileTypeCache();

    private List<FileTypeVO> fileTypeVOS;

    @Autowired
    SystemConfigService systemConfigService;

    private FileTypeCache(){

    }
    public synchronized String getContentType(String fileTypeField){
        Optional<String> first = fileTypeVOS.stream().filter(s -> s.getTypeField().equals(fileTypeField)).map(FileTypeVO::getContentType).findFirst();
        return first.orElse(null);
    }
    public synchronized String getFileTypeName(String fileTypeField){
        Optional<String> first = fileTypeVOS.stream().filter(s -> s.getTypeField().equals(fileTypeField)).map(FileTypeVO::getTypeName).findFirst();
        return first.orElse(null);
    }
    public synchronized void update(List<FileTypeVO> fileTypeVOS){
        this.fileTypeVOS = fileTypeVOS;
    }
    public synchronized List<FileTypeVO> getAll(){
        return fileTypeVOS;
    }

    public static FileTypeCache getInstance(){
        return fileTypeCache;
    }

    @PostConstruct
    public void init(){
        List<FileTypeVO> fileTypeVOS = systemConfigService.queryFileTypes();
        FileTypeCache.getInstance().update(fileTypeVOS);
    }
}
