package com.web.note.handler.response;

import lombok.Data;

import static com.web.note.constant.StatusConstant.OK;

@Data
public class ResultEntity<T> {
    public int status;
    public String message;
    public T data;

    public static ResultEntity<Object> resultOK(){
        ResultEntity<Object> result=new ResultEntity<>();
        result.setStatus(OK);
        return result;
    }
    public static <T> ResultEntity<T> resultOKWithData(T data){
        ResultEntity<T> result=new ResultEntity<>();
        result.setStatus(OK);
        result.setData(data);
        return result;
    }

}
