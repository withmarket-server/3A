package com.market.aaa.config.security.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetails extends UserDetails {
    String getCompany();

    int loginFailCount();
}

