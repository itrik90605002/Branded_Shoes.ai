package com.storeagent.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(name = "contact_number", nullable = false, length = 20)
    private String contactNumber;

    @Column(name = "email_verified")
    private boolean emailVerified = false;

    @Column(name = "mobile_verified")
    private boolean mobileVerified = false;

    @Column(name = "email_verification_code", length = 50)
    private String emailVerificationCode;

    @Column(name = "mobile_otp", length = 20)
    private String mobileOtp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    public User() {}

    public User(Long id, String username, String password, String email, String contactNumber, boolean emailVerified, boolean mobileVerified, String emailVerificationCode, String mobileOtp, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.contactNumber = contactNumber;
        this.emailVerified = emailVerified;
        this.mobileVerified = mobileVerified;
        this.emailVerificationCode = emailVerificationCode;
        this.mobileOtp = mobileOtp;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public boolean isMobileVerified() {
        return mobileVerified;
    }

    public void setMobileVerified(boolean mobileVerified) {
        this.mobileVerified = mobileVerified;
    }

    public String getEmailVerificationCode() {
        return emailVerificationCode;
    }

    public void setEmailVerificationCode(String emailVerificationCode) {
        this.emailVerificationCode = emailVerificationCode;
    }

    public String getMobileOtp() {
        return mobileOtp;
    }

    public void setMobileOtp(String mobileOtp) {
        this.mobileOtp = mobileOtp;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {
        private Long id;
        private String username;
        private String password;
        private String email;
        private String contactNumber;
        private boolean emailVerified;
        private boolean mobileVerified;
        private String emailVerificationCode;
        private String mobileOtp;
        private Role role;

        public UserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder contactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
            return this;
        }

        public UserBuilder emailVerified(boolean emailVerified) {
            this.emailVerified = emailVerified;
            return this;
        }

        public UserBuilder mobileVerified(boolean mobileVerified) {
            this.mobileVerified = mobileVerified;
            return this;
        }

        public UserBuilder emailVerificationCode(String emailVerificationCode) {
            this.emailVerificationCode = emailVerificationCode;
            return this;
        }

        public UserBuilder mobileOtp(String mobileOtp) {
            this.mobileOtp = mobileOtp;
            return this;
        }

        public UserBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public User build() {
            return new User(id, username, password, email, contactNumber, emailVerified, mobileVerified, emailVerificationCode, mobileOtp, role);
        }
    }
}
