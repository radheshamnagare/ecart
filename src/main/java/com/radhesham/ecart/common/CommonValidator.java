package com.radhesham.ecart.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class CommonValidator {

    private static final Logger logger = LoggerFactory.getLogger(CommonValidator.class);
    private CommonValidator() {
    }

    public static boolean isValidMobileNo(String mobileNo) {
        return isValidMobileLength(ConstantsPool.MIN_MOBILE_LENGTH, ConstantsPool.MAX_MOBILE_LENGTH, mobileNo) && isNumeric(mobileNo);
    }

    private static boolean isValidMobileLength(int minLength, int maxLength, String mobileNo) {
        boolean flag = true;
        try {
            logger.info("Entry in isValidLength()");
            if (minLength < 0 || maxLength < 0) {
                logger.warn("isValidMobileLength() minLength & maxLength is {} {}", minLength, maxLength);
                flag = false;
            } else if (Objects.isNull(mobileNo)) {
                flag = false;
            } else if (mobileNo.length() < minLength || mobileNo.length() > maxLength) {
                flag = false;
            }
            logger.info("Exit from isValidLength()");
        } catch (Exception e) {
            flag = false;
            logger.error("Exception in isValidMobileLength() ", e);
        }
        return flag;
    }

    public static boolean isValidEmail(String email) {
        boolean flag = true;
        try {
            logger.info("Entry in isValidEmail()");
            if (Objects.isNull(email)) {
                flag = false;
                logger.warn("isValidEmail _val is null");
            } else if (!isValidPatten(email, ConstantsPool.EMAIL_VALIDATION_PATTERN)) {
                flag = false;
            }
            logger.info("Exit from isValidEmail()");
        } catch (Exception e) {
            logger.error("Exception in isValidEmail() ", e);
        }
        return flag;
    }


    private static boolean isValidPatten(String mobileOrEmailVal, String validationPattern) {
        boolean flag;
        try {
            logger.info("Entry in isValidPatten()");
            Pattern patternMatcher = Pattern.compile(validationPattern);
            flag = patternMatcher.matcher(mobileOrEmailVal).matches();
            logger.info("Exit from isValidPatten()");
        } catch (Exception e) {
            flag = false;
            logger.error("Exception in isValidPatten() ", e);
        }
        return flag;
    }

    public static boolean isNumeric(String numberVal) {
        boolean flag = true;
        try {
            logger.info("Entry in isNumeric() with value {}", numberVal);
            if (Objects.isNull(numberVal)) {
                flag = false;
                logger.warn("isNumeric _val is null");
            } else if (!isValidPatten(numberVal, ConstantsPool.NUMBER_PATTERN)) {
                flag = false;
            }
            logger.info("Exit from isNumeric()");
        } catch (Exception e) {
            flag = false;
            logger.error("Exception in isNumeric() ", e);
        }
        return flag;
    }

    public static boolean isEmpty(String strVal) {
        return Objects.isNull(strVal) || strVal.isEmpty();
    }

    public static <T> boolean isEmpty(List<T> list) {
        return Objects.isNull(list) || list.isEmpty();
    }
}
