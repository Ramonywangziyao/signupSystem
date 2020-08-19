package com.dylan.demo.model;

import lombok.Data;

/**
 * Author: Bussy
 * Creation Date: 2020/8/18
 * Copyright@Bussy
 **/
@Data
public class Task {
    private int id;
    private int numberOfItem;
    private double income;
    private int paied;
    private int completed;
    private double totalTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumberOfItem() {
        return numberOfItem;
    }

    public void setNumberOfItem(int numberOfItem) {
        this.numberOfItem = numberOfItem;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public int getPaied() {
        return paied;
    }

    public void setPaied(int paied) {
        this.paied = paied;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }
}
