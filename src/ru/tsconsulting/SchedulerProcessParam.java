package ru.tsconsulting;

import java.util.ArrayList;
import java.util.HashMap;

public class SchedulerProcessParam {
    private String name;
    private String id;
    private HashMap<String,SchedulerStepParam> stepParams = new HashMap<String,SchedulerStepParam>();
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void addStepParam(SchedulerStepParam stepParam){
        this.stepParams.put(stepParam.getId(),stepParam);
    }

    public HashMap<String,SchedulerStepParam> getStepParams() {
        return stepParams;
    }
}
