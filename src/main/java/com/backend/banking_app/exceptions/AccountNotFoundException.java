package com.backend.banking_app.exceptions;

public class AccountNotFoundException extends Exception{

    public AccountNotFoundException() {
    }

    public AccountNotFoundException(String message){
        super(message);
    }


}
