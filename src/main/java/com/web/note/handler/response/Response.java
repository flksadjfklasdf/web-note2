package com.web.note.handler.response;

import com.web.note.cache.FileTypeCache;
import com.web.note.entity.FileResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.web.note.constant.DatabaseConstant.*;

public class Response {

    public static ResponseEntity<Resource> fileResource(FileResource fileResource, Resource resource) {

        String encodedFilename = null;
        try {
            String originalFilename = fileResource.getOriginalFilename();
            encodedFilename = URLEncoder.encode(originalFilename, "UTF-8")
                    .replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String contentType = FileTypeCache.getInstance().getContentType(fileResource.getFileType());

        if (contentType == null) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename + "\"")
                    .body(resource);
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(resource);

    }
}
