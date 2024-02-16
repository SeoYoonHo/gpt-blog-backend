package com.syh.gptblog.exception;

import java.io.Serial;

public class NotMatchException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public NotMatchException(String message) {
        super(message);
    }

}