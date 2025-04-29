package com.libin.spaced_learning_reminder.controller;

import com.libin.spaced_learning_reminder.entity.LCProblemEntity;
import com.libin.spaced_learning_reminder.service.ReminderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/lcProblem")
@CrossOrigin()
@RequiredArgsConstructor
@Tag(name = "LeetCode Problem Reminder", description = "API for managing LeetCode problem reminders with spaced repetition")
public class ReminderController {

    private final ReminderService reminderService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Set a new reminder for a LeetCode problem",
            description = "Creates a new reminder for a LeetCode problem with the specified schedule. " +
                    "The system will automatically create multiple reminders based on the spaced repetition algorithm."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reminder set successfully",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                        "problemId": "Problem ID cannot be null",
                                        "description": "Description cannot be empty",
                                        "scheduledTime": "Scheduled time cannot be null"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                        "error": "An unexpected error occurred"
                                    }
                                    """)
                    )
            )
    })
    public ResponseEntity<Void> setReminder(
            @Parameter(
                    description = "LeetCode problem details",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = LCProblemEntity.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "problemId": 1,
                                        "description": "Two Sum - Easy",
                                        "scheduledTime": "2024-03-20T10:00:00",
                                        "comments": "Need to review hashmap approach"
                                    }
                                    """)
                    )
            )
            @Valid @RequestBody LCProblemEntity lcProblem) {
        if (lcProblem == null) {
            throw new IllegalArgumentException("Problem cannot be null");
        }
        if (lcProblem.getProblemId() == null) {
            throw new IllegalArgumentException("Problem ID cannot be null");
        }
        if (lcProblem.getDescription() == null || lcProblem.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Problem description cannot be null or empty");
        }
        log.info("Received request to set reminder for problem: {}", lcProblem.getProblemId());
        reminderService.add(lcProblem);
        log.info("Successfully set reminder for problem: {}", lcProblem.getProblemId());
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/overdue", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get all overdue problems",
            description = "Retrieves a list of all LeetCode problems that are past their scheduled review time. " +
                    "Problems are sorted by their scheduled time in ascending order."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved overdue problems",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LCProblemEntity.class),
                            examples = @ExampleObject(value = """
                                    [
                                        {
                                            "id": 1,
                                            "problemId": 1,
                                            "description": "Two Sum - Easy",
                                            "scheduledTime": "2024-03-19T10:00:00",
                                            "comments": "Need to review hashmap approach"
                                        }
                                    ]
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                        "error": "An unexpected error occurred"
                                    }
                                    """)
                    )
            )
    })
    public ResponseEntity<List<LCProblemEntity>> getOverDueProblems() {
        log.info("Fetching overdue problems");
        List<LCProblemEntity> problems = reminderService.getOverDueProblems();
        log.info("Found {} overdue problems", problems.size());
        return ResponseEntity.ok(problems);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get all problems",
            description = "Retrieves a list of all LeetCode problems in the system, regardless of their scheduled time."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved all problems",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LCProblemEntity.class),
                            examples = @ExampleObject(value = """
                                    [
                                        {
                                            "id": 1,
                                            "problemId": 1,
                                            "description": "Two Sum - Easy",
                                            "scheduledTime": "2024-03-19T10:00:00",
                                            "comments": "Need to review hashmap approach"
                                        }
                                    ]
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                        "error": "An unexpected error occurred"
                                    }
                                    """)
                    )
            )
    })
    public ResponseEntity<List<LCProblemEntity>> getAllProblems() {
        log.info("Fetching all problems");
        List<LCProblemEntity> problems = reminderService.getAllProblems();
        log.info("Found {} total problems", problems.size());
        return ResponseEntity.ok(problems);
    }

    @DeleteMapping("/all")
    @Operation(
            summary = "Delete all problems",
            description = "Permanently deletes all problems and their reminders from the system."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "All problems deleted successfully",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                        "error": "An unexpected error occurred"
                                    }
                                    """)
                    )
            )
    })
    public ResponseEntity<Void> deleteAllProblems() {
        log.info("Received request to delete all problems");
        reminderService.deleteAllProblems();
        log.info("Successfully deleted all problems");
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/done/{problemId}")
    @Operation(
            summary = "Mark a problem as done",
            description = "Marks the earliest scheduled reminder for a specific problem as completed. " +
                    "This will remove the reminder from the system."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Problem marked as done successfully",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid problem ID",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                        "error": "Problem ID cannot be null"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                        "error": "An unexpected error occurred"
                                    }
                                    """)
                    )
            )
    })
    public ResponseEntity<Void> markAsDone(
            @Parameter(
                    description = "ID of the problem to mark as done",
                    required = true,
                    example = "1"
            )
            @PathVariable("problemId") Integer problemId) {
        if (problemId == null) {
            throw new IllegalArgumentException("Problem ID cannot be null");
        }
        log.info("Marking problem {} as done", problemId);
        reminderService.removeEarliest(problemId);
        log.info("Successfully marked problem {} as done", problemId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Delete a reminder",
            description = "Permanently deletes a specific reminder from the system."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reminder deleted successfully",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid reminder ID",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                        "error": "Reminder ID cannot be null"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                        "error": "An unexpected error occurred"
                                    }
                                    """)
                    )
            )
    })
    public ResponseEntity<Void> delete(
            @Parameter(
                    description = "ID of the reminder to delete",
                    required = true,
                    example = "1"
            )
            @PathVariable("id") Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Reminder ID cannot be null");
        }
        log.info("Deleting problem with id: {}", id);
        reminderService.delete(id);
        log.info("Successfully deleted problem with id: {}", id);
        return ResponseEntity.ok().build();
    }
}
