package com.librarydesk.domain;

import java.util.UUID;

public class Member {
    private final String id;
    private String name;
    private String email;
    private String phone;

    public Member(String name, String email, String phone) {
        this(UUID.randomUUID().toString(), name, email, phone);
    }

    public Member(String id, String name, String email, String phone) {
        this.id = id;
        setName(name);
        setEmail(email);
        setPhone(phone);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) throw new ValidationException("Name required");
        this.name = name.trim();
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")) throw new ValidationException("Invalid email");
        this.email = email.trim();
    }

    public void setPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) throw new ValidationException("Phone required");
        this.phone = phone.trim();
    }

    @Override
    public String toString() {
        return String.format("%s - %s <%s>", id, name, email);
    }
}
