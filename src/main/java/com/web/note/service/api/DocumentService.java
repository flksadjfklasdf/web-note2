package com.web.note.service.api;

import com.web.note.entity.Collection;
import com.web.note.entity.Document;
import com.web.note.entity.User;
import com.web.note.handler.request.*;
import com.web.note.security.ExternalUsage;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DocumentService {

    boolean create(User user, DocumentCreate document);
    boolean deleteDocument(User user, DocumentId document);
    boolean deleteDocumentInCollection(User user, CollectionId document);
    boolean modify(User user, DocumentModify document);
    Document readDocumentById(User user, DocumentId document);
    List<Document> getDocumentTitleAll(User user, CollectionId collection);


    Document getDocumentById(String documentId);

    boolean saveDocument(User user, DocumentSave document);

    @ExternalUsage
    @Transactional
    boolean exchange(User user, String documentId1, String documentId2);

    void createDefaultDocument(Collection collection1);

    List<Document> getDocumentsByCollectionId(String collectionId);
}
