package com.test.response;

import com.test.exception.BusinessException;

import java.util.HashMap;
import java.util.Map;

public class ResponseFactory {

    public static <T> Response<T> getResponse(int code, String message, T data) {
        Response<T> response = new Response();
        response.setCode(code);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    public static <T> Response<T> getResponse(ResponseCode responseEnum, T data) {
        return getResponse(responseEnum.getCode(), responseEnum.getMessage(), data);
    }

    public static Response getResponse(ResponseCode responseEnum) {
        return getResponse(responseEnum.getCode(), responseEnum.getMessage(), null);
    }

    public static <T> Response<T> getSuccess(String message, T data) {
        return getResponse(ResponseCode.SUCCESS.getCode(), message, data);
    }

    public static <T> Response<T> getSuccessData(T data) {
        return getResponse(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), data);
    }

    public static <V> Response<Map<String, V>> getSuccessData(String k1, V v1) {
        HashMap<Object, Object> data = new HashMap<Object, Object>(1);
        data.put(k1, v1);
        return (Response) getResponse(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), data);
    }

    public static <V> Response<Map<String, V>> getSuccessData(String k1, V v1, String k2, V v2) {
        HashMap<Object, Object> data = new HashMap<Object, Object>(2);
        data.put(k1, v1);
        data.put(k2, v2);
        return (Response) getResponse(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), data);
    }

    public static <V> Response<Map<String, V>> getSuccessData(String k1, V v1, String k2, V v2, String k3, V v3) {
        HashMap<Object, Object> data = new HashMap<Object, Object>(3);
        data.put(k1, v1);
        data.put(k2, v2);
        data.put(k3, v3);
        return (Response) getResponse(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), data);
    }

    public static Response getSuccessMessage(String message) {
        return getResponse(ResponseCode.SUCCESS.getCode(), message, null);
    }

    public static Response getSuccess() {
        return getResponse(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), null);
    }

    public static Response getError(String message) {
        return getResponse(ResponseCode.ERROR.getCode(), message, null);
    }

    public static Response getError() {
        return getResponse(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage(), null);
    }

    public static Response getError(ResponseCode responseCode) {
        return getResponse(responseCode.getCode(), responseCode.getMessage(), null);
    }

    public static Response getError(BusinessException e) {
        return getResponse(e.getErrorCode(), e.getErrorMsg(), null);
    }

}
