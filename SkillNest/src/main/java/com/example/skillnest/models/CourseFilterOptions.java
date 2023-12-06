package com.example.skillnest.models;

import java.util.Date;
import java.util.Optional;

public class CourseFilterOptions {
    private Optional<String> title;

    private Optional<String> topic;

    private Optional<Date> startingDate;

    private Optional<String> sortBy;

    private Optional<String> sortOrder;

    public CourseFilterOptions (String title,
                                String topic,
                                Date startingDate,
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

    public Optional<Date> getStartingDate() {
        return startingDate;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}
