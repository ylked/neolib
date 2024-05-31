package ch.hearc.nde.loanservice.remote.impl;

public class Error400Exception extends RuntimeException{
    public Error400Exception() {
        super();
    }
    public Error400Exception(String message) {
        super(message);
    }
}
