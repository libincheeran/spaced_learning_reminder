package com.libin.spaced_learning_reminder.service;

import com.libin.spaced_learning_reminder.entity.LCProblemEntity;
import com.libin.spaced_learning_reminder.persistence.LeetcodeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReminderService {

    @Autowired
    LeetcodeRepo leetcodeRepo;

    private Set<Integer> frequency = Set.of(7,21,90,180,360);

    public void add(LCProblemEntity lcProblem){

        cleanup(lcProblem);

        LocalDateTime now = LocalDateTime.now();
        frequency.stream()
                .forEach(freq -> {
                    LCProblemEntity lcProblemEntity= new LCProblemEntity();
                    lcProblemEntity.setProblemId(lcProblem.getProblemId());
                    lcProblemEntity.setDescription(lcProblem.getDescription());
                    LocalDateTime newDate = now.plusDays(freq);
                    lcProblemEntity.setScheduledTime(newDate);
                    lcProblemEntity.setComments(lcProblem.getComments());
                    leetcodeRepo.save(lcProblemEntity);
                });
    }

    private void cleanup(LCProblemEntity lcProblem) {
        if(lcProblem.getProblemId() == null){
            return;
        }
        List<LCProblemEntity> byProblemId = leetcodeRepo.findByProblemId(lcProblem.getProblemId());

        byProblemId.stream()
                .forEach(e -> leetcodeRepo.delete(e));
    }

    public List<LCProblemEntity> getOverDueProblems() {
        LocalDateTime now = LocalDateTime.now();
        PageRequest page = PageRequest.of(0, 50);
        return leetcodeRepo.findAll(page).stream()
                .filter(record -> record.getScheduledTime().isBefore(now))
                .sorted(Comparator.comparing(LCProblemEntity::getScheduledTime))
                .collect(Collectors.toList());
    }

    public void removeEarliest(Integer problemId) {
        List<LCProblemEntity> byProblemId = leetcodeRepo.findByProblemId(problemId);

        Optional<LCProblemEntity> first = byProblemId.stream()
                .sorted(Comparator.comparing(LCProblemEntity::getScheduledTime))
                .findFirst();

        first.ifPresent((record) -> leetcodeRepo.delete(record));
    }

    public void delete(Integer id) {
        leetcodeRepo.deleteById(id);
    }
}
