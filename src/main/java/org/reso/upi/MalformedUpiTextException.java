package org.reso.upi;

import org.reso.upi.validation.ValidationMessage;

import java.util.ArrayList;

public class MalformedUpiTextException extends Exception {
    private ArrayList<ValidationMessage> validationMessages;

    public MalformedUpiTextException(ArrayList<ValidationMessage> validationMessages) {
        this.validationMessages = validationMessages;
    }

    public MalformedUpiTextException(ValidationMessage message) {
        ArrayList<ValidationMessage> validationMessages = new ArrayList<ValidationMessage>();
        validationMessages.add(message);

        this.validationMessages = validationMessages;
    }

    public ArrayList<ValidationMessage> getValidationMessages() {
        return validationMessages;
    }

    public void setValidationMessages(ArrayList<ValidationMessage> validationMessages) {
        this.validationMessages = validationMessages;
    }
}
