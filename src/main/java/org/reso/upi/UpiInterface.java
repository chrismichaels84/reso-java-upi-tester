package org.reso.upi;

import org.reso.upi.country_code.CountryCode;
import org.reso.upi.property_type_code.PropertyTypeCode;
import org.reso.upi.validation.ValidationMessage;
import org.reso.upi.validation.ValidationRuleInterface;

import java.util.ArrayList;

public interface UpiInterface {
    String toUpi() throws MalformedUpiTextException;

    void parseUpi(String upiText, String description) throws MalformedUpiTextException;

    void parseUpi(String upiText) throws MalformedUpiTextException;

    ArrayList<ValidationMessage> validate();

    ArrayList<ValidationMessage> validate(boolean strict);

    ArrayList<ValidationMessage> validate(boolean strict, ArrayList<ValidationRuleInterface> ruleSet);

    /* Special Getter */
    boolean isValid(boolean strict, ArrayList<ValidationRuleInterface> ruleSet);

    boolean isValid(boolean strict);

    boolean isValid();

    void setValid(boolean valid);

    String getRawUpiText();

    void setUpiText(String upiText);

    String getDescription();

    void setDescription(String description);

    ArrayList<ValidationMessage> getValidationMessages();

    String getCountryName();

    CountryCode getCountryCode();

    void setCountryCode(CountryCode countryCode);

    void setCountryCode(String countryCode);

    String getSubCountryCode();

    void setSubCountryCode(String subCountryCode);

    String getSubCountyCode();

    void setSubCountyCode(String subCountyCode);

    String getPropertyId();

    void setPropertyId(String propertyId);

    PropertyTypeCode getPropertyTypeCode();

    void setPropertyTypeCode(PropertyTypeCode propertyType);

    void setPropertyTypeCode(String propertyTypeCode);

    String getSubProperty();

    void setSubProperty(String subProperty);

    @Override
    String toString();

    String getUndefinedCountryCode();

    void setUndefinedCountryCode(String undefinedCountryCode);

    ArrayList<ValidationRuleInterface> getValidationRules();

    void setValidationRules(ArrayList<ValidationRuleInterface> validationRules);
}
