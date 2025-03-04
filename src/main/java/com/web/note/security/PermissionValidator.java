package com.web.note.security;

import com.web.note.dao.CollectionMapper;
import com.web.note.dao.DocumentMapper;
import com.web.note.dao.FileResourceMapper;
import com.web.note.dao.ImageMapper;
import com.web.note.entity.*;
import com.web.note.exception.AuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class PermissionValidator {

    @Autowired
    public DocumentMapper documentMapper;
    @Autowired
    public CollectionMapper collectionMapper;
    @Autowired
    public ImageMapper imageMapper;
    @Autowired
    public FileResourceMapper fileResourceMapper;

    public void validateDocumentOwnership(String documentId, String userId) {
        Document document = documentMapper.selectByPrimaryKey(documentId);

        if (document == null) {
            return;
        }
        if (!document.getUserId().equals(userId)) {
            throw new AuthorizationException("User does not have permission to access the document.");
        }
    }

    public void validateCollectionOwnership(String collectionId, String userId) {
        Collection collection = collectionMapper.selectByPrimaryKey(collectionId);

        if (collection == null) {
            return;
        }
        if (!collection.getUserId().equals(userId)) {
            throw new AuthorizationException("User does not have permission to access the collection.");
        }
    }

    public void validateImageOwnership(String imageId, String userId) {
        Image image = imageMapper.selectByPrimaryKey(imageId);

        if (image == null) {
            return;
        }
        if (!image.getUserId().equals(userId)) {
            throw new AuthorizationException("User does not have permission to access the image.");
        }
    }

    public void validateFileResourceOwnership(String fileResourceId, String userId) {
        FileResource fileResource = fileResourceMapper.selectByPrimaryKey(fileResourceId);

        if (fileResource == null) {
            return;
        }
        if (!fileResource.getUserId().equals(userId)) {
            throw new AuthorizationException("User does not have permission to access the file resource.");
        }
    }


}
