package com.example.skillnest.models;

import java.time.LocalDate;
import java.util.Optional;

public class CourseFilterOptions {
    private Optional<String> title;

    private Optional<String> topic;

    private Optional<LocalDate> startingDate;

    private Optional<String> sortBy;

    private Optional<String> sortOrder;

    public CourseFilterOptions() {
    }

    public CourseFilterOptions (String title,
                                String topic,
                                LocalDate startingDate,
                                String sortBy,
                                String sortOrder) {
        this.title = Optional.ofNullable(title);
        this.topic = Optional.ofNullable(topic);
        this.startingDate = Optional.ofNullable(startingDate);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getTitle() {
        return title;
    }

    public Optional<String> getTopic() {
        return topic;
    }

    public Optional<LocalDate> getStartingDate() {
        return startingDate;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}
