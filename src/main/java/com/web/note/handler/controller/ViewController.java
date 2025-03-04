package com.web.note.handler.controller;

import com.web.note.cache.FileTypeCache;
import com.web.note.config.SystemConfig;
import com.web.note.entity.Collection;
import com.web.note.entity.Document;
import com.web.note.entity.User;
import com.web.note.exception.InvalidInputException;
import com.web.note.exception.UnauthorizedAccessException;
import com.web.note.handler.request.CollectionId;
import com.web.note.handler.request.DocumentId;
import com.web.note.handler.response.ViewResponse;
import com.web.note.handler.valid.IdValid;
import com.web.note.service.IpBanService;
import com.web.note.service.api.CollectionService;
import com.web.note.service.api.DocumentService;
import com.web.note.service.api.SystemConfigService;
import com.web.note.service.api.UserService;
import com.web.note.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.List;
import java.util.Objects;

import static com.web.note.constant.DatabaseConstant.DOCUMENT_HTML;
import static com.web.note.constant.DatabaseConstant.DOCUMENT_MARKDOWN;
import static com.web.note.constant.PropertiesName.*;
import static com.web.note.util.Security.base64Encode;

@Slf4j
@Validated
@Controller
public class ViewController implements ErrorController{

    @Autowired
    public CollectionService collectionService;
    @Autowired
    public DocumentService documentService;
    @Autowired
    public UserService userService;
    @Autowired
    public SystemConfig systemConfig;
    @Autowired
    public IpBanService ipBanService;
    @Autowired
    public SystemConfigService systemConfigService;

    @Value("${app.standard.url-prefix}")
    private String standardUrl;
    @Value("${app.img.url-prefix}")
    private String imgUrlPrefix;



    @RequestMapping(value = {"/", "/index.html"})
    public String index() {
        return "index";
    }

    @RequestMapping("/document.html")
    public ModelAndView document(HttpSession session) {

        User user = SessionUtil.getUser(session);

        ModelAndView modelAndView = new ModelAndView();

        if (session.getAttribute(USER_NAME) == null) {
            modelAndView.setViewName("redirect:/login.html");
            session.setAttribute(REDIRECT_VIEW, "redirect:/document.html");
        } else {
            List<Collection> allCollection = collectionService.getAllCollection(user);
            modelAndView.setViewName("document");
            modelAndView.addObject(COLLECTIONS, allCollection);

        }
        return modelAndView;
    }

    @RequestMapping("/resource.html")
    public ModelAndView resource(HttpServletRequest request,HttpSession session) {

        ModelAndView modelAndView=new ModelAndView();

        if (session.getAttribute(USER_NAME) == null) {
            modelAndView.setViewName("redirect:/login.html");
            return modelAndView;
        }

        String contextPath = request.getContextPath();
        String requestUrlPrefix = standardUrl + contextPath;
        modelAndView.addObject(REQUEST_URL_PREFIX, requestUrlPrefix);
        modelAndView.addObject("fileTypeList", FileTypeCache.getInstance().getAll());
        modelAndView.setViewName("resource");
        return modelAndView;
    }

    @RequestMapping("/login.html")
    public String login(HttpServletRequest httpServletRequest) {

        if(ipBanService.isIpBanned(httpServletRequest.getRemoteAddr())) {
            httpServletRequest.getSession().setAttribute(ENABLE_CAPTCHA,true);
        }else{
            httpServletRequest.getSession().setAttribute(ENABLE_CAPTCHA,false);
        }

        return "login";
    }

    @RequestMapping("/signin.html")
    public String signin() {
        return "signin";
    }

    @GetMapping("/edit/{collectionId}.html")
    public ModelAndView edit(HttpServletRequest request, HttpSession session, @PathVariable @IdValid String collectionId) throws InvalidInputException {

        CollectionId collection = new CollectionId();
        collection.setCollectionId(collectionId);
        ModelAndView modelAndView = new ModelAndView();

        User user = SessionUtil.getUser(session);


        if (session.getAttribute(USER_NAME) == null) {
            modelAndView.setViewName("redirect:/login.html");

            String contextPath = request.getContextPath();
            String relativePath = request.getRequestURI().substring(contextPath.length());
            String queryString = request.getQueryString();
            session.setAttribute(REDIRECT_VIEW, "redirect:" + relativePath + (queryString != null ? "?" + queryString : ""));
            return modelAndView;
        }

        Collection collectionById = collectionService.readCollectionById(user, collection);


        if (collectionById == null) {
            return ViewResponse.errorView(HttpStatus.NOT_FOUND,"你访问的内容不存在,该内容可能已经被删除!");
        }

        String type = collectionById.getCollectionType();


        if (Objects.equals(type, DOCUMENT_HTML)) {
            modelAndView.addObject(COLLECTIONS, collectionById);
            modelAndView.setViewName("edit-html");


            String contextPath = request.getContextPath();
            String requestUrlPrefix = standardUrl + contextPath;
            modelAndView.addObject(REQUEST_URL_PREFIX, requestUrlPrefix);
            modelAndView.addObject(IMG_URL_PREFIX, imgUrlPrefix);

            return modelAndView;
        } else if (Objects.equals(type, DOCUMENT_MARKDOWN)) {
            modelAndView.addObject(COLLECTIONS, collectionById);
            modelAndView.setViewName("edit-md");



            String contextPath = request.getContextPath();
            String requestUrlPrefix = standardUrl + contextPath;
            modelAndView.addObject(REQUEST_URL_PREFIX, requestUrlPrefix);
            modelAndView.addObject(IMG_URL_PREFIX, imgUrlPrefix);

            return modelAndView;
        } else {
            return ViewResponse.errorView(HttpStatus.BAD_REQUEST,"无效的请求");
        }
    }

    @RequestMapping("/rc/{collectionId}.html")
    public ModelAndView read(@PathVariable @IdValid String collectionId,HttpSession session) throws InvalidInputException {

        User user = SessionUtil.getUser(session);

        ModelAndView modelAndView=new ModelAndView();

        CollectionId collection=new CollectionId();
        collection.setCollectionId(collectionId);

        Collection collectionById = collectionService.readCollectionById(user, collection);

        List<Document> documentTitleAll = documentService.getDocumentTitleAll(user, collection);


        modelAndView.setViewName("collection-detail");
        modelAndView.addObject(COLLECTION,collectionById);
        modelAndView.addObject(DOCUMENTS,documentTitleAll);


        return modelAndView;

    }
    @RequestMapping("/rd/{documentId}.html")
    public ModelAndView readDocument(HttpSession session,@PathVariable @IdValid String documentId) {

        User user = (User) session.getAttribute(USER_NAME);
        DocumentId document=new DocumentId(documentId);

        String editorName;
        String documentName;
        try {
            Document document1 = documentService.readDocumentById(user, document);
            String userId = document1.getUserId();
            editorName = userService.getUserNameById(userId);
            documentName=document1.getDocumentName();

        } catch (UnauthorizedAccessException e) {
            return ViewResponse.errorView(HttpStatus.UNAUTHORIZED,"你无权访问此页面");
        }



        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("view");
        modelAndView.addObject(DOCUMENT_ID,documentId);
        modelAndView.addObject(EDITOR_NAME,editorName);
        modelAndView.addObject(DOCUMENT_NAME,documentName);
        return modelAndView;

    }

    @RequestMapping("/me.html")
    public ModelAndView me(HttpSession session,HttpServletRequest request){

        ModelAndView modelAndView=new ModelAndView();

        if (session.getAttribute(USER_NAME) == null) {
            modelAndView.setViewName("redirect:/login.html");

            String contextPath = request.getContextPath();
            String relativePath = request.getRequestURI().substring(contextPath.length());
            String queryString = request.getQueryString();
            session.setAttribute(REDIRECT_VIEW, "redirect:" + relativePath + (queryString != null ? "?" + queryString : ""));
            return modelAndView;
        }

        modelAndView.setViewName("user-info");
        return modelAndView;
    }
    @RequestMapping("/systemInit.html")
    public ModelAndView systemInit(HttpSession session){
        ModelAndView modelAndView=new ModelAndView();
        try {
            SessionUtil.getSystemAdmin(session);


            if(!systemConfig.initOK()){
                modelAndView.setViewName("init");
            }else{
                modelAndView.setViewName("redirect:/systemManage.html");
            }
            return modelAndView;
        } catch (Exception e) {
            return ViewResponse.errorView(HttpStatus.INTERNAL_SERVER_ERROR,"服务器出错了!");
        }
    }
    @RequestMapping("/systemManage.html")
    public ModelAndView systemManage(HttpSession session){
        ModelAndView modelAndView=new ModelAndView();
        try {
            SessionUtil.getSystemAdmin(session);

            if(systemConfig.initOK()){
                modelAndView.setViewName("manage");
            }else{
                modelAndView.setViewName("redirect:/systemInit.html");
            }
            return modelAndView;
        } catch (Exception e) {
            return ViewResponse.errorView(HttpStatus.INTERNAL_SERVER_ERROR,"服务器出错了!");
        }
    }
    @RequestMapping("/businessManage.html")
    public ModelAndView businessManage(HttpSession session){
        ModelAndView modelAndView=new ModelAndView();
        SessionUtil.getBusinessAdmin(session);
        try {


            modelAndView.setViewName("businessManage");

            return modelAndView;
        } catch (Exception e) {
            return ViewResponse.errorView(HttpStatus.INTERNAL_SERVER_ERROR,"服务器出错了!");
        }
    }
    @RequestMapping("/error")
    public ModelAndView handleError(WebRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error-page");

        // 获取错误码
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code", WebRequest.SCOPE_REQUEST);
        modelAndView.addObject("errorCode", statusCode);

        // 获取错误消息
        String errorMessage = (String) request.getAttribute("javax.servlet.error.message", WebRequest.SCOPE_REQUEST);

        // 如果是404错误，获取用户访问的URL
        if ( statusCode!= null && statusCode == 404) {
            String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri", WebRequest.SCOPE_REQUEST);
            errorMessage = "你请求的资源 '" + requestUri + "' 未找到,请确认输入了正确的url并确保你有访问该资源的权限";
        }

        modelAndView.addObject("message", errorMessage);

        return modelAndView;
    }
    @RequestMapping("/dError.html")
    public ModelAndView dError(@RequestParam("status")String status,@RequestParam("message") String message){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error-page");

        modelAndView.addObject("errorCode", status);

        modelAndView.addObject("message", message);
        modelAndView.setStatus(HttpStatus.valueOf(Integer.parseInt(status)));

        return modelAndView;
    }
    @RequestMapping("/file/authorize")
    public ModelAndView authorize(@RequestParam("fileId") @IdValid String fileId,@RequestParam(value = "message",required = false) String message){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("authorize");

        String encodedFileId = base64Encode(fileId);
        String encodedMessage = (message != null) ? base64Encode(message) : null;

        modelAndView.addObject("fileId", encodedFileId);
        modelAndView.addObject("message", encodedMessage);
        return modelAndView;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
