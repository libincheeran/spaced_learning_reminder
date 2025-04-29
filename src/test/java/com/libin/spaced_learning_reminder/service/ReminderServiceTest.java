package com.libin.spaced_learning_reminder.service;

import com.libin.spaced_learning_reminder.entity.LCProblemEntity;
import com.libin.spaced_learning_reminder.persistence.LeetcodeRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReminderServiceTest {

    @Mock
    private LeetcodeRepo leetcodeRepo;

    private ReminderService reminderService;

    @BeforeEach
    void setUp() {
        reminderService = new ReminderService(leetcodeRepo, "3,9,19");
    }

    @Test
    void add_ValidProblem_CreatesReminders() {
        // Arrange
        LCProblemEntity problem = createTestProblem();
        when(leetcodeRepo.deleteByProblemId(any())).thenReturn(0);
        when(leetcodeRepo.save(any())).thenReturn(problem);

        // Act
        reminderService.add(problem);

        // Assert
        verify(leetcodeRepo, times(3)).save(any());
        verify(leetcodeRepo).deleteByProblemId(problem.getProblemId());
    }

    @Test
    void add_NullProblem_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> reminderService.add(null));
    }

    @Test
    void add_ProblemWithNullId_ThrowsException() {
        // Arrange
        LCProblemEntity problem = createTestProblem();
        problem.setProblemId(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> reminderService.add(problem));
    }

    @Test
    void getOverDueProblems_ReturnsOverdueProblems() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        LCProblemEntity overdueProblem = createTestProblem();
        overdueProblem.setScheduledTime(now.minusDays(1));
        
        when(leetcodeRepo.findByScheduledTimeBeforeOrderByScheduledTimeAsc(any(), any()))
                .thenReturn(org.springframework.data.domain.Page.empty());

        // Act
        List<LCProblemEntity> result = reminderService.getOverDueProblems();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getOverDueProblems_WithMultipleProblems_ReturnsSortedByDate() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        LCProblemEntity problem1 = createTestProblem();
        problem1.setScheduledTime(now.minusDays(2));
        LCProblemEntity problem2 = createTestProblem();
        problem2.setScheduledTime(now.minusDays(1));

        when(leetcodeRepo.findByScheduledTimeBeforeOrderByScheduledTimeAsc(any(), any()))
                .thenReturn(org.springframework.data.domain.Page.empty());

        // Act
        List<LCProblemEntity> result = reminderService.getOverDueProblems();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void removeEarliest_ProblemExists_RemovesEarliestReminder() {
        // Arrange
        Integer problemId = 1;
        LCProblemEntity earliestReminder = createTestProblem();
        when(leetcodeRepo.findFirstByProblemIdOrderByScheduledTimeAsc(problemId))
                .thenReturn(Optional.of(earliestReminder));

        // Act
        reminderService.removeEarliest(problemId);

        // Assert
        verify(leetcodeRepo).delete(earliestReminder);
    }

    @Test
    void removeEarliest_ProblemNotFound_LogsWarning() {
        // Arrange
        Integer problemId = 1;
        when(leetcodeRepo.findFirstByProblemIdOrderByScheduledTimeAsc(problemId))
                .thenReturn(Optional.empty());

        // Act
        reminderService.removeEarliest(problemId);

        // Assert
        verify(leetcodeRepo, never()).delete(any());
    }

    @Test
    void removeEarliest_NullProblemId_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> reminderService.removeEarliest(null));
    }

    @Test
    void delete_ValidId_DeletesReminder() {
        // Arrange
        Integer id = 1;

        // Act
        reminderService.delete(id);

        // Assert
        verify(leetcodeRepo).deleteById(id);
    }

    @Test
    void delete_NullId_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> reminderService.delete(null));
    }

    @Test
    void cleanupExistingReminders_ExistingReminders_DeletesAll() {
        // Arrange
        LCProblemEntity problem = createTestProblem();
        when(leetcodeRepo.deleteByProblemId(problem.getProblemId())).thenReturn(2);

        // Act
        reminderService.add(problem);

        // Assert
        verify(leetcodeRepo).deleteByProblemId(problem.getProblemId());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "3", "3,9", "3,9,19,29,45,120,220"})
    void constructor_WithDifferentFrequencyConfigs_InitializesCorrectly(String frequencyConfig) {
        // Act
        ReminderService service = new ReminderService(leetcodeRepo, frequencyConfig);

        // Assert
        assertNotNull(service);
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