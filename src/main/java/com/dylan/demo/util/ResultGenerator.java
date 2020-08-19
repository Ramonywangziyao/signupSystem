package com.dylan.demo.util;

import com.dylan.demo.model.Task;
import com.dylan.demo.model.TaskProgress;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Bussy
 * Creation Date: 2020/8/18
 * Copyright@Bussy
 **/
public class ResultGenerator {

    public static ResponseEntity<?> getUnfinishedTaskErrorResult(Task current) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("timestamp", generateCurrentTimestamp());
        resultMap.put("error", "unfinished task exist error");
        resultMap.put("code", -3);
        return new ResponseEntity<>(resultMap, HttpStatus.CONFLICT);
    }

    public static ResponseEntity<?> getInsertResult(int inserted) {
        if(inserted == 1) {
            return getOkResult();
        } else {
            return getInternalErrorResult();
        }
    }

    public static ResponseEntity<?> getNoRunningTaskErrorResult() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("timestamp", generateCurrentTimestamp());
        resultMap.put("error", "no running task error");
        resultMap.put("code", -1);
        return new ResponseEntity<>(resultMap, HttpStatus.NOT_FOUND);
    }


    public static ResponseEntity<?> getNoUnfinishedTaskErrorResult() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("timestamp", generateCurrentTimestamp());
        resultMap.put("error", "no unfinished error");
        resultMap.put("code", 0);
        return new ResponseEntity<>(resultMap, HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<?> getCombinedTaskInfoResult(Task task, TaskProgress lastTaskProgress) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("timestamp", generateCurrentTimestamp());
        resultMap.put("task", task);
        Timestamp startTime = lastTaskProgress == null ? null : lastTaskProgress.getStartTime();
        Timestamp endTime = lastTaskProgress == null ? null : lastTaskProgress.getEndTime();
        String start = startTime != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((new Date(startTime.getTime()))) : null;
        String end = endTime != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((new Date(endTime.getTime()))) : null;
        resultMap.put("startTime", start);
        resultMap.put("endTime", end);
        resultMap.put("code", 1);
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    public static ResponseEntity<?> getUnfinishedTaskResult(Task task, Timestamp startTime) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("timestamp", generateCurrentTimestamp());
        resultMap.put("task", task);
        String start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((new Date(startTime.getTime())));
        resultMap.put("startTime", start);
        resultMap.put("code", 1);
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    public static ResponseEntity<?> getTerminatedTaskResult(int taskId) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("timestamp", generateCurrentTimestamp());
        resultMap.put("terminated", taskId);
        resultMap.put("code", 1);
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    public static ResponseEntity<?> getAllStatResult(List<Task> tasks) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("timestamp", generateCurrentTimestamp());
        resultMap.put("tasks", tasks);
        resultMap.put("code", 1);
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    public static ResponseEntity<?> getAllUnpaidResult(List<Task> unpaidTasks) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("timestamp", generateCurrentTimestamp());
        resultMap.put("unpaid", unpaidTasks);
        resultMap.put("code", 1);
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    public static ResponseEntity<?> getPaidResult(List<Integer> taskIds) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("timestamp", generateCurrentTimestamp());
        resultMap.put("paid", taskIds);
        resultMap.put("code", 1);
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    private static ResponseEntity<?> getOkResult() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("timestamp", generateCurrentTimestamp());
        resultMap.put("code", 1);
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    private static ResponseEntity<?> getInternalErrorResult() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("timestamp", generateCurrentTimestamp());
        resultMap.put("code", -2);
        return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static String generateCurrentTimestamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = new java.util.Date();
        return formatter.format(new Timestamp(date.getTime()));
    }

    public static ResponseEntity<?> getStoppedTaskResult(int taskId) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("timestamp", generateCurrentTimestamp());
        resultMap.put("stopped", taskId);
        resultMap.put("code", 1);
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    public static ResponseEntity<?> getStartedWorkResult(int taskId) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("timestamp", generateCurrentTimestamp());
        resultMap.put("started", taskId);
        resultMap.put("code", 1);
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

}
