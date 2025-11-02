package com.nexDew.Authentication.error;

public class PasswordAlreadyExistException extends Exception {
    public PasswordAlreadyExistException(String msg){
        super(msg);
    }
}
