package com.example.CanbanDesk.Models.Enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority
{
    EMPL,MNG,ADM;

    @Override
    public String getAuthority() {
        return name();
    }
}
