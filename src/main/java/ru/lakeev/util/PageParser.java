package ru.lakeev.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.lakeev.enumirations.AppPaths;
import ru.lakeev.model.Connection;
import ru.lakeev.model.Line;
import ru.lakeev.model.Metro;
import ru.lakeev.model.Station;

public class PageParser {

  public static Metro parsePageToMetro() throws IOException {

    Document doc = Jsoup.connect(AppPaths.WEB_URL.getPath()).maxBodySize(0).get();
    Elements lines = doc.getElementsByClass("js-metro-line");
    Elements stations = doc.getElementsByClass("js-metro-stations");
    Map<String, Line> linesMap = getLinesMap(lines, stations);

    return new Metro(new ArrayList<>(linesMap.values()), getConnections(stations, linesMap));
  }

  private static Map<String, Line> getLinesMap(Elements lines, Elements stations) {

    Map<String, Line> linesMap = new HashMap<>();
    lines.stream().forEach(element -> {
      Line line = new Line(element.attr("data-line"), element.text(), new ArrayList<>());

      stations.select(String.format("[data-line=%s]", line.getNumber())).select("[class=name]")
          .forEach(st -> {
            Station station = new Station(line, st.text());
            line.getStationList().add(station);
          });
      linesMap.put(line.getNumber(), line);
    });
    return linesMap;
  }

  private static ArrayList<Connection> getConnections(Elements stations,
      Map<String, Line> linesMap) {

    ArrayList<Connection> connections = new ArrayList<>();
    linesMap.keySet().stream().forEach(lineKey -> {

      Line line = linesMap.get(lineKey);
      String lineNum = line.getNumber();
      stations.select(String.format("[data-line=%s]", lineNum))
          .select("a:has(span[title])").stream().forEach(elem -> {

            createConnection(elem, line, elem.select("span[class=name]").text(), linesMap, connections);
          });
    });
    return connections;
  }

  private static void createConnection(Element elem, Line line, String stationName,
      Map<String, Line> linesMap, ArrayList<Connection> connections) {

    if (!stationName.isBlank()) {

      Station from = line.getStationByName(stationName);
      elem.select("span[title]").stream().forEach(conStEm -> {

        String conStName = conStEm.attr("title")
            .substring(conStEm.attr("title").indexOf('«') + 1,
                conStEm.attr("title").indexOf('»'));
        String lineToNum = conStEm.attr("class").split("-")[
            conStEm.attr("class").split("-").length - 1];

        if (!conStName.isBlank()) {

          Station to = linesMap.get(lineToNum).getStationByName(conStName);
          Connection connection = new Connection(from, to);
          if (connections.stream().noneMatch(con -> con.compareTo(connection) == 0)) {
            connections.add(connection);
          }
        }
      });
    }
  }
}
