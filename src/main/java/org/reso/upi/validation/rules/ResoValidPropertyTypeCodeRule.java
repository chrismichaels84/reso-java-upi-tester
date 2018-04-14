package org.reso.upi.validation.rules;

import org.reso.upi.UpiInterface;
import org.reso.upi.property_type_code.PropertyTypeCode;
import org.reso.upi.validation.AbstractValidationRule;
import org.reso.upi.validation.ValidationMessage;

import java.util.ArrayList;

public class ResoValidPropertyTypeCodeRule extends AbstractValidationRule {
    public ArrayList<ValidationMessage> validate(UpiInterface upi, boolean strict) {
        ArrayList<ValidationMessage> errors = new ArrayList<ValidationMessage>();

        // If we are in strict mode, check for non standard enumerations
        if (strict) {
            if (upi.getPropertyTypeCode() == PropertyTypeCode.NON_STANDARD) {
                errors.add(new ValidationMessage(410, "The Property Type Code is non standard"));
            }
        }

        return errors;
    }
}
