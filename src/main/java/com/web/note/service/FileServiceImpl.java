package com.web.note.service;

import com.web.note.business.UserBusiness;
import com.web.note.dao.*;
import com.web.note.entity.*;
import com.web.note.exception.InvalidArgumentException;
import com.web.note.exception.ResourceNotFoundException;
import com.web.note.exception.UnauthorizedAccessException;
import com.web.note.exception.UploadException;
import com.web.note.handler.request.FileSearchParam;
import com.web.note.handler.response.FileResourceVO;
import com.web.note.handler.response.ResourceInfo;
import com.web.note.handler.response.Response;
import com.web.note.handler.response.SearchFileEntity;
import com.web.note.service.api.AsyncService;
import com.web.note.service.api.FileService;
import com.web.note.util.MathUtil;
import com.web.note.util.SQLUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import com.web.note.util.Security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.web.note.constant.DatabaseConstant.*;
import static com.web.note.constant.PropertiesName.USER_NAME;
import static com.web.note.util.MathUtil.intDivideCeil;
import static com.web.note.util.StringUtil.empty;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Value("${file.save.path}")
    public String savePath;

    @Autowired
    public FileResourceMapper fileResourceMapper;
    @Autowired
    public DocumentMapper documentMapper;
    @Autowired
    public CollectionMapper collectionMapper;
    @Autowired
    public UserMapper userMapper;
    @Autowired
    public ImageMapper imageMapper;
    @Autowired
    public UserBusiness userBusiness;

    @Autowired
    public AsyncService asyncService;

    @Override
    @Transactional
    public int upload(User user, MultipartFile file, boolean isPublic, Boolean isAuthCode, String authCode, String fileNote, String fileType) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传的文件为空");
        }

        int size = (int) Math.ceil((double) file.getSize() / 1024);


        FileResource fileResource = new FileResource();

        fileResource.setFileSize(size);
        fileResource.setFileId(Security.getUid());
        fileResource.setUploadedAt(new Date());
        fileResource.setUserId(user.getUserId());
        if (isPublic) {
            fileResource.setPermission(FILE_PERMISSION_PUBLIC);
        } else if (isAuthCode) {
            if (empty(authCode)) {
                throw new InvalidArgumentException("请输入授权码!");
            }
            fileResource.setPermission(FILE_PERMISSION_AUTH_CODE);
            fileResource.setAuthCode(authCode);
        } else {
            fileResource.setPermission(FILE_PERMISSION_PRIVATE);
        }
        fileResource.setFileNote(fileNote);
        fileResource.setFileType(fileType);

        fileResource.setOriginalFilename(file.getOriginalFilename());

        fileResource.setLocalFilename(fileResource.getFileId());
        fileResource.setFileStatus(FILE_RESOURCE_OK);


        //剩余可用空间
        int availableSpace = userBusiness.getAvailableSpace(user);
        if (size > availableSpace) {
            throw new UploadException("剩余可用空间不足!!!");
        }

        this.saveFile(fileResource, file);


        return fileResourceMapper.insert(fileResource);
    }

    private void saveFile(FileResource fileResource, MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            Path filePath = Paths.get(savePath).resolve(fileResource.getLocalFilename());

            File dest = filePath.toFile();
            file.transferTo(dest);

            String s = Security.calculateMD5(dest);
            fileResource.setFileMd5(s);
        }
    }

    @Override
    public ResponseEntity<Resource> getFile(HttpSession session, HttpServletRequest request, String fileId, String authCode) {

        String contextPath = request.getContextPath();

        FileResource fileResource = fileResourceMapper.selectByPrimaryKey(fileId);

        if (fileResource == null) {
            throw new ResourceNotFoundException("你无权访问该资源");
        }

        Path filePath = Paths.get(savePath).resolve(fileResource.getLocalFilename());

        Resource resource = new FileSystemResource(filePath.toFile());

        Integer permission = fileResource.getPermission();

        ResponseEntity<Resource> response = Response.fileResource(fileResource, resource);

        User user = (User) session.getAttribute(USER_NAME);


        if (permission == FILE_PERMISSION_PUBLIC || (user != null && Objects.equals(user.getUserId(), fileResource.getUserId()))) {
            return response;
        }

        if (permission == FILE_PERMISSION_PRIVATE || empty(authCode)) {
            if (permission == FILE_PERMISSION_PRIVATE) {
                throw new UnauthorizedAccessException("你无权访问该资源");
            } else {
                String redirectPath = contextPath + "/file/authorize?fileId=" + fileId;
                return ResponseEntity.status(302).location(URI.create(redirectPath)).build();
            }
        }

        if (Objects.equals(authCode, fileResource.getAuthCode())) {
            return response;
        } else {
            String messageEncoded = "";
            try {
                messageEncoded = URLEncoder.encode("授权码错误", "UTF-8");
            } catch (UnsupportedEncodingException ignored) {

            }
            String redirectPath = contextPath + "/file/authorize?fileId=" + fileId + "&message=" + messageEncoded;
            return ResponseEntity.status(302).location(URI.create(redirectPath)).build();
        }

    }

    public SearchFileEntity searchFiles(User user, FileSearchParam fileSearchParam) {
        Integer pageSize = fileSearchParam.getPageSize();
        Integer page = fileSearchParam.getPage();
        Integer offset = (page - 1) * pageSize;
        Integer minSize = (int)(fileSearchParam.getMinSize() * 1024.0);
        Integer maxSize = (int)(fileSearchParam.getMaxSize() * 1024.0);
        String fileName;
        if (fileSearchParam.getFileName() != null && !fileSearchParam.getFileName().isEmpty()) {
            fileName = "%" + SQLUtil.escapeSqlSpecialChar(fileSearchParam.getFileName().toLowerCase()) + "%";
        } else {
            fileName = null;
        }

        String fileType;
        if (fileSearchParam.getFileType() != null && !fileSearchParam.getFileType().isEmpty()) {
            fileType = fileSearchParam.getFileType();
        } else {
            fileType = null;
        }

        Date startTime = fileSearchParam.getStartTime();
        Date endTime = fileSearchParam.getEndTime();
        int count = this.fileResourceMapper.countByUserId(user.getUserId(), fileName, fileType, startTime, endTime, minSize, maxSize);
        int pages = MathUtil.intDivideCeil(count, pageSize);
        int maxPages = Math.max(1, pages);
        List<FileResource> resources = this.fileResourceMapper.selectFiles(user.getUserId(), fileName, fileType, startTime, endTime, minSize, maxSize, pageSize, offset);
        List<FileResourceVO> collect = resources.stream().map(FileResourceVO::new).collect(Collectors.toList());
        SearchFileEntity searchFileEntity = new SearchFileEntity();
        searchFileEntity.setFiles(collect);
        searchFileEntity.setTotalPages(maxPages);
        return searchFileEntity;
    }

    @Override
    public int modifyFile(User user, String fileId, Boolean isPublic, Boolean isAuthCode, String authCode, String fileNote, String fileType) {

        FileResource modifyFileResource = new FileResource();


        if (isPublic) {
            modifyFileResource.setPermission(FILE_PERMISSION_PUBLIC);
        } else if (isAuthCode) {
            if (empty(authCode)) {
                throw new InvalidArgumentException("请输入授权码!");
            }
            modifyFileResource.setPermission(FILE_PERMISSION_AUTH_CODE);
            modifyFileResource.setAuthCode(authCode);
        } else {
            modifyFileResource.setPermission(FILE_PERMISSION_PRIVATE);
        }
        modifyFileResource.setFileNote(fileNote);
        modifyFileResource.setFileType(fileType);

        FileResourceExample fileResourceExample = new FileResourceExample();
        fileResourceExample.createCriteria().andUserIdEqualTo(user.getUserId()).andFileIdEqualTo(fileId);

        return fileResourceMapper.updateByExampleSelective(modifyFileResource, fileResourceExample);
    }

    @Override
    public int deleteFile(User user, FileResource fileResource) {

        FileResourceExample fileResourceExample = new FileResourceExample();
        fileResourceExample.createCriteria().andUserIdEqualTo(user.getUserId()).andFileIdEqualTo(fileResource.getFileId());

        List<FileResource> resources = fileResourceMapper.selectByExample(fileResourceExample);

        if (resources == null || resources.size() == 0) {
            return 0;
        }
        if (resources.size() > 1) {
            throw new RuntimeException("内部严重错误!出现多条重复主键");
        }

        asyncService.deleteFile(resources);

        return fileResourceMapper.deleteByExample(fileResourceExample);
    }

    @Override
    public ResourceInfo getResourceInfo(User user) {

        ResourceInfo resourceInfo = new ResourceInfo();

        FileResourceExample fileResourceExample = new FileResourceExample();
        fileResourceExample.createCriteria().andUserIdEqualTo(user.getUserId());
        int fileCount = fileResourceMapper.countByExample(fileResourceExample);

        DocumentExample documentExample = new DocumentExample();
        documentExample.createCriteria().andUserIdEqualTo(user.getUserId());
        int documentCount = documentMapper.countByExample(documentExample);

        ImageExample imageExample = new ImageExample();
        imageExample.createCriteria().andUserIdEqualTo(user.getUserId());
        int imageCount = imageMapper.countByExample(imageExample);

        int imageSizeCount = imageMapper.countImageSizeByUser(user.getUserId());
        int fileSizeCount = fileResourceMapper.countFileSizeByUser(user.getUserId());

        User userDB = userMapper.selectByPrimaryKey(user.getUserId());


        CollectionExample collectionExample = new CollectionExample();
        collectionExample.createCriteria().andUserIdEqualTo(user.getUserId());

        int i = collectionMapper.countByExample(collectionExample);


        resourceInfo.setTotalFiles(fileCount);
        resourceInfo.setTotalArticles(documentCount);
        resourceInfo.setTotalImages(imageCount);

        resourceInfo.setSpaceLimit(userDB.getStorageLimit());

        resourceInfo.setTotalImagesSize(imageSizeCount);
        resourceInfo.setTotalFilesSize(fileSizeCount);
        resourceInfo.setUsedSpace(imageSizeCount + fileSizeCount);
        resourceInfo.setTotalCollections(i);

        return resourceInfo;
    }



}
