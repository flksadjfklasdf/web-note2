package com.web.note.service;

import com.web.note.dao.CollectionMapper;
import com.web.note.entity.*;
import com.web.note.exception.UnauthorizedAccessException;
import com.web.note.handler.request.CollectionCreate;
import com.web.note.handler.request.CollectionId;
import com.web.note.handler.request.CollectionModify;
import com.web.note.handler.request.UserId;
import com.web.note.handler.response.CollectionFile;
import com.web.note.handler.response.DocumentFile;
import com.web.note.security.ExternalUsage;
import com.web.note.security.InternalUsage;
import com.web.note.security.PermissionValidator;
import com.web.note.service.api.CollectionService;
import com.web.note.service.api.DocumentService;
import com.web.note.util.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.web.note.constant.DatabaseConstant.*;
import static com.web.note.constant.PropertiesName.USER_NAME;

@Service
public class CollectionServiceImpl implements CollectionService {

    @Autowired
    public CollectionMapper collectionMapper;
    @Autowired
    public DocumentService documentService;
    @Autowired
    public PermissionValidator permissionValidator;


    @Override
    @ExternalUsage
    public boolean createCollection(User user, CollectionCreate collection) {

        Collection collection1=new Collection();

        collection1.setCollectionId(Security.getUid());
        collection1.setCreatedAt(new Date());
        collection1.setUserId(user.getUserId());

        collection1.setCollectionType(collection.getCollectionType());
        collection1.setCollectionName(collection.getCollectionName());
        collection1.setCollectionDescription(collection.getCollectionDescription());
        collection1.setIsPublic(collection.getIsPublic());

        int insert = collectionMapper.insert(collection1);

        return insert ==1;
    }

    @Override
    @ExternalUsage
    public boolean deleteCollection(User user, CollectionId collection){

        permissionValidator.validateCollectionOwnership(collection.collectionId, user.getUserId());

        String collectionId = collection.getCollectionId();

        CollectionExample collectionExample=new CollectionExample();

        collectionExample.createCriteria(). andCollectionIdEqualTo(collectionId);

        documentService.deleteDocumentInCollection(user,collection);

        int i = collectionMapper.deleteByExample(collectionExample);

        return i != 0;
    }

    @Override
    @ExternalUsage
    public boolean modifyCollection(User user, CollectionModify collection){

        permissionValidator.validateCollectionOwnership(collection.collectionId, user.getUserId());

        String collectionId = collection.getCollectionId();

        CollectionExample collectionExample=new CollectionExample();

        collectionExample.createCriteria().andCollectionIdEqualTo(collectionId);

        Collection target=new Collection();

        target.setCollectionDescription(collection.getCollectionDescription());
        target.setCollectionName(collection.getCollectionName());
        target.setIsPublic(collection.getIsPublic());


        int i = collectionMapper.updateByExampleSelective(target,collectionExample);

        return i != 0;
    }

    @Override
    @ExternalUsage
    public List<Collection> getAllCollection(User user) {

        String userId = user.getUserId();

        CollectionExample collectionExample = new CollectionExample();
        collectionExample.createCriteria().andUserIdEqualTo(userId);
        collectionExample.setOrderByClause("created_at DESC");

        return collectionMapper.selectByExampleWithBLOBs(collectionExample);
    }

    @Override
    @ExternalUsage
    public List<Collection> getAllCollectionByUserId(UserId userId) {
        CollectionExample collectionExample = new CollectionExample();
        collectionExample.createCriteria().andUserIdEqualTo(userId.getUserId());
        return collectionMapper.selectByExampleWithBLOBs(collectionExample);
    }

    @Override
    @ExternalUsage
    public Collection readCollectionById(User user, CollectionId collectionId) {

        String userId = user.getUserId();

        CollectionExample collectionExample = new CollectionExample();
        collectionExample.createCriteria().andCollectionIdEqualTo(collectionId.getCollectionId());

        List<Collection> collections = collectionMapper.selectByExampleWithBLOBs(collectionExample);

        if(collections.size()==0){
            return null;
        }

        Collection collection1 = collections.get(0);

        if(Objects.equals(collection1.getUserId(),userId)){
            return collection1;
        }else{
            if(collection1.getIsPublic()==COLLECTION_PUBLIC){
                return collection1;
            }else{
                throw new UnauthorizedAccessException("该文档是非公开的,你无权访问");
            }
        }
    }

    @Override
    @InternalUsage
    public Collection getCollectionById(String collectionId) {

        CollectionExample collectionExample = new CollectionExample();
        collectionExample.createCriteria().andCollectionIdEqualTo(collectionId);
        List<Collection> collections = collectionMapper.selectByExampleWithBLOBs(collectionExample);

        if (collections != null && collections.size() == 1) {
            return collections.get(0);
        }else if(collections == null || collections.isEmpty()){
            return null;
        }else{
            throw new RuntimeException("返回了多个结果!!!");
        }
    }

    @Override
    public CollectionFile getCollectionFile(HttpSession session, String collectionId) {
        User u=(User) session.getAttribute(USER_NAME);

        Collection collection = getCollectionById(collectionId);


        if(collection.getIsPublic() || (u!=null && Objects.equals(u.getUserId(),collection.getUserId()))){
            List<Document> documents= documentService.getDocumentsByCollectionId(collectionId);

            CollectionFile collectionFile=new CollectionFile();
            collectionFile.setCollectionName(collection.getCollectionName());

            List<DocumentFile> collect = documents.stream().map(document -> {
                DocumentFile d = new DocumentFile();
                d.setDocumentName(document.getDocumentName());
                d.setContent(document.getDocumentContent());
                return d;
            }).collect(Collectors.toList());

            collectionFile.setDocumentFileList(collect);

            return collectionFile;
        }

        throw new UnauthorizedAccessException();
    }

    @Override
    @InternalUsage
    public void createDefaultCollection(User user) {
        Collection collection1=new Collection();

        collection1.setCollectionId(Security.getUid());
        collection1.setCreatedAt(new Date());
        collection1.setUserId(user.getUserId());

        collection1.setCollectionType(DOCUMENT_MARKDOWN);
        collection1.setCollectionName("教程");
        collection1.setCollectionDescription("这是教程,可以帮助你熟悉");
        collection1.setIsPublic(COLLECTION_PRIVATE);

        collectionMapper.insert(collection1);

        documentService.createDefaultDocument(collection1);
    }
}
