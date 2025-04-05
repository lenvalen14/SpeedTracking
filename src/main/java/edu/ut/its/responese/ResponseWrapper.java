package edu.ut.its.responese;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseWrapper<T> {
    private String responseMessage;
    private T data;

    public ResponseWrapper(String responseMessage, T data) {
        this.responseMessage = responseMessage;
        this.data = data;
    }
}