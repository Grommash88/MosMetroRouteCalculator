package ru.lakeev.model;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import lombok.Getter;
import ru.lakeev.enumirations.ExceptionMsgTemplates;
import ru.lakeev.exceptions.StationNameException;
import ru.lakeev.util.MetroLogger;

@Getter
public class StationIndex {

  HashMap<String, Line> number2line;
  TreeSet<Station> stations;
  TreeMap<Station, TreeSet<Station>> connections;

  public StationIndex() {
    number2line = new HashMap<>();
    stations = new TreeSet<>();
    connections = new TreeMap<>();
  }

  public void addStation(Station station) {
    stations.add(station);
  }

  public void addLine(Line line) {
    number2line.put(line.getNumber(), line);
  }

  public void addConnection(List<Station> stations) {
    for (Station station : stations) {
      if (!connections.containsKey(station)) {
        connections.put(station, new TreeSet<>());
      }
      TreeSet<Station> connectedStations = connections.get(station);
      connectedStations.addAll(stations.stream()
          .filter(s -> !s.equals(station)).collect(Collectors.toList()));
    }
  }

  public Line getLine(String number) {
    return number2line.get(number);
  }

  public Station getStation(String name) {
    for (Station station : stations) {
      if (station.getName().equalsIgnoreCase(name)) {
        return station;
      }
    }
    return null;
  }

  public Station getStation(String name, String lineNumber) {
    Station query = new Station(getLine(lineNumber), name);
    Station station = stations.ceiling(query);

    return station.equals(query) ? station : null;
  }

  public Set<Station> getConnectedStations(Station station) {
    if (connections.containsKey(station)) {
      return connections.get(station);
    }
    return new TreeSet<>();
  }

  public String getReport() {
    StringBuilder report = new StringBuilder();
    report.append(System.lineSeparator());
    getNumber2line().keySet().stream()
        .map(key -> getNumber2line().get(key))
        .forEach(line -> {
          StringBuilder sb = new StringBuilder();
          sb.append("Line number: ").append(line.getNumber()).append(System.lineSeparator())
              .append("Line name: ").append(line.getName()).append(System.lineSeparator())
              .append("\tstations count: ").append(line.getStationList().size())
              .append(System.lineSeparator())
              .append("\tconnections count: ");
          int connectionsOnLineCount = (int) line.getStationList().stream()
              .filter(station -> !getConnectedStations(station).isEmpty()).count();
          sb.append(connectionsOnLineCount).append(System.lineSeparator())
              .append("\tlist of stations:")
              .append(System.lineSeparator());
          line.getStationList().forEach(station -> {

            Set<Station> transition = getConnectedStations(station);
            if (!transition.isEmpty()) {

              sb.append("\t\t\t").append(station.getName()).append(" transition to the station: ");
              transition.forEach(s -> {
                sb.append("(").append(s.getName()).append(" ").append(s.getLine().getName()
                    .concat(") "));
              });
              sb.append(System.lineSeparator());
            } else {

              sb.append("\t\t\t").append(station.getName()).append(System.lineSeparator());
            }
          });
          report.append(sb);
        });

    return report.toString();
  }

  public Station takeStation(Scanner scanner, String message)
      throws StationNameException {
    for (; ; ) {
      MetroLogger.logMessage(message);
      String line = scanner.nextLine().trim();
      if (line.matches(".*[1-9].*")) {
        throw new StationNameException(
            String.format(ExceptionMsgTemplates.STATION_NAME_EXCEPTION_TEMP.getTemplate(), line));
      }
      Station station = getStation(line);
      if (station != null) {
        return station;
      }
      MetroLogger.logMessage("Станция не найдена :(");
    }
  }
}