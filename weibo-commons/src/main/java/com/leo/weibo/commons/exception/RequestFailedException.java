package com.leo.weibo.commons.exception;

/**
 * Created by Leo on 2017/6/6.
 */
public class RequestFailedException extends RuntimeException {

    public RequestFailedException(String message) {
        super(message);
    }

}
