package ru.tsconsulting;

import oracle.jdbc.pool.OracleDataSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;

public class SchedulerStep extends Thread {
    private SchedulerStepParam param;
    SchedulerStep(SchedulerStepParam param){
        this.param=param;
    }
    private ArrayList<SchedulerStep> childSteps;
    @Override
    public void run() {
        switch (param.getType()){
            case "SQL":
                runSQL(param.getCommands());
                break;
            case "CMD":
                runCMD(param.getCommands());
                break;
            case "SUBSTEPS":
                runChildSteps(param.getChildSteps());
                break;
        }
    }
    private void runSQL(ArrayList<String> commands) {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            OracleDataSource ods = new OracleDataSource();
            ods.setURL("jdbc:oracle:thin:@bcvm496:1521:sieb496");
            ods.setUser("SIEBEL");
            ods.setPassword("SIEBEL");
            Connection sqlConnection = ods.getConnection();


            for (Iterator<String> i = commands.iterator();i.hasNext();)
            {
                Statement statement = sqlConnection.createStatement();
                ResultSet rs = statement.executeQuery(i.next());
                while (rs.next()){
                    System.out.println("date:"+rs.getString("CREATED"));
                }
                rs.close();
                statement.close();
            }
            sqlConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void runCMD(ArrayList<String> commands){
        ProcessBuilder builder = new ProcessBuilder(commands);
        builder.redirectErrorStream(true);
        Process p = null;
        try {
            p = builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            try {
                line = r.readLine();
                if (line == null) {
                    break;
                }
                System.out.println(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void runChildSteps(ArrayList<SchedulerStepParam> childStepsParams){
        boolean isAlive;
        childSteps = new ArrayList<SchedulerStep>();
        for (Iterator<SchedulerStepParam> i = childStepsParams.iterator();i.hasNext();){
            SchedulerStep step = new SchedulerStep(i.next());
            childSteps.add(step);
            step.start();
        }
        /*for(Iterator<SchedulerStep> i = childSteps.iterator();i.hasNext();){

        }*/
    }
}
