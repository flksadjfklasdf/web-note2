package com.web.note.config;

import com.google.gson.Gson;
import com.web.note.exception.*;
import com.web.note.handler.response.ResultEntity;
import com.web.note.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.io.IOException;
import java.util.Set;

import static com.web.note.constant.StatusConstant.*;
import static com.web.note.constant.PropertiesName.*;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BusinessException.class)
    public ModelAndView resolveBusinessException(BusinessException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("异常请求{}", e.getMessage());
        return getModelAndView("error-page", e.getMessage(), 200, BUSINESS_ERROR, request, response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ModelAndView resolveMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String errorMessage = "Validation failed"; // 默认错误消息

        if (e.getBindingResult().hasErrors()) {
            errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        }

        log.info("参数校验失败:" + errorMessage);

        return getModelAndView("redirect:/login.html", errorMessage, 200, INVALID_INPUT, request, response);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ModelAndView resolveConstraintViolationException(ConstraintViolationException e, HttpServletRequest request, HttpServletResponse response) throws IOException {

        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        StringBuilder stringBuilder = new StringBuilder();

        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            stringBuilder.append(constraintViolation.getMessage());
        }

        log.info("参数校验失败:" + stringBuilder);

        return getModelAndView("error-page", stringBuilder.toString(), 200, INVALID_INPUT, request, response);
    }

    @ExceptionHandler(value = ValidateException.class)
    public ModelAndView resolveValidateException(ValidateException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        return getModelAndView("redirect:/login.html", e.getMessage(), 200, INVALID_INPUT, request, response);
    }

    @ExceptionHandler(value = LoginException.class)
    public ModelAndView resolveLoginException(LoginException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        return getModelAndView("redirect:/login.html", e.getMessage(), 403, UN_LOGIN, request, response);
    }

    @ExceptionHandler(value = UnLoginException.class)
    public ModelAndView resolveUnLoginException(UnLoginException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        return getModelAndView("redirect:/login.html", "未登录", 403, UN_LOGIN, request, response);
    }

    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public ModelAndView resolveMaxUploadSizeExceededException(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return getModelAndView("error-page", "上传文件大小超出限制!", 200, SERVICE_ERROR, request, response);
    }

    @ExceptionHandler(value = InvalidInputException.class)
    public ModelAndView resolveAuthorizeException(InvalidInputException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        return getModelAndView("error-page", e.getMessage(), 200, INVALID_INPUT, request, response);
    }

    @ExceptionHandler(value = UnauthorizedAccessException.class)
    public ModelAndView resolveUnauthorizedAccessException(UnauthorizedAccessException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        return getModelAndView("error-page", e.getMessage(), 401, UN_AUTH, request, response);
    }

    @ExceptionHandler(value = AuthorizationException.class)
    public ModelAndView resolveAuthorizationException(AuthorizationException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("异常请求{}", e.getMessage());
        return getModelAndView("error-page", e.getMessage(), 401, UN_AUTH, request, response);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ModelAndView resolveHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("异常请求{}", e.getMessage());
        return getModelAndView("error-page", "参数非法", 400, SERVICE_ERROR, request, response);
    }
    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ModelAndView resolveResourceNotFoundException(ResourceNotFoundException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("异常请求{}", e.getMessage());
        return getModelAndView("error-page", e.getMessage(), 401, UN_AUTH, request, response);
    }
    @ExceptionHandler(value = PageNotFoundException.class)
    public ModelAndView resolvePageNotFoundException(PageNotFoundException e,HttpServletRequest request,HttpServletResponse response) throws IOException{
        return getModelAndView("error-page", e.getMessage(), 404, PAGE_NOT_FOUND, request, response);
    }

    @ExceptionHandler(value = Exception.class)
    public ModelAndView resolveRuntimeException(RuntimeException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.error(" ALERT!!! 发生非预期的异常 ", e);
        return getModelAndView("error-page", "内部出现错误,请稍后再试", 500, SERVICE_ERROR, request, response);
    }


    private static ModelAndView getModelAndView(String viewName, String message, int errorCode, int status, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //e.printStackTrace();

        boolean ajaxRequest = RequestUtil.isAjaxRequest(request);

        if (ajaxRequest) {

            ResultEntity<Object> responseEntity = new ResultEntity<>();

            responseEntity.setStatus(status);

            responseEntity.setMessage(message);

            Gson gson = new Gson();

            String json = gson.toJson(responseEntity);

            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(json);

            response.setStatus(errorCode);

            return null;
        }

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName(viewName);

        modelAndView.addObject(ERROR_CODE, errorCode);
        modelAndView.addObject(MESSAGE_NAME, message);

        response.setStatus(errorCode);

        return modelAndView;
    }
}
