package org.reso.upi.validation.rules;

import org.reso.upi.UpiInterface;
import org.reso.upi.country_code.CountryCode;
import org.reso.upi.validation.AbstractValidationRule;
import org.reso.upi.validation.ValidationMessage;

import java.util.ArrayList;

public class ResoValidCountryCodeRule extends AbstractValidationRule {
    public ArrayList<ValidationMessage> validate(UpiInterface upi, boolean strict) {
        ArrayList<ValidationMessage> errors = new ArrayList<ValidationMessage>();

        // If we are in strict mode, check for non standard enumerations
        if (strict) {
            if (upi.getCountryCode() == CountryCode.UNDEFINED) {
                errors.add(new ValidationMessage(400, "The Country Code is not recognized."));
            }
        }

        return errors;
    }
}
