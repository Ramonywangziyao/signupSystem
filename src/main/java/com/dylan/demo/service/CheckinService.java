package com.dylan.demo.service;

import com.dylan.demo.dao.CheckinDao;
import com.dylan.demo.model.CurrentTask;
import com.dylan.demo.model.Task;
import com.dylan.demo.model.TaskProgress;
import com.dylan.demo.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.TimeZone;

/**
 * Author: Bussy
 * Creation Date: 2020/8/18
 * Copyright@Bussy
 **/
@Service
public class CheckinService {
    @Autowired
    CheckinDao checkinDao;

    @Transactional
    public ResponseEntity<?> createNewTask(Task task) {
        CurrentTask currentTask = checkinDao.selectCurrentTask();
        if(currentTask != null) {
            Task current = checkinDao.selectTaskById(currentTask.getTaskId());
            return ResultGenerator.getUnfinishedTaskErrorResult(current);
        }
        int inserted = checkinDao.insertNewTask(task);
        inserted = checkinDao.insertNewCurrentTask(task.getId());
        return ResultGenerator.getInsertResult(inserted);
    }

    @Transactional
    public ResponseEntity<?> startWorkOnTask(@NotNull @Valid @RequestParam("taskId") int taskId) {
        CurrentTask currentTask = checkinDao.selectCurrentTask();
        if(currentTask == null || currentTask.getTaskId() != taskId) {
            return ResultGenerator.getNoRunningTaskErrorResult();
        }
        TaskProgress unfinishedTaskProgresses = checkinDao.selectTaskProgressWitoutEndTimeByTaskId(taskId);
        if(unfinishedTaskProgresses != null && unfinishedTaskProgresses.getTaskId() == taskId) {
            Task current = checkinDao.selectTaskById(unfinishedTaskProgresses.getTaskId());
            return ResultGenerator.getUnfinishedTaskErrorResult(current);
        }
        int inserted = checkinDao.insertNewTaskProgress(taskId);
        return ResultGenerator.getStartedWorkResult(taskId);
    }

    @Transactional
    public ResponseEntity<?> getCurrentTask() {
        CurrentTask currentTask = checkinDao.selectCurrentTask();
        if(currentTask == null) {
            return ResultGenerator.getNoRunningTaskErrorResult();
        }
        Task task = checkinDao.selectTaskById(currentTask.getTaskId());
        TaskProgress lastTaskProgress = checkinDao.selectMostRecentTaskProgressByTaskId(task.getId());
        return ResultGenerator.getCombinedTaskInfoResult(task, lastTaskProgress);
    }

    @Transactional
    public ResponseEntity<?> getInProgressUnfinishedTask() {
        CurrentTask currentTask = checkinDao.selectCurrentTask();
        if(currentTask == null) {
            return ResultGenerator.getNoRunningTaskErrorResult();
        }
        TaskProgress unfinishedTaskProgresses = checkinDao.selectTaskProgressWitoutEndTimeByTaskId(currentTask.getTaskId());
        if(unfinishedTaskProgresses == null) {
            return ResultGenerator.getNoUnfinishedTaskErrorResult();
        }
        Task task = checkinDao.selectTaskById(unfinishedTaskProgresses.getTaskId());
        Timestamp startTime = unfinishedTaskProgresses.getStartTime();
        return ResultGenerator.getUnfinishedTaskResult(task, startTime);
    }

    @Transactional
    public ResponseEntity<?> stopTask(@NotNull @Valid @RequestParam("taskId") int taskId) {
        CurrentTask currentTask = checkinDao.selectCurrentTask();
        if(currentTask == null || currentTask.getTaskId() != taskId) {
            return ResultGenerator.getNoRunningTaskErrorResult();
        }
        TaskProgress unfinishedTaskProgresses = checkinDao.selectTaskProgressWitoutEndTimeByTaskId(taskId);
        if(unfinishedTaskProgresses == null || unfinishedTaskProgresses.getTaskId() != taskId) {
            return ResultGenerator.getNoUnfinishedTaskErrorResult();
        }
        int updated = checkinDao.updateTaskProgressEndTimeById(unfinishedTaskProgresses.getId());
        Task task = checkinDao.selectTaskById(taskId);
        double totalTime = calculateSeconds(unfinishedTaskProgresses.getId());
        updated = checkinDao.updateHourById(totalTime, task.getId());
        return ResultGenerator.getStoppedTaskResult(taskId);
    }

    @Transactional
    public ResponseEntity<?> terminateTask(@NotNull @Valid @RequestParam("taskId") int taskId) {
        CurrentTask currentTask = checkinDao.selectCurrentTask();
        if(currentTask == null || taskId != currentTask.getTaskId()) {
            return ResultGenerator.getNoRunningTaskErrorResult();
        }
        TaskProgress unfinishedTaskProgresses = checkinDao.selectTaskProgressWitoutEndTimeByTaskId(taskId);
        if(unfinishedTaskProgresses != null && unfinishedTaskProgresses.getTaskId() == taskId) {
            Task current = checkinDao.selectTaskById(currentTask.getTaskId());
            return ResultGenerator.getUnfinishedTaskErrorResult(current);
        }
        int numberOfItem = checkinDao.selectTaskById(taskId).getNumberOfItem();
        int updated = checkinDao.updateTaskToCompleteById(taskId);
        Double tempTime = checkinDao.selectTotalTimeOfTaskById(taskId);
        double totalTime = tempTime == null ? 0 : tempTime;
        double income = calculateIncome(totalTime, numberOfItem);
        updated = checkinDao.updateIncomeById(taskId, income);
        int deletedTask = checkinDao.deleteCurrentTaskById(taskId);
        updated = checkinDao.updateTaskTerminateTimeById(taskId);
        updated = checkinDao.deleteNotWorkedTask();
        return ResultGenerator.getTerminatedTaskResult(taskId);
    }

    public ResponseEntity<?> getStat() {
        List<Task> tasks = checkinDao.selectAllTasks();
        return ResultGenerator.getAllStatResult(tasks);
    }

    public ResponseEntity<?> getTaskUnpaid() {
        List<Task> unpaidTasks = checkinDao.selectAllUnpaidCompletedTasks();
        return ResultGenerator.getAllUnpaidResult(unpaidTasks);
    }

    @Transactional
    public ResponseEntity<?> payCompletedTasks(@NotNull @Valid @RequestParam("taskId") List<Integer> taskIds) {
        for(int taskId: taskIds) {
            int updated = checkinDao.updateToPaidByTaskId(taskId);
        }
        return ResultGenerator.getPaidResult(taskIds);
    }

    private double calculateSeconds(int taskProgressId) {
        int seconds = checkinDao.selectTaskProgressTimeDurationById(taskProgressId);
        return seconds;
    }

    public double calculateIncome(double seconds, int numberOfItem) {
        int hourSalary = 14;
        int min = (int)(seconds / 60);
        int hour = min / 60;
        double duration = (double)hour + (double)(min % 60)/(double)60;
        double maxHour = (double)numberOfItem / 15;
        double actualHour = Math.min(duration, maxHour);
        return hourSalary * actualHour;
    }

}
