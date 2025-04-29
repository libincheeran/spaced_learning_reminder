package com.libin.spaced_learning_reminder.controller;

import com.libin.spaced_learning_reminder.entity.LCProblemEntity;
import com.libin.spaced_learning_reminder.service.ReminderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReminderControllerTest {

    @Mock
    private ReminderService reminderService;

    private ReminderController reminderController;

    @BeforeEach
    void setUp() {
        reminderController = new ReminderController(reminderService);
    }

    @Test
    void setReminder_ValidProblem_ReturnsOk() {
        // Arrange
        LCProblemEntity problem = createTestProblem();
        doNothing().when(reminderService).add(problem);

        // Act
        ResponseEntity<Void> response = reminderController.setReminder(problem);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        verify(reminderService).add(problem);
    }

    @Test
    void setReminder_NullProblem_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> reminderController.setReminder(null));
    }

    @Test
    void setReminder_ProblemWithNullId_ThrowsException() {
        // Arrange
        LCProblemEntity problem = createTestProblem();
        problem.setProblemId(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> reminderController.setReminder(problem));
    }

    @Test
    void setReminder_ProblemWithNullDescription_ThrowsException() {
        // Arrange
        LCProblemEntity problem = createTestProblem();
        problem.setDescription(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> reminderController.setReminder(problem));
    }

    @Test
    void setReminder_ProblemWithNullScheduledTime_ThrowsException() {
        // Arrange
        LCProblemEntity problem = createTestProblem();
        problem.setScheduledTime(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> reminderController.setReminder(problem));
    }

    @Test
    void getOverDueProblems_ReturnsProblems() {
        // Arrange
        List<LCProblemEntity> problems = List.of(createTestProblem());
        when(reminderService.getOverDueProblems()).thenReturn(problems);

        // Act
        ResponseEntity<List<LCProblemEntity>> response = reminderController.getOverDueProblems();

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(reminderService).getOverDueProblems();
    }

    @Test
    void getOverDueProblems_NoProblems_ReturnsEmptyList() {
        // Arrange
        when(reminderService.getOverDueProblems()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<LCProblemEntity>> response = reminderController.getOverDueProblems();

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(reminderService).getOverDueProblems();
    }

    @Test
    void markAsDone_ValidProblemId_ReturnsOk() {
        // Arrange
        Integer problemId = 1;
        doNothing().when(reminderService).removeEarliest(problemId);

        // Act
        ResponseEntity<Void> response = reminderController.markAsDone(problemId);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        verify(reminderService).removeEarliest(problemId);
    }

    @Test
    void markAsDone_NullProblemId_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> reminderController.markAsDone(null));
    }

    @Test
    void delete_ValidId_ReturnsOk() {
        // Arrange
        Integer id = 1;
        doNothing().when(reminderService).delete(id);

        // Act
        ResponseEntity<Void> response = reminderController.delete(id);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        verify(reminderService).delete(id);
    }

    @Test
    void delete_NullId_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> reminderController.delete(null));
    }

    private LCProblemEntity createTestProblem() {
        return LCProblemEntity.builder()
                .problemId(1)
                .description("Test Problem")
                .scheduledTime(LocalDateTime.now())
                .comments("Test Comments")
                .build();
    }
} 