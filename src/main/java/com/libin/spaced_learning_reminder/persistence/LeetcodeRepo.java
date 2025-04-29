package com.libin.spaced_learning_reminder.persistence;

import com.libin.spaced_learning_reminder.entity.LCProblemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeetcodeRepo extends JpaRepository<LCProblemEntity, Integer> {

    /**
     * Finds all reminders for a specific problem ID
     * @param problemId The ID of the problem
     * @return List of reminders for the given problem
     */
    List<LCProblemEntity> findByProblemId(Integer problemId);

    /**
     * Finds the earliest scheduled reminder for a specific problem
     * @param problemId The ID of the problem
     * @return Optional containing the earliest reminder if found
     */
    Optional<LCProblemEntity> findFirstByProblemIdOrderByScheduledTimeAsc(Integer problemId);

    /**
     * Finds all overdue reminders before the specified date
     * @param date The cutoff date
     * @param pageable Pagination information
     * @return Page of overdue reminders
     */
    Page<LCProblemEntity> findByScheduledTimeBeforeOrderByScheduledTimeAsc(
            LocalDateTime date, Pageable pageable);

    /**
     * Counts the number of reminders for a specific problem
     * @param problemId The ID of the problem
     * @return The count of reminders
     */
    long countByProblemId(Integer problemId);

    /**
     * Deletes all reminders for a specific problem
     * @param problemId The ID of the problem
     * @return The number of deleted reminders
     */
    @Modifying
    @Query("DELETE FROM LCProblemEntity l WHERE l.problemId = :problemId")
    int deleteByProblemId(@Param("problemId") Integer problemId);
}
