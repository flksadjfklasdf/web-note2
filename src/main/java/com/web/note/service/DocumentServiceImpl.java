package com.web.note.service;

import com.web.note.dao.DocumentMapper;
import com.web.note.dao.ImageMapper;
import com.web.note.entity.*;
import com.web.note.entity.Collection;
import com.web.note.exception.PageNotFoundException;
import com.web.note.exception.UnauthorizedAccessException;
import com.web.note.handler.request.*;
import com.web.note.security.ExternalUsage;
import com.web.note.security.InternalUsage;
import com.web.note.security.PermissionValidator;
import com.web.note.service.api.AsyncService;
import com.web.note.service.api.CollectionService;
import com.web.note.service.api.DocumentService;
import com.web.note.service.api.ImageService;
import com.web.note.util.RegexUtil;
import com.web.note.util.ResourceUtil;
import com.web.note.util.Security;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

import static com.web.note.constant.DatabaseConstant.*;

@Slf4j
@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    public DocumentMapper documentMapper;
    @Autowired
    public CollectionService collectionService;
    @Autowired
    public PermissionValidator permissionValidator;
    @Autowired
    public AsyncService asyncService;
    @Lazy
    @Autowired
    public ImageService imageService;
    @Autowired
    public ImageMapper imageMapper;

    @Override
    @ExternalUsage
    public boolean create(User user, DocumentCreate document) {

        permissionValidator.validateCollectionOwnership(document.getCollectionId(), user.getUserId());

        Collection collectionById = collectionService.getCollectionById(document.getCollectionId());

        int maxOrder = documentMapper.getMaxOrder(document.getCollectionId());

        if (collectionById == null) {
            throw new RuntimeException("无效的操作!");
        }
        if (!Objects.equals(user.getUserId(), collectionById.getUserId())) {
            throw new RuntimeException("无效的操作!");
        }

        Document document1 = new Document();

        document1.setDocumentId(Security.getUid());
        document1.setCreatedAt(new Date());
        document1.setDocumentContent(CONTENT_EMPTY);
        document1.setUserId(user.getUserId());
        document1.setDocumentType(collectionById.getCollectionType());
        document1.setCollectionId(collectionById.getCollectionId());
        document1.setDocumentName(document.getDocumentName());
        document1.setUpdatedAt(new Date());
        document1.setSortOrder(maxOrder + 1);

        int insert = documentMapper.insert(document1);

        return insert == 1;
    }

    @Override
    @ExternalUsage
    public boolean deleteDocument(User user, DocumentId document) {

        permissionValidator.validateDocumentOwnership(document.documentId, user.getUserId());

        DocumentExample documentExample = new DocumentExample();
        documentExample.createCriteria().andDocumentIdEqualTo(document.getDocumentId());

        imageService.deleteImageInDocument(user, document);

        int i = documentMapper.deleteByExample(documentExample);

        return i == 1;
    }

    @Override
    @ExternalUsage
    public boolean deleteDocumentInCollection(User user, CollectionId collection) {

        permissionValidator.validateCollectionOwnership(collection.collectionId, user.getUserId());

        DocumentExample documentExample = new DocumentExample();
        documentExample.createCriteria().andCollectionIdEqualTo(collection.getCollectionId());

        imageService.deleteImageInCollection(user, collection);

        int i = documentMapper.deleteByExample(documentExample);

        return i >= 0;
    }

    @Override
    @ExternalUsage
    public boolean modify(User user, DocumentModify document) {

        permissionValidator.validateDocumentOwnership(document.documentId, user.getUserId());

        DocumentExample documentExample = new DocumentExample();
        documentExample.createCriteria().andDocumentIdEqualTo(document.getDocumentId());

        Document document1 = new Document();

        document1.setDocumentName(document.getDocumentName());
        document1.setUpdatedAt(new Date());

        int i = documentMapper.updateByExampleSelective(document1, documentExample);


        return i == 1;
    }

    @Override
    @ExternalUsage
    public Document readDocumentById(User user, DocumentId document) {

        DocumentExample documentExample = new DocumentExample();
        documentExample.createCriteria().andDocumentIdEqualTo(document.getDocumentId());

        List<Document> documents = documentMapper.selectByExampleWithBLOBs(documentExample);

        if (documents.isEmpty()) {
            return null;
        }

        Document document1 = documents.get(0);
        Collection collectionById = collectionService.getCollectionById(document1.getCollectionId());


        if (collectionById.getIsPublic() == COLLECTION_PUBLIC) {
            return document1;
        } else {
            if (user != null && Objects.equals(document1.getUserId(), user.getUserId())) {
                return document1;
            } else {
                throw new UnauthorizedAccessException("该文档是非公开的,你无权访问");
            }
        }
    }

    @Override
    @ExternalUsage
    public List<Document> getDocumentTitleAll(User user, CollectionId collection) {

        DocumentExample documentExample = new DocumentExample();
        documentExample.createCriteria()
                .andCollectionIdEqualTo(collection.getCollectionId());

        documentExample.setOrderByClause(" sort_order ASC ");

        Collection collectionDB = collectionService.getCollectionById(collection.getCollectionId());

        if (collectionDB == null) {
            throw new PageNotFoundException("你访问的页面不存在");
        }

        if (Objects.equals(collectionDB.getUserId(), user.getUserId()) || collectionDB.getIsPublic()) {
            List<Document> documents = documentMapper.selectByExample(documentExample);
            documents.forEach(this::nullifyDocumentFields);
            return documents;
        } else {
            throw new UnauthorizedAccessException("该文档是非公开的,你无权访问");
        }
    }

    @Override
    @ExternalUsage
    public boolean saveDocument(User user, DocumentSave document) {

        DocumentExample documentExample = new DocumentExample();
        documentExample.createCriteria().andUserIdEqualTo(user.getUserId())
                .andDocumentIdEqualTo(document.getDocumentId());

        Document document1 = new Document();

        document1.setDocumentContent(document.getDocumentContent());
        document1.setUpdatedAt(new Date());

        int i = documentMapper.updateByExampleSelective(document1, documentExample);

        ((DocumentServiceImpl) AopContext.currentProxy()).analyseResource(user.getUserId(), document.getDocumentId(), document.getDocumentContent());

        return i == 1;
    }

    @Override
    @ExternalUsage
    @Transactional()
    public boolean exchange(User user, String documentId1, String documentId2) {
        permissionValidator.validateDocumentOwnership(documentId1, user.getUserId());
        permissionValidator.validateDocumentOwnership(documentId2, user.getUserId());

        Document document1 = documentMapper.selectByPrimaryKey(documentId1);
        Document document2 = documentMapper.selectByPrimaryKey(documentId2);


        Document record1 = new Document();
        record1.setDocumentId(documentId1);
        record1.setSortOrder(document2.getSortOrder());
        documentMapper.updateByPrimaryKeySelective(record1);

        Document record2 = new Document();
        record2.setDocumentId(documentId2);
        record2.setSortOrder(document1.getSortOrder());
        documentMapper.updateByPrimaryKeySelective(record2);

        return true;
    }

    @Override
    @InternalUsage
    public void createDefaultDocument(Collection collection1) {
        List<Document> documents = new ArrayList<>();

        documents.add(getDefaultDocument("tutorial1.md", "md编辑器教程1", collection1));
        documents.add(getDefaultDocument("tutorial2.md", "md编辑器教程2", collection1));

        for (int i = 0; i < documents.size(); i++) {
            documents.get(i).setSortOrder(i);
        }

        for (Document document : documents) {
            documentMapper.insert(document);
        }
    }

    @Override
    public List<Document> getDocumentsByCollectionId(String collectionId) {
        DocumentExample documentExample = new DocumentExample();
        documentExample.createCriteria().andCollectionIdEqualTo(collectionId);
        documentExample.setOrderByClause(" sort_order ASC ");
        return documentMapper.selectByExampleWithBLOBs(documentExample);
    }


    @Override
    @InternalUsage
    public Document getDocumentById(String documentId) {

        DocumentExample documentExample = new DocumentExample();
        documentExample.createCriteria().andDocumentIdEqualTo(documentId);
        List<Document> documents = documentMapper.selectByExampleWithBLOBs(documentExample);

        if (documents != null && documents.size() == 1) {
            return documents.get(0);
        } else if (documents == null || documents.isEmpty()) {
            return null;
        } else {
            throw new RuntimeException("返回了多个结果!!!");
        }
    }


    private void nullifyDocumentFields(Document document) {
        document.setCreatedAt(null);
        document.setDocumentContent(null);
    }

    private Document getDefaultDocument(String path, String name, Collection collection) {
        try {
            String content = ResourceUtil.readFileContent("res/" + path);
            Document document = new Document();

            document.setUserId(collection.getUserId());
            document.setCollectionId(collection.getCollectionId());
            document.setDocumentName(name);
            document.setUpdatedAt(new Date());
            document.setCreatedAt(new Date());
            document.setDocumentType(collection.getCollectionType());
            document.setDocumentId(Security.getUid());
            document.setDocumentContent(content);

            return document;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Async("async")
    public void analyseResource(String userId, String documentId, String content) {
        List<String> documentImageIds = RegexUtil.extractImageId(content);
        Set<String> stringSet = new HashSet<>(documentImageIds);

        ImageExample example = new ImageExample();
        example.createCriteria()
                .andUserIdEqualTo(userId)
                .andDocumentIdEqualTo(documentId);
        List<Image> images = imageMapper.selectByExample(example);

        images.forEach(s -> {
            if (stringSet.contains(s.getImageId()) && s.getPendingDeleteAt() != null) {
                s.setPendingDeleteAt(null);
                imageMapper.updateByPrimaryKey(s);
            }
            if (!stringSet.contains(s.getImageId())) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 1);
                Date dateAfterOneDay = calendar.getTime();

                if (s.getPendingDeleteAt() == null) {
                    s.setPendingDeleteAt(dateAfterOneDay);
                    imageMapper.updateByPrimaryKey(s);
                }
            }

        });

    }

}
