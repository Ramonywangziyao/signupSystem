package com.dylan.demo.dao;

import com.dylan.demo.model.CurrentTask;
import com.dylan.demo.model.Task;
import com.dylan.demo.model.TaskProgress;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Bussy
 * Creation Date: 2020/8/18
 * Copyright@Bussy
 **/
@Repository
@Mapper
public interface CheckinDao {
    @Select("select * from CurrentTask limit 1")
    CurrentTask selectCurrentTask();
    
    @Select("select * from TaskProgress where endTime is null AND taskId = #{taskId} limit 1")
    TaskProgress selectTaskProgressWitoutEndTimeByTaskId(@Param("taskId") int taskId);
    
    @Select("select * from Task where id = #{id}")
    Task selectTaskById(@Param("id") int id);
    
    @Select("select * from TaskProgress where taskId = #{taskId} order by createDate desc limit 1")
    TaskProgress selectMostRecentTaskProgressByTaskId(@Param("taskId") int taskId);
    
    @Select("select * from Task order by createDate desc")
    List<Task> selectAllTasks();
    
    @Select("select * from Task where paid = 0 and completed = 1")
    List<Task> selectAllUnpaidCompletedTasks();
    
    @Select("select totalTime from Task where id = #{id}")
    Double selectTotalTimeOfTaskById(@Param("id") int id);
    
    @Select("select TIMESTAMPDIFF(SECOND,(select startTime from TaskProgress where id = #{id}),(select endTime from TaskProgress where id = #{id}))")
    int selectTaskProgressTimeDurationById(@Param("id") int id);
    
    @Insert("insert into Task(numberOfItem) values(#{numberOfItem})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertNewTask(Task task);
    
    @Insert("insert into CurrentTask(taskId) values(#{taskId})")
    int insertNewCurrentTask(@Param("taskId") int taskId);
    
    @Insert("insert into TaskProgress(taskId) values(#{taskId})")
    int insertNewTaskProgress(@Param("taskId") int taskId);
    
    @Delete("delete from CurrentTask where taskId = #{taskId}")
    int deleteCurrentTaskById(@Param("taskId") int taskId);
    
    @Update("update TaskProgress set endTime = CURRENT_TIMESTAMP where id = #{id}")
    int updateTaskProgressEndTimeById(int id);
    
    @Update("update Task set totalTime = totalTime + #{totalTime} where id = #{id}")
    int updateHourById(@Param("totalTime") double totalTime, @Param("id") int id);
    
    @Update("update Task set completed = 1 where id = #{id}")
    int updateTaskToCompleteById(int taskId);
    
    @Update("update Task set income = #{income} where id = #{id}")
    int updateIncomeById(@Param("id") int id, @Param("income") double income);
    
    @Update("update Task set paid = 1 where id = #{id}")
    int updateToPaidByTaskId(@Param("id") int id);

    @Update("update Task set terminateDate = (select endTime from TaskProgress where taskId = #{id} order by endTime desc limit 1) where id = #{id}")
    int updateTaskTerminateTimeById(@Param("id") int id);
}
