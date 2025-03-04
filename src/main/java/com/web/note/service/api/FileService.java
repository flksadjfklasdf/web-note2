package com.web.note.service.api;

import com.web.note.entity.FileResource;
import com.web.note.entity.User;
import com.web.note.handler.request.FileSearchParam;
import com.web.note.handler.response.ResourceInfo;
import com.web.note.handler.response.SearchFileEntity;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public interface FileService {
    int upload(User user, MultipartFile file, boolean isPublic, Boolean isAuthCode, String authCode, String fileNote, String fileType) throws IOException;

    ResponseEntity<Resource> getFile(HttpSession session, HttpServletRequest request,String fileId, String authCode);

    SearchFileEntity searchFiles(User user, FileSearchParam fileSearchParam);

    int modifyFile(User user,String fileId, Boolean isPublic,Boolean isAuthCode,String authCode,String fileNote,String fileType);

    int deleteFile(User user, FileResource fileResource);

    ResourceInfo getResourceInfo(User user);
}
