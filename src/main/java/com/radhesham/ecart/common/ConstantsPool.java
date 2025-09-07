package com.radhesham.ecart.common;

public class ConstantsPool {

    private  ConstantsPool(){
    }
    public static final Integer MIN_MOBILE_LENGTH=10;
    public static final Integer MAX_MOBILE_LENGTH=14;

    public static final String NUMBER_PATTERN="^\\d+$";
    public static final String EMAIL_VALIDATION_PATTERN="^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    //error codes
    public static final String error_code_success="000";
    public static final String error_code_duplicate="001";
    public static final String error_code_invalid="003";
    public static final String error_code_unknown="004";
    public static final String error_code_failed="005";
    public static final String error_code_required="006";
    public static final String unknown="unknown";

    public static final String first_constant="{first}";
    public static final String second_constant="{second}";

    public static final String STATUS_PENDING="pending";
    public static final String STATUS_PROCESSED="processed";
    public static final String STATUS_FAILED="failed";
    public static final String STATUS_SUCCESS="success";
    public static final String STATUS_CREATED="created";

    public static final String ACTION_DELETE="delete";
    public static final String ACTION_DELETE_ALL="delete_all";
    public static final String ACTION_ADD="add";

    public static final String STATE_ONE="1";
    public static final String STATE_ZERO="0";



}
