package com.web.note.handler.controller;

import com.web.note.entity.Collection;
import com.web.note.entity.User;
import com.web.note.handler.request.CollectionCreate;
import com.web.note.handler.request.CollectionId;
import com.web.note.handler.request.CollectionModify;
import com.web.note.handler.response.CollectionFile;
import com.web.note.handler.response.DocumentFile;
import com.web.note.handler.response.ResultEntity;
import com.web.note.service.api.CollectionService;
import com.web.note.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.web.note.constant.StatusConstant.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/collection")
public class CollectionController {

    @Autowired
    public CollectionService collectionService;

    @PostMapping("/create")
    public ResultEntity<Object> create(HttpSession session, @RequestBody @Validated  CollectionCreate collection){

        User user = SessionUtil.getUser(session);

        boolean result = collectionService.createCollection(user, collection);
        if(result){
            return ResultEntity.resultOK();
        }
        ResultEntity<Object> entity=new ResultEntity<>();
        entity.setStatus(SERVICE_ERROR);
        entity.setMessage("没有创建成功");
        return entity;

    }
    @PostMapping("/delete")
    public ResultEntity<Object> delete(HttpSession session, @RequestBody @Validated  CollectionId collection){

        User user = SessionUtil.getUser(session);

        boolean b = collectionService.deleteCollection(user, collection);

        if(b){
            return ResultEntity.resultOK();
        }
        ResultEntity<Object> entity=new ResultEntity<>();
        entity.setStatus(SERVICE_ERROR);
        entity.setMessage("没有删除成功");
        return entity;

    }
    @PostMapping("/modify")
    public ResultEntity<Object> modify(HttpSession session, @RequestBody @Validated  CollectionModify collection){

        User user = SessionUtil.getUser(session);

        boolean b = collectionService.modifyCollection(user, collection);

        if(b){
            return ResultEntity.resultOK();
        }
        ResultEntity<Object> entity=new ResultEntity<>();
        entity.setStatus(SERVICE_ERROR);
        entity.setMessage("没有保存成功");
        return entity;

    }
    @PostMapping("/getAll")
    public ResultEntity<List<Collection>> get(HttpSession session){

        User user = SessionUtil.getUser(session);

        List<Collection> collection1 = collectionService.getAllCollection(user);

        ResultEntity<List<Collection>> entity=new ResultEntity<>();
        entity.setData(collection1);
        entity.setStatus(OK);

        return entity;
    }
    @PostMapping("/getCollectionById")
    public ResultEntity<Collection> get(HttpSession session,@RequestBody @Validated  CollectionId collection){

        User user = SessionUtil.getUser(session);

        Collection collection1 = collectionService.readCollectionById(user, collection);

        if (collection1 == null) {
            ResultEntity<Collection> entity=new ResultEntity<>();
            entity.setStatus(NO_RESULT);
            entity.setMessage("没有找到相关数据");
            return entity;
        }
        ResultEntity<Collection> entity=new ResultEntity<>();
        entity.setStatus(OK);
        entity.setData(collection1);
        return entity;
    }
    @GetMapping("/export")
    public ResponseEntity<StreamingResponseBody> exportCollection(HttpSession session,@RequestParam("collectionId")String collectionId) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);


        CollectionFile collectionAll = collectionService.getCollectionFile(session, collectionId);
        List<DocumentFile> documentFileList = collectionAll.getDocumentFileList();
        String collectionName = collectionAll.getCollectionName();

        try {
            headers.setContentDispositionFormData("attachment", URLEncoder.encode(collectionName,"UTF-8")+".zip");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("发生错误!",e);
        }


        StreamingResponseBody responseBody = outputStream -> {
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {

                zipOutputStream.putNextEntry(new ZipEntry("notes/"));

                for (int i =1 ;i <= documentFileList.size();i++) {
                    String filename = i+"."+documentFileList.get(i-1).getDocumentName()+".md";
                    String fileContent = documentFileList.get(i-1).getContent();
                    zipOutputStream.putNextEntry(new ZipEntry("notes/"+filename));
                    zipOutputStream.write(fileContent.getBytes(StandardCharsets.UTF_8));
                    zipOutputStream.closeEntry();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
    }
}
