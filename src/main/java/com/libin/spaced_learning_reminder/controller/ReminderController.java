package com.libin.spaced_learning_reminder.controller;

import com.libin.spaced_learning_reminder.entity.LCProblemEntity;
import com.libin.spaced_learning_reminder.service.ReminderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/lcProblem")
@CrossOrigin()
public class ReminderController {

    @Autowired
    private ReminderService reminderService;

    @PostMapping()
    public void setReminder(@RequestBody LCProblemEntity lcProblem) {
        validate(lcProblem);
        reminderService.add(lcProblem);
        System.out.println("remind.getLeetcodeId() = " + lcProblem.getProblemId());
    }

    private void validate(LCProblemEntity lcProblem) {
        if(StringUtils.isBlank(lcProblem.getDescription())) {
            throw new IllegalArgumentException("Description cannot be null");
        }
    }

    @GetMapping("/overdue")
    public List<LCProblemEntity> getOverDueProblems() {
        return reminderService.getOverDueProblems();
    }

    @DeleteMapping("/done/{problemId}")
    public void skip(@PathVariable("problemId") Integer problemId) {
        reminderService.removeEarliest(problemId);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Integer id) {
        reminderService.delete(id);
    }
}
