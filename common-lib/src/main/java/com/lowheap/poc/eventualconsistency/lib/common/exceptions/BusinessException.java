package com.lowheap.poc.eventualconsistency.lib.common.exceptions;

public class BusinessException extends RuntimeException {
    public BusinessException(String msg) {
        super(msg);
    }
}
