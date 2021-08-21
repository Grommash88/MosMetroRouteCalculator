package ru.lakeev.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import ru.lakeev.enumirations.AppPaths;
import ru.lakeev.enumirations.ExceptionMsgTemplates;
import ru.lakeev.model.Line;
import ru.lakeev.model.Metro;
import ru.lakeev.model.Station;
import ru.lakeev.model.StationIndex;

public class MetroDeserializer {


  public static StationIndex createStationIndex() {
    StationIndex stationIndex = new StationIndex();
    String data = getJsonFile();
    try {
      JSONParser parser = new JSONParser();
      JSONObject jsonData = (JSONObject) parser.parse(data);

      JSONArray linesArray = (JSONArray) jsonData.get("lines");
      parseLines(linesArray, stationIndex);

      JSONObject stationsObject = (JSONObject) jsonData.get("stations");
      parseStations(stationsObject, stationIndex);

      JSONArray connectionsArray = (JSONArray) jsonData.get("connections");
      parseConnections(connectionsArray, stationIndex);
    } catch (Exception e) {
      MetroLogger.logException(e);
      e.printStackTrace();
    }
    return stationIndex;
  }

  private static void parseConnections(JSONArray connectionsArray, StationIndex stationIndex) {
    connectionsArray.forEach(connectionObject ->
    {
      JSONArray connection = (JSONArray) connectionObject;
      List<Station> connectionStations = new ArrayList<>();
      connection.forEach(item ->
      {
        JSONObject itemObject = (JSONObject) item;
        String lineNumber = (String) itemObject.get("line");
        String stationName = (String) itemObject.get("name");

        Station station = stationIndex.getStation(stationName, lineNumber);
        if (station == null) {
          throw new IllegalArgumentException(String
              .format(ExceptionMsgTemplates.STATION_NOT_FOUND_TEMP.getTemplate(), stationName,
                  lineNumber));
        }
        connectionStations.add(station);
      });
      stationIndex.addConnection(connectionStations);
    });
  }

  private static void parseStations(JSONObject stationsObject, StationIndex stationIndex) {
    stationsObject.keySet().forEach(lineNumberObject ->
    {
      String lineNumber = (String) lineNumberObject;
      Line line = stationIndex.getLine(lineNumber);
      JSONArray stationsArray = (JSONArray) stationsObject.get(lineNumberObject);
      stationsArray.forEach(stationObject ->
      {
        Station station = new Station(line, (String) stationObject);
        stationIndex.addStation(station);
        line.addStation(station);
      });
    });
  }

  private static void parseLines(JSONArray linesArray, StationIndex stationIndex) {
    linesArray.forEach(lineObject -> {
      JSONObject lineJsonObject = (JSONObject) lineObject;
      Line line = new Line(
          (String) lineJsonObject.get("number"),
          (String) lineJsonObject.get("name"),
          new ArrayList<>()
      );
      stationIndex.addLine(line);
    });
  }

  private static String getJsonFile() {

    StringBuilder builder = new StringBuilder();
    try {
      if (!Paths.get(AppPaths.JSON_FILE_PATH.getPath()).toFile().exists()) {
        Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Metro.class, new MetroSerializer())
            .create();
        MetroSerializer.writeToFile(PageParser.parsePageToMetro(), gson);
      }
      List<String> lines = Files.readAllLines(Paths.get(AppPaths.JSON_FILE_PATH.getPath()));
      lines.forEach(builder::append);
    } catch (Exception e) {
      MetroLogger.logException(e);
      e.printStackTrace();
    }
    return builder.toString();
  }
}