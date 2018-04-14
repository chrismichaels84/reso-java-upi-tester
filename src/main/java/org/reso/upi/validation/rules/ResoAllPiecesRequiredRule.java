package org.reso.upi.validation.rules;

import org.reso.upi.UpiInterface;
import org.reso.upi.validation.AbstractValidationRule;
import org.reso.upi.validation.ValidationMessage;

import java.util.ArrayList;

public class ResoAllPiecesRequiredRule extends AbstractValidationRule {
    public static ValidationMessage getMissingCountryCodeMessage() {
        return new ValidationMessage(310, "The COUNTRY_CODE is not defined");
    }

    public static ValidationMessage getMissingSubCountryCodeMessage() {
        return new ValidationMessage(320, "The SUB_COUNTRY_CODE is not defined");
    }

    public static ValidationMessage getMissingCountyCodeMessage() {
        return new ValidationMessage(330, "The SUB_COUNTY_CODE is not defined");
    }

    public static ValidationMessage getMissingPropertyIdMessage() {
        return new ValidationMessage(340, "The PROPERTY_ID is not defined");
    }

    public static ValidationMessage getMissingPropertyTypeCodeMessage() {
        return new ValidationMessage(350, "The PROPERTY TYPE CODE is not defined");
    }

    public static ValidationMessage getMissingSubPropertyMessage() {
        return new ValidationMessage(360, "The SUB PROPERTY is not defined");
    }


    public ArrayList<ValidationMessage> validate(UpiInterface upi, boolean strict) {

        // Create the local errors bag
        ArrayList<ValidationMessage> errors = new ArrayList<ValidationMessage>();

        // Non Strict Checking
        if (upi.getCountryCode() == null) {
            errors.add(ResoAllPiecesRequiredRule.getMissingCountryCodeMessage());
        }

        if (upi.getSubCountryCode() == null) {
            errors.add(ResoAllPiecesRequiredRule.getMissingSubCountryCodeMessage());
        }

        if (upi.getSubCountyCode() == null) {
            errors.add(ResoAllPiecesRequiredRule.getMissingCountyCodeMessage());
        }

        if (upi.getPropertyId() == null) {
            errors.add(ResoAllPiecesRequiredRule.getMissingPropertyIdMessage());
        }

        if (upi.getPropertyTypeCode() == null) {
            errors.add(ResoAllPiecesRequiredRule.getMissingPropertyTypeCodeMessage());
        }

        if (upi.getSubProperty() == null) {
            errors.add(ResoAllPiecesRequiredRule.getMissingSubPropertyMessage());
        }

        return errors;
    }
}
