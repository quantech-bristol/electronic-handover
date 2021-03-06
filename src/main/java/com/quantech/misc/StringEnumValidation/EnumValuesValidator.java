package com.quantech.misc.StringEnumValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

//This class can define how to validate a string is an enum value. Needs its a duplicate (in code but with dif name)
//of Interface validateString where its validated by tag points here
public class EnumValuesValidator implements ConstraintValidator<ValidateEnumValues, String> {
    private List<String> acceptedVals;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context)
    {
        return acceptedVals.contains(value.toUpperCase());
    }



    @Override
    public void initialize(ValidateEnumValues constraintAnnotation)
    {
        acceptedVals = new ArrayList<String>();
        Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClazz();

        @SuppressWarnings("rawtypes")
        Enum[] enumValArr = enumClass.getEnumConstants();

        for(@SuppressWarnings("rawtypes") Enum enumVal : enumValArr)
        {
            acceptedVals.add(enumVal.toString().toUpperCase());
        }

    }


}
