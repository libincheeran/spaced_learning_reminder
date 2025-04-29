package com.libin.spaced_learning_reminder.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "leetcode_problems")
@Schema(description = "LeetCode problem entity with scheduled reminder")
public class LCProblemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the reminder", example = "1")
    private Integer id;

    @NotNull(message = "Problem ID cannot be null")
    @Schema(description = "LeetCode problem ID", example = "1")
    private Integer problemId;

    @Schema(description = "Scheduled time for the next reminder", example = "2024-03-20T10:00:00")
    private LocalDateTime scheduledTime;

    @NotBlank(message = "Description cannot be empty")
    @Schema(description = "Problem description or notes", example = "Two Sum - Easy")
    private String description;

    @Schema(description = "Additional comments about the problem", example = "Need to review hashmap approach")
    private String comments;
}
