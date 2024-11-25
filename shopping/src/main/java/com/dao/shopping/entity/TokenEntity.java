package com.dao.shopping.entity;

import com.dao.shopping.enums.TokenType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "token")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenEntity extends BaseEntity{

    String token;

    boolean isExpired;

    boolean isRevoke;

    @Enumerated(EnumType.STRING)
    TokenType tokenType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public boolean isRevoke() {
        return isRevoke;
    }

    public void setRevoke(boolean revoke) {
        isRevoke = revoke;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}

