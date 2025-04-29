package com.libin.spaced_learning_reminder.persistence;

import com.libin.spaced_learning_reminder.config.TestConfig;
import com.libin.spaced_learning_reminder.entity.LCProblemEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestConfig.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class LeetcodeRepoTest {

    @Autowired
    private LeetcodeRepo leetcodeRepo;

    private static final int TEST_PROBLEM_ID = 1;
    private static final String TEST_DESCRIPTION = "Test Problem";
    private static final String TEST_COMMENTS = "Test Comments";

    @BeforeEach
    void setUp() {
        leetcodeRepo.deleteAll();
    }

    @Test
    void findByProblemId_ExistingProblem_ReturnsReminders() {
        // Arrange
        LCProblemEntity problem = createTestProblem();
        leetcodeRepo.save(problem);

        // Act
        List<LCProblemEntity> result = leetcodeRepo.findByProblemId(problem.getProblemId());

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(problem.getProblemId(), result.get(0).getProblemId());
    }

    @Test
    void findFirstByProblemIdOrderByScheduledTimeAsc_ExistingProblem_ReturnsEarliestReminder() {
        // Arrange
        LCProblemEntity problem1 = createTestProblem();
        LCProblemEntity problem2 = createTestProblem();
        problem2.setScheduledTime(problem1.getScheduledTime().plusDays(1));
        leetcodeRepo.save(problem1);
        leetcodeRepo.save(problem2);

        // Act
        Optional<LCProblemEntity> result = leetcodeRepo.findFirstByProblemIdOrderByScheduledTimeAsc(problem1.getProblemId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(problem1.getScheduledTime(), result.get().getScheduledTime());
    }

    @Test
    void findByScheduledTimeBeforeOrderByScheduledTimeAsc_OverdueProblems_ReturnsProblems() {
        // Arrange
        LCProblemEntity overdueProblem = createTestProblem();
        overdueProblem.setScheduledTime(LocalDateTime.now().minusDays(1));
        leetcodeRepo.save(overdueProblem);

        // Act
        var result = leetcodeRepo.findByScheduledTimeBeforeOrderByScheduledTimeAsc(
                LocalDateTime.now(), PageRequest.of(0, 10));

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void countByProblemId_ExistingProblem_ReturnsCount() {
        // Arrange
        LCProblemEntity problem = createTestProblem();
        leetcodeRepo.save(problem);

        // Act
        long count = leetcodeRepo.countByProblemId(problem.getProblemId());

        // Assert
        assertEquals(1, count);
    }

    @Test
    void deleteByProblemId_ExistingProblem_DeletesAllReminders() {
        // Arrange
        LCProblemEntity problem1 = createTestProblem();
        LCProblemEntity problem2 = createTestProblem();
        leetcodeRepo.save(problem1);
        leetcodeRepo.save(problem2);

        // Act
        int deletedCount = leetcodeRepo.deleteByProblemId(TEST_PROBLEM_ID);

        // Assert
        assertEquals(2, deletedCount);
        assertEquals(0, leetcodeRepo.countByProblemId(TEST_PROBLEM_ID));
    }

    private LCProblemEntity createTestProblem() {
        return LCProblemEntity.builder()
                .problemId(TEST_PROBLEM_ID)
                .description(TEST_DESCRIPTION)
                .scheduledTime(LocalDateTime.now())
                .comments(TEST_COMMENTS)
                .build();
    }
} 