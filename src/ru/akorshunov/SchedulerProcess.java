package ru.akorshunov;

import ru.akorshunov.params.SchedulerProcessParam;
import ru.akorshunov.params.SchedulerStepParam;

public class SchedulerProcess extends Thread{
    SchedulerProcessParam param;
    SchedulerProcess(SchedulerProcessParam param){
        this.param = param;
    }
    @Override
    public void run() {
        String nextStepId = "-1";//Первый шаг с индексом -1
        while(nextStepId != null){
            SchedulerStepParam stepParam = param.getStepParams().get(nextStepId);
            SchedulerStep step = new SchedulerStep(stepParam);
            nextStepId = stepParam.getNextId();
            step.start();
            try {
                step.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
