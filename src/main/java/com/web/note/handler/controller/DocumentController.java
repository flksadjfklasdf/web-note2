package com.web.note.handler.controller;

import com.web.note.entity.Document;
import com.web.note.entity.User;
import com.web.note.handler.request.*;
import com.web.note.handler.response.ResultEntity;
import com.web.note.service.api.DocumentService;
import com.web.note.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import java.util.List;

import static com.web.note.constant.PropertiesName.USER_NAME;
import static com.web.note.constant.StatusConstant.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/document")
public class DocumentController {

    @Autowired
    public DocumentService documentService;

    @PostMapping("/create")
    public ResultEntity<Object> create( HttpSession session, @RequestBody @Validated DocumentCreate document){

        User user = SessionUtil.getUser(session);

        boolean result = documentService.create(user, document);

        if (result) {
            ResultEntity<Object> entity = new ResultEntity<>();
            entity.setStatus(OK);
            return entity;
        }
        ResultEntity<Object> entity = new ResultEntity<>();
        entity.setStatus(SERVICE_ERROR);
        entity.setMessage("没有创建成功");
        return entity;
    }

    @PostMapping("/delete")
    public ResultEntity<Object> delete( HttpSession session, @RequestBody @Validated  DocumentId document){

        User user = SessionUtil.getUser(session);

        boolean result = documentService.deleteDocument(user, document);

        if (result) {
            ResultEntity<Object> entity = new ResultEntity<>();
            entity.setStatus(OK);
            return entity;
        }
        ResultEntity<Object> entity = new ResultEntity<>();
        entity.setStatus(SERVICE_ERROR);
        entity.setMessage("没有删除成功");
        return entity;
    }

    @PostMapping("/modify")
    public ResultEntity<Object> modify( HttpSession session, @RequestBody @Validated  DocumentModify document){

        User user = SessionUtil.getUser(session);

        boolean result = documentService.modify(user, document);

        if (result) {
            ResultEntity<Object> entity = new ResultEntity<>();
            entity.setStatus(OK);
            return entity;
        }
        ResultEntity<Object> entity = new ResultEntity<>();
        entity.setStatus(SERVICE_ERROR);
        entity.setMessage("没有更改成功");
        return entity;
    }
    @PostMapping("/save")
    public ResultEntity<Object> save( HttpSession session, @RequestBody @Validated DocumentSave document){

        User user = SessionUtil.getUser(session);

        boolean result = documentService.saveDocument(user, document);

        if (result) {
            ResultEntity<Object> entity = new ResultEntity<>();
            entity.setStatus(OK);
            return entity;
        }
        ResultEntity<Object> entity = new ResultEntity<>();
        entity.setStatus(SERVICE_ERROR);
        entity.setMessage("没有保存成功");
        return entity;
    }

    @PostMapping("/getDocumentById")
    public ResultEntity<Document> getDocumentById( HttpSession session, @RequestBody @Validated  DocumentId document){

        User user = (User) session.getAttribute(USER_NAME);

        Document document1 = documentService.readDocumentById(user, document);

        ResultEntity<Document> entity = new ResultEntity<>();

        if (document1 == null) {
            entity.setStatus(STATUS_404);
            entity.setMessage("你访问的内容不存在");
        }
        entity.setData(document1);
        entity.setStatus(OK);

        return entity;
    }

    @PostMapping("/getDocumentTitleAll")
    public ResultEntity<List<Document>> getDocumentTitleAll(HttpSession session, @RequestBody @Validated  CollectionId collection){

        User user = SessionUtil.getUser(session);

        List<Document> documents = documentService.getDocumentTitleAll(user,collection);

        ResultEntity<List<Document>> entity = new ResultEntity<>();
        entity.setData(documents);
        entity.setStatus(OK);

        return entity;
    }
    @PostMapping("/exchangeOrder")
    public ResultEntity<?> exchange(HttpSession session,@RequestBody @Validated DocumentExchange documentExchange){
        User user = SessionUtil.getUser(session);

        documentService.exchange(user,documentExchange.documentId1, documentExchange.documentId2);

        return ResultEntity.resultOK();
    }

}
