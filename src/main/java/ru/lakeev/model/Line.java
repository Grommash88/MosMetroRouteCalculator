package ru.lakeev.model;

import java.util.List;
import lombok.Getter;
import ru.lakeev.enumirations.ExceptionMsgTemplates;

@Getter
public class Line implements Comparable<Line> {

  private String number;
  private String name;
  private List<Station> stationList;

  public Line(String number, String name, List<Station> stationList) {
    this.number = number;
    this.name = name;
    this.stationList = stationList;
  }

  public Station getStationByName(String name) {

    return stationList.stream()
        .filter(s -> s.getName().equals(name))
        .findFirst()
        .orElseThrow(() -> {
          System.out.println(name);
          throw new IllegalArgumentException(String
              .format(ExceptionMsgTemplates.STATION_NOT_FOUND_TEMP.getTemplate(), name, getName()));
        });
  }

  public void addStation(Station station) {
    stationList.add(station);
  }

  @Override
  public int compareTo(Line line) {
    return CharSequence.compare(number, line.getNumber());
  }

  @Override
  public boolean equals(Object obj) {
    return compareTo((Line) obj) == 0;
  }

  @Override
  public String toString() {
    return "stations:" + stationList;
  }
}