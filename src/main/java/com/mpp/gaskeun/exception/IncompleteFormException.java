package com.mpp.gaskeun.exception;

public class IncompleteFormException extends IllegalArgumentException{
    public IncompleteFormException() {
        super("Form contains invalid/incomplete data.");
    }
}
