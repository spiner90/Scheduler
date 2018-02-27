package ru.akorshunov.converters;

import com.google.gson.*;
import ru.akorshunov.SchedulerProcessParam;
import ru.akorshunov.SchedulerStep;
import ru.akorshunov.SchedulerStepParam;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CustomJsonConverter implements JsonSerializer<SchedulerProcessParam>, JsonDeserializer<SchedulerProcessParam>{
    public JsonElement serialize(SchedulerProcessParam src, Type type, JsonSerializationContext context){
        JsonObject object = new JsonObject();
        object.addProperty("id",src.getId());
        object.addProperty("name",src.getName());

        JsonArray stepArr = new JsonArray();//Массив шагов
        HashMap<String, SchedulerStepParam> selects = src.getStepParams();

        //добавляем шаги в массив
        for(Map.Entry<String,SchedulerStepParam> entry : selects.entrySet()){
            String key = entry.getKey();
            SchedulerStepParam stepParam = entry.getValue();
            JsonObject stepObject = new JsonObject();
            stepObject.addProperty("id",stepParam.getId());
            stepObject.addProperty("nextId",stepParam.getNextId());
            stepObject.addProperty("type",stepParam.getType());
            stepObject.addProperty("name",stepParam.getName());
            JsonArray stepCommands = new JsonArray();
            //обавляем команды шага в массив
            for (Iterator<String> i = stepParam.getCommands().iterator();i.hasNext();){
                stepCommands.add(i.next());
            }
            stepObject.add("commands",stepCommands);
            JsonArray childSteps = new JsonArray();
            //Дочерние шаги
            for (Iterator<SchedulerStepParam> i = stepParam.getChildSteps().iterator();i.hasNext();){
                JsonObject childStepObject = new JsonObject();
                SchedulerStepParam childStep = i.next();
                childStepObject.addProperty("id",childStep.getId());
                childStepObject.addProperty("nextId",childStep.getNextId());
                childStepObject.addProperty("type",childStep.getType());
                childStepObject.addProperty("name",childStep.getName());
                JsonArray childStepCommands = new JsonArray();
                //Команды дочернего шага
                for (Iterator<String> j = childStep.getCommands().iterator();j.hasNext();){
                    childStepCommands.add(j.next());
                }
                childStepObject.add("commands",childStepCommands);
                childSteps.add(childStepObject);
            }
            stepObject.add("childSteps",childSteps);
            stepArr.add(stepObject);
        }
        object.add("stepParams",stepArr);
        return object;
    }
    public SchedulerProcessParam deserialize(JsonElement json, Type type, JsonDeserializationContext context){
        JsonObject object = json.getAsJsonObject();
        String id = object.get("id").getAsString();
        String name = object.get("name").getAsString();
        JsonArray stepArr = object.getAsJsonArray("stepParams");
        SchedulerProcessParam processParam = new SchedulerProcessParam(id,name);
        for(Iterator<JsonElement> i = stepArr.iterator(); i.hasNext();){

            JsonElement stepElement = i.next();
            JsonObject jsonStep = stepElement.getAsJsonObject();
            String stepName = jsonStep.get("name").getAsString();
            String stepId = jsonStep.get("id").getAsString();
            JsonElement stepNextIdElement = jsonStep.get("nextId");
            String stepNextId = null;
            if (stepNextIdElement != null)
                stepNextId = stepNextIdElement.getAsString();
            String stepType = jsonStep.get("type").getAsString();
            SchedulerStepParam stepParam = new SchedulerStepParam(stepId,stepNextId,stepType,stepName);
            JsonArray stepCommandsArr = jsonStep.get("commands").getAsJsonArray();
            for(Iterator<JsonElement> j = stepCommandsArr.iterator(); j.hasNext();) {
                stepParam.addCommand(j.next().getAsString());
            }
            JsonArray childStepsArr = jsonStep.get("childSteps").getAsJsonArray();
            for(Iterator<JsonElement> j = childStepsArr.iterator(); j.hasNext();) {
                JsonElement childStepElement = j.next();
                JsonObject childJsonStep = childStepElement.getAsJsonObject();
                String childStepName = childJsonStep.get("name").getAsString();
                String childStepId = childJsonStep.get("id").getAsString();
                String childStepNextId = childJsonStep.get("nextId").getAsString();
                String childStepType = childJsonStep.get("type").getAsString();
                SchedulerStepParam childStepParam = new SchedulerStepParam(childStepId,childStepNextId,childStepType,childStepName);
                JsonArray childStepCommandsArr = childJsonStep.get("commands").getAsJsonArray();
                for(Iterator<JsonElement> k = childStepCommandsArr.iterator(); k.hasNext();) {
                    childStepParam.addCommand(k.next().getAsString());
                }
                stepParam.addChildStep(childStepParam);
            }
            processParam.addStepParam(stepParam);
        }
        return new SchedulerProcessParam(id,name);
    }
}
