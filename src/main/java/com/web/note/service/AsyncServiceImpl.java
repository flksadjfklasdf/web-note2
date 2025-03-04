package com.web.note.service;

import com.web.note.dao.*;
import com.web.note.entity.*;
import com.web.note.security.InternalUsage;
import com.web.note.service.api.AsyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Service
public class AsyncServiceImpl implements AsyncService {

    @Value("${img.save.path}")
    public String imagePath;

    @Value("${file.save.path}")
    public String filePath;


    @Autowired
    public CollectionMapper collectionMapper;
    @Autowired
    public DocumentMapper documentMapper;
    @Autowired
    public ImageMapper imageMapper;
    @Autowired
    public FileResourceMapper fileResourceMapper;
    @Autowired
    public UserConfigMapper userConfigMapper;


    @Override
    @Transactional
    @Async("async")
    @InternalUsage
    public void deleteImages(List<Image> images) {
        for (Image image : images) {
            String imageName = image.getImageName();
            Path imagePath = Paths.get(this.imagePath, imageName);

            try {
                if (Files.exists(imagePath)) {
                    Files.delete(imagePath);
                    log.info("删除文件成功: {}", imagePath);
                } else {
                    log.error("该文件找不到: {}", imagePath);
                }
            } catch (IOException e) {
                log.error("删除文件时发生错误:{}", imagePath, e);
            }
        }
    }

    @Override
    @Transactional
    @Async("async")
    @InternalUsage
    public void deleteFile(List<FileResource> fileResources) {
        for (FileResource fileResource : fileResources) {
            String fileName = fileResource.getLocalFilename();
            Path imagePath = Paths.get(this.filePath, fileName);

            try {
                if (Files.exists(imagePath)) {
                    Files.delete(imagePath);
                    log.info("删除文件成功: {}", imagePath);
                } else {
                    log.error("该文件找不到: {}", imagePath);
                }
            } catch (IOException e) {
                log.error("删除文件时发生错误:{}", imagePath, e);
            }
        }
    }

    @Override
    @Transactional
    @Async("async")
    @InternalUsage
    public void deleteUserData(List<String> userIds) {
        for (String userId : userIds) {
            deleteUserCollections(userId);
            deleteUserDocuments(userId);
            deleteUserImages(userId);
            deleteUserFiles(userId);
            deleteUserConfigs(userId);
        }
    }

    @Override
    @Transactional
    @Async("async")
    @InternalUsage
    public void deleteImagesByCollectionId(String collectionId) {
        ImageExample imageExample = new ImageExample();
        imageExample.createCriteria().andCollectionIdEqualTo(collectionId);

        List<Image> images = imageMapper.selectByExample(imageExample);

        deleteImages(images);

        imageMapper.deleteByExample(imageExample);
    }
    @Override
    @Transactional
    @Async("async")
    @InternalUsage
    public void deleteImagesByDocumentId(String documentId) {
        ImageExample imageExample = new ImageExample();
        imageExample.createCriteria().andDocumentIdEqualTo(documentId);

        List<Image> images = imageMapper.selectByExample(imageExample);

        deleteImages(images);

        imageMapper.deleteByExample(imageExample);
    }




    @Transactional
    public void deleteUserCollections(String userId){

        CollectionExample collectionExample=new CollectionExample();
        collectionExample.createCriteria().andUserIdEqualTo(userId);

        collectionMapper.deleteByExample(collectionExample);
    }
    @Transactional
    public void deleteUserDocuments(String userId){

        DocumentExample documentExample=new DocumentExample();
        documentExample.createCriteria().andUserIdEqualTo(userId);

        documentMapper.deleteByExample(documentExample);
    }
    @Transactional
    public void deleteUserImages(String userId){
        ImageExample imageExample = new ImageExample();
        imageExample.createCriteria().andUserIdEqualTo(userId);

        List<Image> images = imageMapper.selectByExample(imageExample);

        deleteImages(images);

        imageMapper.deleteByExample(imageExample);
    }
    @Transactional
    public void deleteUserFiles(String userId){
        FileResourceExample fileResourceExample = new FileResourceExample();
        fileResourceExample.createCriteria().andUserIdEqualTo(userId);

        List<FileResource> fileResources = fileResourceMapper.selectByExample(fileResourceExample);

        deleteFile(fileResources);


        fileResourceMapper.deleteByExample(fileResourceExample);
    }
    @Transactional
    public void deleteUserConfigs(String userId){
        UserConfigExample userConfigExample = new UserConfigExample();
        userConfigExample.createCriteria().andUserIdEqualTo(userId);

        userConfigMapper.deleteByExample(userConfigExample);
    }
    @Async("async")
    public void asyncDo(Runnable runnable){
        runnable.run();
    }
}
