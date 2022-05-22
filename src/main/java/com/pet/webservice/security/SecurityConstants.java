package com.pet.webservice.security;

public class SecurityConstants {

    /* this using in order for user to auth on this URL (** - any) */
    public static final String SIGN_UP_URLS = "/api/auth/**";

    /* this all necessary for generation JWT*/
    public static final String SECRET = "SecretKeyGenJWT";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String CONTENT_TYPE = "application/json";
}
