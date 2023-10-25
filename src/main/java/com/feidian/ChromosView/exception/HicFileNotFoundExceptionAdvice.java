package com.feidian.ChromosView.exception;

import com.feidian.ChromosView.utils.ApiResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class HicFileNotFoundExceptionAdvice {
    @ExceptionHandler(value = {HicFileNotFoundException.class})
    @ResponseBody
    public ApiResponse<String> fileNotFound(HicFileNotFoundException e) {
        System.out.println(e.getMessage());
        return ApiResponse.fail(202, "File not found!");
    }

}
