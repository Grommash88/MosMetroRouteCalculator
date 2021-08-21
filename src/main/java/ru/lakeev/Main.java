package ru.lakeev;

import java.util.List;
import java.util.Scanner;
import ru.lakeev.calculator.RouteCalculator;
import ru.lakeev.enumirations.ConsoleMsgTemplates;
import ru.lakeev.model.Station;
import ru.lakeev.model.StationIndex;
import ru.lakeev.util.MetroDeserializer;
import ru.lakeev.util.MetroLogger;

public class Main {

  public static void main(String[] args) {

    Scanner scanner = new Scanner(System.in);
    StationIndex stationIndex = MetroDeserializer.createStationIndex();
    RouteCalculator calculator = new RouteCalculator(stationIndex);

    while (true) {

      MetroLogger.logMessage(ConsoleMsgTemplates.COMMANDS_LIST_MSG_TEMPLATE.getTemplate());
      String input = scanner.nextLine().trim();
      if (input.equals("EXIT")) {
        break;
      } else {
        if (input.equals("REPORT")) {

          MetroLogger.logReport(stationIndex.getReport());
        } else if (input.equals("ROUTE")) {
          try {
            Station from = stationIndex.takeStation(scanner, "Введите станцию отправления:");
            Station to = stationIndex.takeStation(scanner, "Введите станцию назначения:");
            List<Station> route = calculator.getShortestRoute(from, to);
            MetroLogger.logMessage("Маршрут:");
            MetroLogger.printRoute(route);
            MetroLogger.logMessage(String
                .format(ConsoleMsgTemplates.ROUTE_DURATION_MSG_TEMPLATE.getTemplate(),
                    RouteCalculator.calculateDuration(route)));

          } catch (Exception e) {
            MetroLogger.logException(e);
            MetroLogger.logMessage(e.getMessage());
          }
        } else {
          MetroLogger.logMessage(
              ConsoleMsgTemplates.UNRECOGNIZED_COMMAND_MSG_TEMPLATE.getTemplate());
        }
      }
    }
  }
}


