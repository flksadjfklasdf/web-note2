package com.web.note.service;

import com.web.note.business.UserBusiness;
import com.web.note.dao.CollectionMapper;
import com.web.note.dao.DocumentMapper;
import com.web.note.dao.ImageMapper;
import com.web.note.entity.*;
import com.web.note.entity.Collection;
import com.web.note.exception.UploadException;
import com.web.note.handler.request.CollectionId;
import com.web.note.handler.request.DocumentId;
import com.web.note.handler.request.ImageId;
import com.web.note.handler.response.ImageCollection;
import com.web.note.handler.response.ImageDocument;
import com.web.note.handler.response.ImageInfo;
import com.web.note.handler.response.ImageUploadResult;
import com.web.note.security.ExternalUsage;
import com.web.note.service.api.AsyncService;
import com.web.note.service.api.CollectionService;
import com.web.note.service.api.DocumentService;
import com.web.note.service.api.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.web.note.util.Security.getUid;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    @Value("${img.save.path}")
    public String savePath;


    @Autowired
    public ImageMapper imageMapper;
    @Autowired
    public DocumentMapper documentMapper;
    @Autowired
    public CollectionMapper collectionMapper;


    @Autowired
    public DocumentService documentService;
    @Autowired
    public CollectionService collectionService;
    @Autowired
    public AsyncService asyncService;
    @Autowired
    public UserBusiness userBusiness;

    @Override
    @ExternalUsage
    public ImageUploadResult uploadImage(User user, MultipartFile file, String documentId) {


        Document documentById = documentService.getDocumentById(documentId);

        if (documentById == null) {
            throw new RuntimeException("无效的操作!");
        }
        if (!Objects.equals(user.getUserId(), documentById.getUserId())) {
            throw new RuntimeException("无效的操作!");
        }

        Image image = new Image();

        String uid = getUid();
        image.setImageId(uid);


        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null) {
            throw new RuntimeException("无法获取上传文件的文件名");
        }

        String fileExtension = getFileExtension(originalFilename);
        String newFileName = image.getImageId() + "." + fileExtension;


        image.setImageName(newFileName);
        int size = (int) ((double) file.getSize() / 1024);
        image.setImageSize(size);
        image.setCollectionId(documentById.getCollectionId());
        image.setDocumentId(documentById.getDocumentId());
        image.setCreatedAt(new Date());
        image.setUserId(user.getUserId());

        //校验空间容量
///        int freeSpace = userBusiness.getAvailableSpace(user);
///        if (size > freeSpace) {
///            throw new UploadException("剩余可用空间不足!!!");
///        }

        imageMapper.insert(image);
        saveFile(file,image);

        ImageUploadResult imageUploadResult = new ImageUploadResult();
        imageUploadResult.setImageId(uid);

        return imageUploadResult;
    }

    @Override
    @ExternalUsage
    public ResponseEntity<Resource> getImageById(ImageId id) {

        Image image = imageMapper.selectByPrimaryKey(id.getImageId());

        if (image == null) {
            return ResponseEntity.notFound().build();
        }

        Path imagePath = Paths.get(savePath).resolve(image.getImageName());

        Resource resource = new FileSystemResource(imagePath.toFile());

        CacheControl cacheControl = CacheControl.maxAge(365, TimeUnit.DAYS);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE,"image/png")
                .cacheControl(cacheControl)
                .body(resource);
    }

    @Override
    @ExternalUsage
    public List<ImageCollection> getAllCollectionsImages(User user) {

        List<Collection> allCollectionByUserId = collectionMapper.getAllCollectionByUserIdHavingImage(user.getUserId());

        List<ImageCollection> imageCollections = new ArrayList<>();

        for (Collection collection : allCollectionByUserId) {
            ImageCollection imageCollection = new ImageCollection();
            imageCollection.setCollectionId(collection.getCollectionId());
            imageCollection.setCollectionName(collection.getCollectionName());
            imageCollection.setSizeCount(imageMapper.getSumSizeInCollection(collection.getCollectionId()));
            imageCollection.setImageCount(imageMapper.countInCollection(collection.getCollectionId()));

            imageCollections.add(imageCollection);
        }

        return imageCollections;
    }

    @Override
    @ExternalUsage
    public List<ImageDocument> getCollectionImages(User user, CollectionId  id) {

        List<Document> allCollectionByUserId = documentMapper.getDocumentsByUserIdHavingImage(user.getUserId(),id.getCollectionId());

        List<ImageDocument> imageDocuments = new ArrayList<>();

        for (Document document : allCollectionByUserId) {
            ImageDocument imageDocument = new ImageDocument();
            imageDocument.setDocumentId(document.getDocumentId());
            imageDocument.setDocumentName(document.getDocumentName());
            imageDocument.setSizeCount(imageMapper.getSumSizeInDocument(document.getDocumentId()));
            imageDocument.setImageCount(imageMapper.countInDocument(document.getDocumentId()));

            imageDocuments.add(imageDocument);
        }

        return imageDocuments;
    }

    @Override
    @ExternalUsage
    public List<ImageInfo> getDocumentImages(User user,DocumentId id) {

        List<Image> imagesId = imageMapper.getImagesByUserIdHavingImage(user.getUserId(),id.getDocumentId());

        List<ImageInfo> imageDocuments = new ArrayList<>();

        for (Image image : imagesId) {
            ImageInfo imageInfo = new ImageInfo();
            imageInfo.setImageId(image.getImageId());
            imageInfo.setImageName(image.getImageName());
            imageInfo.setImageCreateTime(image.getCreatedAt().toString());
            imageInfo.setSize(image.getImageSize());

            imageDocuments.add(imageInfo);
        }

        return imageDocuments;
    }
    /**
     * !!!数据库删除
     */
    @Override
    @ExternalUsage
    public int deleteImageInCollection(User user, CollectionId id) {

        ImageExample imageExample=new ImageExample();
        imageExample.createCriteria().andCollectionIdEqualTo(id.getCollectionId())
                .andUserIdEqualTo(user.getUserId());

        List<Image> images = imageMapper.selectByExample(imageExample);
        asyncService.deleteImages(images);


        return imageMapper.deleteByExample(imageExample);

    }
    /**
     * !!!数据库删除
     */
    @Override
    @ExternalUsage
    public int deleteImageInDocument(User user, DocumentId id) {

        ImageExample imageExample=new ImageExample();
        imageExample.createCriteria().andDocumentIdEqualTo(id.getDocumentId())
                .andUserIdEqualTo(user.getUserId());

        List<Image> images = imageMapper.selectByExample(imageExample);

        asyncService.deleteImages(images);

        return imageMapper.deleteByExample(imageExample);
    }

    /**
     * !!!数据库删除
     */
    @Override
    @ExternalUsage
    public int deleteImageById(User user, ImageId id) {

        ImageExample imageExample=new ImageExample();
        imageExample.createCriteria().andImageIdEqualTo(id.getImageId())
                .andUserIdEqualTo(user.getUserId());

        List<Image> images = imageMapper.selectByExample(imageExample);

        asyncService.deleteImages(images);

        return imageMapper.deleteByExample(imageExample);
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return filename.substring(lastDotIndex + 1);
        }
        return "no.jpg";
    }

    private void saveFile(MultipartFile file, Image image) {
        if (!file.isEmpty()) {
            try {
                Path filePath = Paths.get(savePath).resolve(image.getImageName());

                File dest = filePath.toFile();

                file.transferTo(dest);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 每天0点清理无效的图片内容
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupInvalidImages() {

        Map<String, Object> params = new HashMap<>();
        params.put("now", new Date());

        List<Image> invalidImages = imageMapper.getAllInvalidImage(params);

        if (invalidImages != null && !invalidImages.isEmpty()) {

            Map<String, Object> deleteParams = new HashMap<>();
            deleteParams.put("imageIds",invalidImages.stream().map(Image::getImageId).collect(Collectors.toList()));
            imageMapper.batchDelete(deleteParams);
            log.info("Successfully deleted " + invalidImages.size() + " invalid images.");
        } else {
            log.info("No invalid images found for deletion.");
        }
    }
}
