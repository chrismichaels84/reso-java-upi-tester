# RESO Java UPI Tester

## Basic Usage
You can create an object from a valid UPI. 
This does NOT actually validate the UPI, it only ensures that the UPI has 6 parts
If the UPI does not have 6 parts, it will throw a `org.reso.upi.MalformedUpiTextException`

```java
UpiInterface upi = new Upi("US-04015-N-11022331-R-N");
```

Now, get the various pieces of the upi.

```java
CountryCode countryCode = upi.getCountryCode(); // US
String subCountryCode = upi.getSubCountryCode(); // 04015
String subCountyCode = upi.getSubCountyCode(); // N for none
String propertyId = upi.getPropertyId(); // 11022331
PropertyTypeCode propertyTypeCode = upi.getPropertyTypeCode(); // R
String subProperty = upi.getSubProperty(); // N for no unit number

String countryName = upi.getCountryName(); // This is set from the Country Code

// You can also add an arbitrary description
upi.setDescription("This is some cool place");

// Or the full UPI Code again
String upiText = upi.getRawUpiText(); // Unvalidated UPI text. Just what you provided.
```

You can construct a UPI object in a couple ways.
```java
UpiInterface emptyUpi = new Upi();
UpiInterface givenUpi = new Upi("US-04015-N-11022331-R-N");
UpiInterface givenUpiAndDescription = new Upi("US-04015-N-11022331-R-N", "Some Description");
UpiInterface pieceByPiece = new Upi(
        CountryCode.CO, // Must be a `CountryCode` enum
        "subCountryCode", // String
        "subCountyCode",  // String
        "propertyId", // String
        PropertyTypeCode.R, // Must be a org.reso.upi.property_type_code.PropertyTypeCode enum
        "subProperty", // String
        "aDescription" // String (optional)
);
```

## Validations
The real power comes in when you want to validate a UPI
This will ensure that the given UPI code follows certain `Rules` (more below)
By default, the only rules that MUST be followed are 6 parts to the UPI code.

```java
ArrayList<ValidationMessage> errors = upi.validate();

// You can also do a `strict` validation.
// This will make sure that `CountryCode` and `org.reso.upi.property_type_code.PropertyTypeCode` are accepted values.
ArrayList<ValidationMessage> strictErrors = upi.validate(true);

// if the errors are empty, you have a valid UPI Code.
assert errors.size() == 0 && strictErrors.size() == 0;

// If you just want a TRUE or FALSE, all you have to do is ask
// This will validate in non-strict mode and return a pass/fail
boolean isValid = upi.isValid();

// You can also do it as strict
boolean isValidStrict = upi.isValid(true);

// And then, you can get the ValidationMessages
ArrayList<ValidationMessage> messages = upi.getValidationMessages();

// Each of error is a `org.reso.upi.validation.ValidationMessage` with a code and english error message
for (ValidationMessage validationMessage : messages) {
    int code = validationMessage.getCode();
    String message = validationMessage.getMessage();
}
```

### Validation Rules
You can control the Validation Rules.
By default, the `ResoRules` and `ResoStrictRules` are added.
But, you can remove those rule sets and/or add your own by creating an ArrayList of rules and passing or setting it.
```java
ArrayList<ValidationRuleInterface> ruleSet = new ArrayList<ValidationRuleInterface>();

// These are default
ruleSet.add(new ResoAllPiecesRequiredRule()); // Makes sure that all 6 parts are there
ruleSet.add(new ResoValidPropertyTypeCodeRule()); // Ensure that `org.reso.upi.property_type_code.PropertyTypeCode` is not `NON_STANDARD` (strict)
ruleSet.add(new ResoValidCountryCodeRule()); // Ensures that `CountryCode` is not `UNDEFINED` (strict)

// And you can add your own
ruleSet.add(new MyCustomRule()); // Whatever you want

// Then, set it on the UPI object to replace the existing ruleset
upi.setValidationRules(ruleSet);

// Or, pass it as a second argument where you run validations
upi.validate(true, ruleSet); // strict, ruleset
upi.isValid(false, ruleSet); // strict, ruleset
```

## Country Codes and Property Type Codes
A word about CountryCodes and PropertyTypeCodes
These are both enumerations with set values.
The CountryCodes are taken from []() and include all the valid ISO country codes.
There is also an `UNDEFINED` code, which is valid for non-strict validation, but fails for strict.
Take a look at the `CountryCode` enumeration. There are a lot of helpers to help you find the country.
You can also get a lot of information about a country from its CountryCode.
Pull Request welcome to flesh this out more.

Similarly, org.reso.upi.property_type_code.PropertyTypeCode is a list of enumerations, but you can add a NON_STANDARD value.
A NON_STANDARD value will pass a non-strict validation, but fail a strict validation.

In both cases, you can set the value on the `org.reso.upi.Upi` object by passing in the enum

`upi.setCountryCode(CountryCode.FI); // Finland`

Or by passing in the string. If the string is not found,
CountryCode.UNDEFINED enum will be created. This will fail strict validation.

`upi.setCountryCode("XX");`

This will mean that getting that code will return an `UNDEFINED` ENUM

`CountryCode undefinedEnum = upi.getCountryCode(); // CountryCode.UNDEFINED`

You can still get the code you typed in, however. And this one will be used to piece anything together.

```java
String givenCountryCode = upi.getUndefinedCountryCode();
```

PropertyTypes are a little simpler.
If you set a non-standard property type, you can still get the value from the `org.reso.upi.property_type_code.PropertyTypeCode` enum.

```java
upi.setPropertyTypeCode("BO");
PropertyTypeCode code = upi.getPropertyTypeCode();
String nonStandardCode = code.getCode(); // BO
String description = code.getDescription(); // null

// Or, you can create a Non-Standard PropertyType Code enum
upi.setPropertyTypeCode(PropertyTypeCode.getNonStandard("XX", "Description"));
```

In both cases, what you supply is what will be used for validations and concatenation.

## Tests
There are some tests in the `tests` directory that should cover all the api features. 
But, more and better tests are always welcome. 
PR's encouraged.

## Contributing
Pull Requests are welcome and encouraged. Please issue PRs against `master` branch. New features will not be accepted with tests.

## Roadmap / ToDo
- [ ] Complete Property Type Code Enumerations
- [ ] Better Error Messages
- [ ] FIPS Helper
- [ ] Clean up and more complete tests
- [ ] Complete Github package (contributing, etc)

## Credits
- Michael Wilson <mwilson at fbsdata.com>

## Licence