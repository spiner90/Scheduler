package ru.akorshunov.scheduler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.akorshunov.scheduler.converters.CustomJsonConverter;
import ru.akorshunov.scheduler.params.SchedulerProcessParam;
import ru.akorshunov.scheduler.params.SchedulerStepParam;

public class Main {
    public static void main(String[] args) {
        SchedulerStepParam param = new SchedulerStepParam("-1","1","CMD","Test");
        param.addCommand("C:\\Windows\\System32\\cmd.exe");
        param.addCommand("/c");
        param.addCommand("dir \"C:\\\"");
        SchedulerStepParam param2 = new SchedulerStepParam("1","2","SQL","Test");
        param2.addCommand("select sysdate CREATED from dual");
        param2.addCommand("select sysdate CREATED from dual");
        param2.addCommand("select sysdate CREATED from dual");
        SchedulerStepParam param3 = new SchedulerStepParam("2",null,"CMD","Test");
        param3.addCommand("C:\\Windows\\System32\\cmd.exe");
        param3.addCommand("/c");
        param3.addCommand("dir \"D:\\\"");

        SchedulerProcessParam procParam = new SchedulerProcessParam("130","LOAD_MAIN_RUS");
        procParam.addStepParam(param3);
        procParam.addStepParam(param);
        procParam.addStepParam(param2);
        SchedulerProcess proc = new SchedulerProcess(procParam);
        proc.start();
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(SchedulerProcessParam.class,new CustomJsonConverter());
        Gson gson = builder.create();
        String json = gson.toJson(procParam);
        SchedulerProcessParam processParamTest = gson.fromJson(json, SchedulerProcessParam.class);
        System.out.println(json);
        json = gson.toJson(procParam);
        System.out.println(json);
    }
}
