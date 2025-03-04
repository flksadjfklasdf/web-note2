package com.web.note.handler.controller;

import com.web.note.dao.FileTypeMapper;
import com.web.note.entity.FileResource;
import com.web.note.entity.FileType;
import com.web.note.entity.FileTypeExample;
import com.web.note.entity.User;
import com.web.note.handler.request.FileModify;
import com.web.note.handler.request.FileSearchParam;
import com.web.note.handler.response.FileTypeVO;
import com.web.note.handler.response.ResourceInfo;
import com.web.note.handler.response.ResultEntity;
import com.web.note.handler.response.SearchFileEntity;
import com.web.note.handler.valid.AuthCodeValid;
import com.web.note.handler.valid.DatabaseVarcharValid;
import com.web.note.handler.valid.IdValid;
import com.web.note.security.RequireRole;
import com.web.note.security.Role;
import com.web.note.service.api.FileService;
import com.web.note.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotEmpty;

import java.util.List;
import java.util.stream.Collectors;

import static com.web.note.constant.StatusConstant.OK;
import static com.web.note.constant.StatusConstant.SERVICE_ERROR;

@Validated
@RestController
@RequestMapping("/file")
public class FileResourceController {

    @Autowired
    public FileService fileService;
    @Autowired
    public FileTypeMapper fileTypeMapper;

    @RequireRole(Role.NORMAL_USER)
    @RequestMapping("/upload")
    public ResultEntity<Object> uploadFile(
            HttpSession session,
            @RequestParam("file") MultipartFile file,
            @RequestParam(name = "isPublic", defaultValue = "false") Boolean isPublic,
            @RequestParam(name = "isAuthCode", defaultValue = "false") Boolean isAuthCode,
            @RequestParam("authCode") @AuthCodeValid String authCode,
            @RequestParam(name = "fileNote",defaultValue = "") @DatabaseVarcharValid String fileNote,
            @RequestParam(name = "fileType",defaultValue = "default") String fileType) {

        ResultEntity<Object> result1 = new ResultEntity<>();

        User user = SessionUtil.getUser(session);

        try {
            int result = fileService.upload(user, file, isPublic, isAuthCode,authCode,fileNote,fileType);
            result1.setStatus(OK);
            result1.setMessage(result == 1 ? "上传成功" : "上传失败");
            return result1;
        } catch (Exception e) {
            result1.setStatus(SERVICE_ERROR);
            result1.setMessage(e.getMessage());
            return result1;
        }
    }

    @RequestMapping("/d/{fileId}")
    public ResponseEntity<Resource> downloadFile(
            HttpSession session,
            HttpServletRequest request,
            @PathVariable @IdValid String fileId,
            @RequestParam(name = "authCode", required = false) String authCode) {

        return fileService.getFile(session, request, fileId, authCode);
    }

    @RequireRole(Role.NORMAL_USER)
    @RequestMapping("/search")
    public ResultEntity<SearchFileEntity> getAllResources(HttpSession session, @RequestBody FileSearchParam fileSearchParam) {

        User user = SessionUtil.getUser(session);

        SearchFileEntity resources = fileService.searchFiles(user,fileSearchParam);

        ResultEntity<SearchFileEntity> result = new ResultEntity<>();

        result.setStatus(OK);
        result.setData(resources);
        return result;
    }
    @RequireRole(Role.NORMAL_USER)
    @RequestMapping("/delete")
    public ResultEntity<Object> deleteFile(HttpSession session, @RequestBody @Validated  FileResource fileResource){

        User user = SessionUtil.getUser(session);

        int x=fileService.deleteFile(user,fileResource);

        ResultEntity<Object> result1 = ResultEntity.resultOK();
        result1.setMessage(x == 1 ? "删除成功" : "删除失败");
        return result1;
    }
    @RequireRole(Role.NORMAL_USER)
    @RequestMapping("/modify")
    public ResultEntity<Object> modifyFile( HttpSession session, @RequestBody @Validated  FileModify fileModify){

        User user = SessionUtil.getUser(session);

        int x=fileService.modifyFile(user,
                fileModify.getFileId(),
                fileModify.getIsPublic(),
                fileModify.getIsAuthCode(),
                fileModify.getAuthCode(),
                fileModify.getFileNote(),
                fileModify.getFileType());

        ResultEntity<Object> result1 = new ResultEntity<>();

        result1.setStatus(OK);
        result1.setMessage(x == 1 ? "更改成功" : "更改失败");
        return result1;
    }

    @RequireRole(Role.NORMAL_USER)
    @RequestMapping("/resourceInfo")
    public ResultEntity<ResourceInfo> getResourceInfo(HttpSession session){

        User user = SessionUtil.getUser(session);

        ResourceInfo resourceInfo=fileService.getResourceInfo(user);

        ResultEntity<ResourceInfo> result1 = new ResultEntity<>();
        result1.setData(resourceInfo);
        result1.setStatus(OK);

        return result1;
    }
    @RequireRole(Role.NORMAL_USER)
    @RequestMapping({"/types"})
    public ResultEntity<List<FileTypeVO>> getAllTypes() {
        FileTypeExample example = new FileTypeExample();
        List<FileType> fileTypes = this.fileTypeMapper.selectByExample(example);
        List<FileTypeVO> collect = fileTypes.stream().map(FileTypeVO::new).collect(Collectors.toList());
        return ResultEntity.resultOKWithData(collect);
    }
}
