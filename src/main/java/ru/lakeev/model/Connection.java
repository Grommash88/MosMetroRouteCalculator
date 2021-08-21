package ru.lakeev.model;

import java.io.Serializable;
import java.util.ArrayList;
import lombok.Getter;

@Getter
public class Connection implements Comparable<Connection>, Serializable {

  private ArrayList<Station> stations;

  public Connection(Station station1, Station station2) {
    stations = new ArrayList<>();
    stations.add(station1);
    stations.add(station2);
  }

  @Override
  public int compareTo(Connection connection) {

    if ((stations.get(0).getName().equals(connection.getStations().get(1).getName())
        && stations.get(0).getLine().equals(connection.getStations().get(1).getLine())
        && stations.get(1).equals(connection.getStations().get(0))
        && stations.get(1).getLine().equals(connection.getStations().get(0).getLine()))
        || (stations.get(0).getName().equals(connection.getStations().get(0).getName())
        && stations.get(0).getLine().equals(connection.getStations().get(0).getLine())
        && stations.get(1).getName().equals(connection.getStations().get(1).getName())
        && stations.get(1).getLine().equals(connection.getStations().get(1).getLine()))) {
      return 0;
    } else {
      return 1;
    }

  }
}
