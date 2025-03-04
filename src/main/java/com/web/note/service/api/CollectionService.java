package com.web.note.service.api;

import com.web.note.entity.Collection;
import com.web.note.entity.User;
import com.web.note.handler.request.CollectionCreate;
import com.web.note.handler.request.CollectionId;
import com.web.note.handler.request.CollectionModify;
import com.web.note.handler.request.UserId;
import com.web.note.handler.response.CollectionFile;

import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.util.List;

public interface CollectionService {
    boolean createCollection(User user, CollectionCreate collection);
    boolean deleteCollection(User user, CollectionId collection);
    boolean modifyCollection(User user, CollectionModify collection);
    List<Collection> getAllCollection(User user);
    Collection readCollectionById(User user, CollectionId collection);

    void createDefaultCollection(User user);
    List<Collection> getAllCollectionByUserId(UserId userId);
    Collection getCollectionById(String collectionId);

    CollectionFile getCollectionFile(HttpSession httpSession, String collectionId);
}
