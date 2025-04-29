package com.libin.spaced_learning_reminder.service;

import com.libin.spaced_learning_reminder.entity.LCProblemEntity;
import com.libin.spaced_learning_reminder.persistence.LeetcodeRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReminderService {

    private static final int DEFAULT_PAGE_SIZE = 50;
    private static final int DEFAULT_PAGE_NUMBER = 0;

    private final LeetcodeRepo leetcodeRepo;
    private final Set<Integer> frequency;

    public ReminderService(LeetcodeRepo leetcodeRepo, 
                         @Value("${app.reminder.frequency:3,9,19,29,45,120,220}") String frequencyConfig) {
        this.leetcodeRepo = leetcodeRepo;
        this.frequency = parseFrequencyConfig(frequencyConfig);
    }

    private Set<Integer> parseFrequencyConfig(String config) {
        if (config == null || config.trim().isEmpty()) {
            return Set.of(3, 9, 19, 29, 45, 120, 220); // Default frequencies
        }
        return Set.of(config.split(","))
                .stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }

    @Transactional
    public void add(LCProblemEntity lcProblem) {
        if (lcProblem == null) {
            throw new IllegalArgumentException("Problem cannot be null");
        }
        log.info("Adding new problem with ID: {}", lcProblem.getProblemId());
        validateProblem(lcProblem);
        cleanupExistingReminders(lcProblem);
        scheduleNewReminders(lcProblem);
    }

    private void validateProblem(LCProblemEntity lcProblem) {
        if (lcProblem == null) {
            log.error("Problem cannot be null");
            throw new IllegalArgumentException("Problem cannot be null");
        }
        if (lcProblem.getProblemId() == null) {
            log.error("Problem ID cannot be null");
            throw new IllegalArgumentException("Problem ID cannot be null");
        }
        if (lcProblem.getDescription() == null || lcProblem.getDescription().trim().isEmpty()) {
            log.error("Problem description cannot be null or empty");
            throw new IllegalArgumentException("Problem description cannot be null or empty");
        }
    }

    private void cleanupExistingReminders(LCProblemEntity lcProblem) {
        log.debug("Cleaning up existing reminders for problem: {}", lcProblem.getProblemId());
        int deletedCount = leetcodeRepo.deleteByProblemId(lcProblem.getProblemId());
        log.debug("Deleted {} existing reminders", deletedCount);
    }

    private void scheduleNewReminders(LCProblemEntity lcProblem) {
        LocalDateTime now = LocalDateTime.now();
        
        frequency.forEach(freq -> {
            LCProblemEntity newReminder = createReminder(lcProblem, now, freq);
            leetcodeRepo.save(newReminder);
            log.debug("Scheduled reminder for problem {} at {}", lcProblem.getProblemId(), newReminder.getScheduledTime());
        });
        
        log.info("Successfully added all reminders for problem: {}", lcProblem.getProblemId());
    }

    private LCProblemEntity createReminder(LCProblemEntity template, LocalDateTime baseTime, int daysToAdd) {
        return LCProblemEntity.builder()
                .problemId(template.getProblemId())
                .description(template.getDescription())
                .scheduledTime(baseTime.plusDays(daysToAdd))
                .comments(template.getComments())
                .build();
    }

    @Transactional(readOnly = true)
    public List<LCProblemEntity> getOverDueProblems() {
        log.info("Fetching overdue problems");
        LocalDateTime now = LocalDateTime.now();
        PageRequest page = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        
        List<LCProblemEntity> overdueProblems = leetcodeRepo
                .findByScheduledTimeBeforeOrderByScheduledTimeAsc(now, page)
                .getContent();
                
        log.info("Found {} overdue problems", overdueProblems.size());
        return overdueProblems;
    }

    @Transactional
    public void removeEarliest(Integer problemId) {
        if (problemId == null) {
            throw new IllegalArgumentException("Problem ID cannot be null");
        }
        log.info("Removing earliest reminder for problem: {}", problemId);
        
        leetcodeRepo.findFirstByProblemIdOrderByScheduledTimeAsc(problemId)
                .ifPresentOrElse(
                    this::deleteReminder,
                    () -> log.warn("No reminders found for problem: {}", problemId)
                );
    }

    private void deleteReminder(LCProblemEntity reminder) {
        leetcodeRepo.delete(reminder);
        log.info("Successfully removed reminder with ID: {} for problem: {}", 
                reminder.getId(), reminder.getProblemId());
    }

    @Transactional
    public void delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Reminder ID cannot be null");
        }
        log.info("Deleting reminder with ID: {}", id);
        leetcodeRepo.deleteById(id);
        log.info("Successfully deleted reminder with ID: {}", id);
    }

    @Transactional(readOnly = true)
    public List<LCProblemEntity> getAllProblems() {
        log.info("Fetching all problems");
        List<LCProblemEntity> allProblems = leetcodeRepo.findAll();
        log.info("Found {} total problems", allProblems.size());
        return allProblems;
    }

    @Transactional
    public void deleteAllProblems() {
        log.info("Deleting all problems");
        leetcodeRepo.deleteAll();
        log.info("Successfully deleted all problems");
    }
}
