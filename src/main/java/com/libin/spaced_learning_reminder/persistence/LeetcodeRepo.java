package com.libin.spaced_learning_reminder.persistence;

import com.libin.spaced_learning_reminder.entity.LCProblemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeetcodeRepo extends JpaRepository<LCProblemEntity, Integer> {

    List<LCProblemEntity> findByProblemId(Integer problemId);
}
