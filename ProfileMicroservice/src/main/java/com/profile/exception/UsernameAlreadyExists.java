package com.profile.exception;

public class UsernameAlreadyExists extends Exception{
    public UsernameAlreadyExists() {
        super("Username already exists!");
    }
}
