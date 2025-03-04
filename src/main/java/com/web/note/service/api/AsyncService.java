package com.web.note.service.api;

import com.web.note.entity.FileResource;
import com.web.note.entity.Image;
import com.web.note.security.InternalUsage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AsyncService {
    void deleteImages(List<Image> images);

    void deleteFile(List<FileResource> fileResource);

    void deleteUserData(List<String> userIds);

    @Transactional
    @Async("async")
    void deleteImagesByCollectionId(String collectionId);

    @Transactional
    @Async("async")
    @InternalUsage
    void deleteImagesByDocumentId(String documentId);
}
