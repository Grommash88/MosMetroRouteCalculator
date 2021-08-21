package ru.lakeev.util;

import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import ru.lakeev.enumirations.ConsoleMsgTemplates;
import ru.lakeev.model.Line;
import ru.lakeev.model.Station;

public class MetroLogger {

  private static final Marker EXCEPTION = MarkerManager.getMarker("EXCEPTION");
  private static final Marker CONSOLE = MarkerManager.getMarker("CONSOLE");
  private static final Logger LOGGER = LogManager.getRootLogger();

  public static void logException(Exception e) {

    LOGGER.error(EXCEPTION, Arrays.toString(e.getStackTrace()).concat(System.lineSeparator())
        .concat(e.getMessage()));
  }

  public static void logReport(String message) {

    LOGGER.info(CONSOLE, message);
  }

  public static void logMessage(String message) {

    LOGGER.info(CONSOLE, message);
  }

  public static void printRoute(List<Station> route) {
    Station previousStation = null;
    for (Station station : route) {
      if (previousStation != null) {
        Line prevLine = previousStation.getLine();
        Line nextLine = station.getLine();
        if (!prevLine.equals(nextLine)) {
          MetroLogger.logMessage(String
              .format(ConsoleMsgTemplates.CONNECTION_ON_THE_ROUTE_MSG_TEMPLATE.getTemplate(),
                  station.getName(), nextLine.getName()));
        }
      }
      MetroLogger.logMessage("\t".concat(station.getName()));
      previousStation = station;
    }
  }
}
