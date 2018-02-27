package ru.akorshunov.converters;

import com.google.gson.*;
import ru.akorshunov.SchedulerProcessParam;
import ru.akorshunov.SchedulerStepParam;
import java.lang.reflect.Type;
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
        return new SchedulerProcessParam(id,name);
    }
}
