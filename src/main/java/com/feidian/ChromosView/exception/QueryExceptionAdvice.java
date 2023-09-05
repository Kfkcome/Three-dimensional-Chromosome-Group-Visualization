package com.feidian.ChromosView.exception;

import com.feidian.ChromosView.utils.ApiResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileNotFoundException;

@ControllerAdvice
public class QueryExceptionAdvice {
    @ExceptionHandler(value = {QueryException.class})
    @ResponseBody
    public ApiResponse queryFail(QueryException queryException) {
        if (queryException.getMessage().equals("uuid错误，无法查询到数据"))
            return ApiResponse.fail(203, queryException.getMessage());
        return ApiResponse.fail(202, queryException.getMessage());
    }
    @ExceptionHandler(value = FileNotFoundException.class)
    @ResponseBody
    public ApiResponse findError(FileNotFoundException fileNotFoundException)
    {
        System.out.println(fileNotFoundException.getMessage());
        return ApiResponse.fail(202,"查询不到对应文件");
    }
}
