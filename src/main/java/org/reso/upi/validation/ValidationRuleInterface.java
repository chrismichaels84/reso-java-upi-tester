package org.reso.upi.validation;

import org.reso.upi.UpiInterface;

import java.util.ArrayList;

public interface ValidationRuleInterface {
    ArrayList<ValidationMessage> validate(UpiInterface upi, boolean strict);
}
