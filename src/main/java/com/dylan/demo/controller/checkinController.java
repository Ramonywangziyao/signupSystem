package com.dylan.demo.controller;

import com.dylan.demo.model.Task;
import com.dylan.demo.service.CheckinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Author: Bussy
 * Creation Date: 2020/8/18
 * Copyright@Bussy
 **/
@Validated
@RestController
@RequestMapping("/checkInSystem")
public class checkinController {
    @Autowired
    CheckinService checkinService;

    @PostMapping("/newTask")
    public ResponseEntity<?> createNewTask(@NotNull @Valid @RequestBody Task task) {
        return checkinService.createNewTask(task);
    }

    @PostMapping("/continue")
    public ResponseEntity<?> continueTask(@NotNull @Valid @RequestParam("taskId") int taskId) {
        return checkinService.startWorkOnTask(taskId);
    }

    @GetMapping("/inprogress")
    public ResponseEntity<?> getInProgressTask() {
        return checkinService.getInProgressUnfinishedTask();
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentTask() {
        return checkinService.getCurrentTask();
    }

    @PostMapping("/stop")
    public ResponseEntity<?> stopTask(@NotNull @Valid @RequestParam("taskId") int taskId) {
        return checkinService.stopTask(taskId);
    }

    @PostMapping("/termination")
    public ResponseEntity<?> terminateTask(@NotNull @Valid @RequestParam("taskId") int taskId) {
        return checkinService.terminateTask(taskId);
    }

    @GetMapping("/stat")
    public ResponseEntity<?> getStat() {
        return checkinService.getStat();
    }

    @PostMapping("/payment")
    public ResponseEntity<?> payCompletedTasks(@NotNull @Valid @RequestParam("taskId") List<Integer> taskIds) {
        return checkinService.payCompletedTasks(taskIds);
    }

    @GetMapping("/unpaid")
    public ResponseEntity<?> getUnpaidTask() {
        return checkinService.getTaskUnpaid();
    }

}
