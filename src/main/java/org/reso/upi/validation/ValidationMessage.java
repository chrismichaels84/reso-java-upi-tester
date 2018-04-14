package org.reso.upi.validation;

public class ValidationMessage {
    private int code;
    private String message;

    public ValidationMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return String.valueOf(this.getCode()) + " - " + this.getMessage();
    }
}
