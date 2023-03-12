package com.market.aaa.config.constant;

public class Constants {
    public static final Long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L; // 30분
    public static final Long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L; // 1주일

    public static final String JWT_PREFIX = "Bearer";

    public static final String JWT_HEADER = "Authorization";

    public static final String JWT_CLAIMS_ROLE = "roles";

    public static final String JWT_CLAIMS_COMPANY = "company";

    public static final int USER_LOGIN_FAIL_LOCK_COUNT = 5;
}
