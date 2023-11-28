package com.example.skillnest.models;

import java.util.Optional;

public class UserFilterOptions {

    private final Optional<String> firstName;

    private final Optional<String> lastName;

    private final Optional<String> email;

    private final Optional<String> sortBy;

    private final Optional<String> sortOrder;

    public UserFilterOptions(String firstName,
                             String lastName,
                             String email,
                             String sortBy,
                             String sortOrder) {
        this.firstName = Optional.ofNullable(firstName);
        this.lastName = Optional.ofNullable(lastName);
        this.email = Optional.ofNullable(email);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getFirstName() {
        return firstName;
    }

    public Optional<String> getLastName() {
        return lastName;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}
