package com.example.skillnest.models.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CourseDto {
    @NotNull(message = "The course must have a title.")
    @Size(min = 16, max = 64, message = "The title must be between 16 and 64 symbols")
    private String title;

    @NotNull
    private String topic;

    @NotNull(message = "The course must have a content.")
    @Size(min = 4, max = 8192, message = "The content must be between 32 symbols and 8192 symbols.")
    private String content;

    @NotNull
    private LocalDate starting_date;

    public CourseDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public LocalDate getStarting_date() {
        return starting_date;
    }

    public void setStarting_date(LocalDate starting_date) {
        this.starting_date = starting_date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
