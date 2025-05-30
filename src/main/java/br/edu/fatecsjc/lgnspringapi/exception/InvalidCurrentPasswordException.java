package br.edu.fatecsjc.lgnspringapi.exception;

public class InvalidCurrentPasswordException extends RuntimeException {
    public InvalidCurrentPasswordException() {
        super("Wrong password");
    }
}
