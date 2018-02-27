package ru.akorshunov.params;

import java.util.ArrayList;

public class SchedulerStepParam {
    private String id;
    private String nextId;
    private String name;
    private String type;
    private ArrayList<String> commands;
    private ArrayList<SchedulerStepParam> childSteps = new ArrayList<SchedulerStepParam>();
    public SchedulerStepParam(String id,String nextId,String type,String name){
        this.id = id;
        this.nextId = nextId;
        this.type = type;
        this.name = name;
    }
    public void setId(String id){
        this.id = id;
    }
    public void setNextId(String id){
        this.nextId = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setType(String type){
        this.type = type;
    }
    public void addChildStep(SchedulerStepParam step){
        if(childSteps == null)
            childSteps = new ArrayList<SchedulerStepParam>();
        childSteps.add(step);
    }
    public String getType(){
        return type;
    }
    public String getId(){
        return id;
    }
    public String getNextId(){
        return nextId;
    }
    public String getName(){
        return name;
    }
    public ArrayList<String> getCommands(){
        return commands;
    }
    public ArrayList<SchedulerStepParam> getChildSteps(){
        return childSteps;
    }
    public void addCommand(String command){
        if(commands == null)
            commands = new ArrayList<String>();
        commands.add(command);
    }
}
