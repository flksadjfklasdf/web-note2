package com.web.note.service.api;

import com.web.note.entity.Image;
import com.web.note.entity.User;
import com.web.note.exception.InvalidInputException;
import com.web.note.handler.request.CollectionId;
import com.web.note.handler.request.DocumentId;
import com.web.note.handler.request.ImageId;
import com.web.note.handler.response.ImageCollection;
import com.web.note.handler.response.ImageDocument;
import com.web.note.handler.response.ImageInfo;
import com.web.note.handler.response.ImageUploadResult;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface ImageService {
    ImageUploadResult uploadImage(User user, MultipartFile file, String documentId) throws InvalidInputException;

    ResponseEntity<Resource> getImageById(ImageId imageId);

    List<ImageCollection> getAllCollectionsImages(User user);

    List<ImageDocument> getCollectionImages(User user, CollectionId collectionId);

    List<ImageInfo> getDocumentImages(User user, DocumentId documentId);

    int deleteImageInCollection(User user, CollectionId collectionId);

    int deleteImageInDocument(User user, DocumentId documentId);

    int deleteImageById(User user, ImageId imageId);
}
