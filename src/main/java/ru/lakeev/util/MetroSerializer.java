package ru.lakeev.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import ru.lakeev.model.Connection;
import ru.lakeev.model.Line;
import ru.lakeev.model.Metro;
import ru.lakeev.model.Station;

public class MetroSerializer implements JsonSerializer<Metro> {

  private static final String JSON_FILE_PATH = "src/main/resources/mosMetro.json";

  @Override
  public JsonElement serialize(Metro metro, Type type,
      JsonSerializationContext jsonSerializationContext) {
    JsonObject finalResult = new JsonObject();
    JsonObject result = new JsonObject();
    JsonArray lines = new JsonArray();
    JsonArray connections = new JsonArray();

    for (Line line : metro.getLines()) {

      JsonArray stations = new JsonArray();
      result.add(line.getNumber(), stations);

      JsonObject gsonLine = new JsonObject();
      gsonLine.addProperty("number", line.getNumber());
      gsonLine.addProperty("name", line.getName());
      lines.add(gsonLine);

      for (Station s : line.getStationList()) {
        stations.add(s.getName());
      }
    }

    for (Connection connection : metro.getConnections()) {
      JsonArray gsonConnection = new JsonArray();

      for (Station station : connection.getStations()) {
        JsonObject gsonStation = new JsonObject();
        gsonStation.addProperty("line", station.getLine().getNumber());
        gsonStation.addProperty("name", station.getName());
        gsonConnection.add(gsonStation);
      }

      connections.add(gsonConnection);
    }

    finalResult.add("stations", result);
    finalResult.add("connections", connections);
    finalResult.add("lines", lines);
    return finalResult;
  }

  public static void writeToFile(Metro metro, Gson gson) {

    try {
      FileWriter fileWriter = new FileWriter(JSON_FILE_PATH);
      fileWriter.write(gson.toJson(metro));
      fileWriter.flush();
      fileWriter.close();
    } catch (IOException e) {
      MetroLogger.logException(e);
      e.printStackTrace();
    }
  }
}