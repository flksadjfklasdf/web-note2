package com.web.note.handler.controller;

import com.web.note.entity.User;
import com.web.note.handler.request.CollectionId;
import com.web.note.handler.request.DeleteImageParam;
import com.web.note.handler.request.DocumentId;
import com.web.note.handler.request.ImageId;
import com.web.note.handler.response.*;
import com.web.note.handler.valid.IdValid;
import com.web.note.security.RequireRole;
import com.web.note.security.Role;
import com.web.note.service.api.CollectionService;
import com.web.note.service.api.ImageService;
import com.web.note.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;


import java.util.List;

import static com.web.note.constant.StatusConstant.*;

@Validated
@RestController
@RequestMapping({"/image","/i"})
public class ImageController {

    @Autowired
    public ImageService imageService;
    @Autowired
    public CollectionService collectionService;


    @RequireRole(Role.USER)
    @RequestMapping("/upload")
    public ResultEntity<ImageUploadResult> uploadFile(@RequestParam("image") MultipartFile file, HttpSession httpSession, @RequestParam("documentId") String docuemtnId) {

        ResultEntity<ImageUploadResult> result = new ResultEntity<>();

        try {
            User user = SessionUtil.getUser(httpSession);

            ImageUploadResult ir = imageService.uploadImage(user,file, docuemtnId);
            result.setStatus(OK);
            result.setData(ir);
            return result;
        } catch (Exception e) {
            result.setStatus(SERVICE_ERROR);
            result.setMessage(e.getMessage());
            return result;
        }
    }

    @Deprecated
    @GetMapping("/getImage")
    public ResponseEntity<Resource> getImage(@RequestParam @IdValid String imageId) {

        ImageId imageId1=new ImageId();
        imageId1.setImageId(imageId);

        return imageService.getImageById(imageId1);
    }
    @GetMapping("/g/{imageId}")
    public ResponseEntity<Resource> getImage2(@PathVariable @IdValid String imageId) {

        ImageId imageId1=new ImageId();
        imageId1.setImageId(imageId);

        return imageService.getImageById(imageId1);
    }

    @RequireRole(Role.USER)
    @RequestMapping("/getAllCollectionsImages")
    public ResultEntity<List<ImageCollection>> getImages(HttpSession session) {

        User user = SessionUtil.getUser(session);

        List<ImageCollection> imageCollections = imageService.getAllCollectionsImages(user);

        ResultEntity<List<ImageCollection>> entity = new ResultEntity<>();

        entity.setData(imageCollections);

        return entity;
    }
    @RequireRole(Role.USER)
    @RequestMapping("/getImagesInCollection")
    public ResultEntity<List<ImageDocument>> getImagesInCollection(HttpSession session, @RequestBody @Validated  CollectionId id){

        User user = SessionUtil.getUser(session);

        List<ImageDocument> imageCollections = imageService.getCollectionImages(user,id);

        ResultEntity<List<ImageDocument>> entity = new ResultEntity<>();

        entity.setData(imageCollections);

        return entity;
    }
    @RequireRole(Role.USER)
    @RequestMapping("/getImagesInDocument")
    public ResultEntity<List<ImageInfo>> getImagesInDocument(HttpSession session, @RequestBody @Validated  DocumentId id){

        User user = SessionUtil.getUser(session);

        List<ImageInfo> imageCollections = imageService.getDocumentImages(user,id);

        ResultEntity<List<ImageInfo>> entity = new ResultEntity<>();

        entity.setData(imageCollections);

        return entity;
    }
    @RequireRole(Role.USER)
    @RequestMapping("/deleteImage")
    public ResultEntity<Object> deleteImage(HttpSession session, @RequestBody @Validated  DeleteImageParam param){

        int result;

        User user = SessionUtil.getUser(session);

        ResultEntity<Object> entity=new ResultEntity<>();

        if(param.type==1){
            CollectionId collectionId=new CollectionId();
            collectionId.setCollectionId(param.getItemId());

            result=imageService.deleteImageInCollection(user,collectionId);
        } else if (param.type==2) {
            DocumentId documentId=new DocumentId();
            documentId.setDocumentId(param.getItemId());

            result=imageService.deleteImageInDocument(user,documentId);
        } else {
            ImageId imageId=new ImageId();
            imageId.setImageId(param.getItemId());

            result=imageService.deleteImageById(user,imageId);
        }

        if(result!=0){
            entity.setStatus(OK);
        }else{
            entity.setStatus(DO_NOTHING);
        }
        return entity;

    }
}
