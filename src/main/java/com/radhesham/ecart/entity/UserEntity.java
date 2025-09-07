package com.radhesham.ecart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Table(name = "m_users")
@Entity
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "password")
    private String password;

    @Column(name="enable",columnDefinition = "INT DEFAULT 0")
    private int enable;

    @Column(name="credential_non_expired",columnDefinition = "INT DEFAULT 0")
    private int credentialNonExpired;

    @Column(name="account_non_lock",columnDefinition = "INT DEFAULT 0")
    private int accountNonLock;

    @Column(name="account_non_expired",columnDefinition = "INT DEFAULT 0")
    private int accountNonExpired;

    @Column(name="remarks",columnDefinition = "varchar(255) default ''")
    private String remarks;

    @Column(name = "insert_time", updatable = false)
    @CreationTimestamp()
    private Date insertTime;

    @Column(name = "update_time", insertable = false)
    @UpdateTimestamp
    private Date updateTime;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return getAccountNonExpired()>0;
    }

    @Override
    public boolean isAccountNonLocked() {
        return getAccountNonLock()>0;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return getCredentialNonExpired()>0;
    }

    @Override
    public boolean isEnabled() {
        return getEnable()>0;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", enable=" + enable +
                ", credentialNonExpired=" + credentialNonExpired +
                ", accountNonLock=" + accountNonLock +
                ", accountNonExpired=" + accountNonExpired +
                ", remarks='" + remarks + '\'' +
                ", insertTime=" + insertTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
