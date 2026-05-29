package com.generation153.harmonyfree.core.security.model;

public class CustomUserPrincipal {
	
	private final Long userId;
    private final String email;

    public CustomUserPrincipal(Long userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

}
