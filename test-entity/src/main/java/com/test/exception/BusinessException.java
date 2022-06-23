package com.test.exception;

public class BusinessException extends RuntimeException{

    private static final long serialVersionUID = 81085691206487216L;

    private int errorCode;

    private String errorMsg;

    private Object refObject;

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Object getRefObject() {
        return this.refObject;
    }

    public void setRefObject(Object refObject) {
        this.refObject = refObject;
    }

    public BusinessException(int errorCode, String errorMsg) {
        setErrorCode(errorCode);
        this.errorMsg = errorMsg;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        setErrorCode(errorCode);
    }

    public String getDesc() {
        return getErrorCode() + ":" + this.errorMsg;
    }

    public String toString() {
        return "BusinessException{errorCode='" + getErrorCode() + '\'' + ", errorMsg='" + this.errorMsg + '\'' + ", refObject=" + this.refObject + '}';
    }

}
