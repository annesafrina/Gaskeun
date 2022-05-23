package com.mpp.gaskeun.exception;

public class IncompleteFormException extends IllegalStateException{
    public IncompleteFormException() {
        super("Form contains invalid/incomplete data.");
    }
}
