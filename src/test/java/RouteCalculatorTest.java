
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.lakeev.calculator.RouteCalculator;
import ru.lakeev.model.Line;
import ru.lakeev.model.Station;
import ru.lakeev.model.StationIndex;

@DisplayName("Тестирование класса ru.lakeev.calculator.RouteCalculator.java")
public class RouteCalculatorTest {

  private Random random;
  private StationIndex testSubway;
  private RouteCalculator testRouteCalculator;

  private Line testFirstLine;
  private Line testSecondLine;
  private Line testThirdLine;
  private Line testFourthLine;

  @BeforeEach
  protected void setUp() {

    String[] line1 = {"Первая станция", "Вторая станция", "Третья станция", "Четвертая станция",
        "Пятая станция", "Шестая станция", "Седьмая станция"};
    String[] line2 = {"Восьмая станция", "Девятая станция", "Десятая станция",
        "Одиннацатая станция", "Двенадцатая станция", "Тринадцатая станция",
        "Четырнадцатая станция"};
    String[] line3 = {"Пятнадцатая станция", "Шестнадцатая станция", "Семнадцатая станция",
        "Восемнадцатая станция", "Девятнадцатая станция", "Двадцатая станция"};
    String[] line4 = {"Двадцать первая станция", "Двадцать вторая станция",
        "Двадцать третья станция", "Двадцать четвертая станция", "Двадцать пятая станция"};

    testFirstLine = new Line("1", "Первая", new ArrayList<Station>());
    testSecondLine = new Line("2", "Вторая", new ArrayList<Station>());
    testThirdLine = new Line("3", "Третья", new ArrayList<Station>());
    testFourthLine = new Line("4", "Четвертая", new ArrayList<Station>());

    for (String stationName : line1) {
      testFirstLine.addStation(new Station(testFirstLine, stationName));
    }
    for (String stationName : line2) {
      testSecondLine.addStation(new Station(testSecondLine, stationName));
    }
    for (String stationName : line3) {
      testThirdLine.addStation(new Station(testThirdLine, stationName));
    }
    for (String stationName : line4) {
      testFourthLine.addStation(new Station(testFourthLine, stationName));
    }

    testSubway = new StationIndex();
    testSubway.addLine(testFirstLine);
    testSubway.addLine(testSecondLine);
    testSubway.addLine(testThirdLine);
    testSubway.addLine(testFourthLine);

    for (int lineNum = 1; lineNum <= 4; lineNum++) {
      testSubway.getLine(String.valueOf(lineNum)).getStationList()
          .forEach(s -> testSubway.addStation(s));
    }

    testSubway.addConnection(
        Arrays.asList(testFirstLine.getStationList().get(2),
            testThirdLine.getStationList().get(2)));
    testSubway.addConnection(
        Arrays.asList(testThirdLine.getStationList().get(4),
            testSecondLine.getStationList().get(2)));
    testSubway.addConnection(
        Arrays.asList(testFirstLine.getStationList().get(4),
            testFourthLine.getStationList().get(1)));
    testSubway.addConnection(
        Arrays.asList(testFourthLine.getStationList().get(3),
            testSecondLine.getStationList().get(4)));
    testRouteCalculator = new RouteCalculator(testSubway);
  }

  @Test
  @DisplayName("Тест метода getShortestRoute(), маршрут без пересадок.")
  public void testGetRouteOnTheLine() {

    List<Station> expected = Arrays.asList(
        testSubway.getLine("1").getStationList().get(5),
        testSubway.getLine("1").getStationList().get(4),
        testSubway.getLine("1").getStationList().get(3),
        testSubway.getLine("1").getStationList().get(2));

    List<Station> actual = testRouteCalculator
        .getShortestRoute(testFirstLine.getStationList().get(5),
            testFirstLine.getStationList().get(2));
    assertEquals(expected, actual, "Построенный маршрут отличается от оптимального.");
  }

  @Test
  @DisplayName("Тест метода getShortestRoute(), маршрут c пересадкой.")
  public void testGetRouteWithOneConnection() {
    List<Station> actual = testRouteCalculator
        .getShortestRoute(testSecondLine.getStationList().get(4),
            testThirdLine.getStationList().get(0));
    List<Station> expected = Arrays.asList(
        testSubway.getLine("2").getStationList().get(4),
        testSubway.getLine("2").getStationList().get(3),
        testSubway.getLine("2").getStationList().get(2),
        testSubway.getLine("3").getStationList().get(4),
        testSubway.getLine("3").getStationList().get(3),
        testSubway.getLine("3").getStationList().get(2),
        testSubway.getLine("3").getStationList().get(1),
        testSubway.getLine("3").getStationList().get(0));
    assertEquals(expected, actual, "Построенный маршрут отличается от оптимального.");
  }

  @Test
  @DisplayName("Тест метода getShortestRoute(), маршрут c 2мя пересадками.")
  public void testGetRouteWithTwoConnections() {

    List<Station> expected = Arrays.asList(
        testSubway.getLine("2").getStationList().get(6),
        testSubway.getLine("2").getStationList().get(5),
        testSubway.getLine("2").getStationList().get(4),
        testSubway.getLine("4").getStationList().get(3),
        testSubway.getLine("4").getStationList().get(2),
        testSubway.getLine("4").getStationList().get(1),
        testSubway.getLine("1").getStationList().get(4),
        testSubway.getLine("1").getStationList().get(3));
    List<Station> actual = testRouteCalculator
        .getShortestRoute(testSecondLine.getStationList().get(6),
            testFirstLine.getStationList().get(3));
    assertEquals(expected, actual, "Построенный маршрут отличается от оптимального.");
  }

  @Test
  @DisplayName("Тест метода calculateDuration(), маршрут без пересадок.")
  public void testTimeOfRouteOnTheLine() throws NullPointerException {

    List<Station> testRoute = testRouteCalculator.getShortestRoute(
        testSubway.getStation("Первая станция"),
        testSubway.getStation("Седьмая станция"));

    int connectionOnTheRoute = 0;
    Station previousStation = null;
    for (int i = 0; i < testRoute.size(); i++) {
      Station station = testRoute.get(i);
      if (i > 0) {
        if (!previousStation.getLine().equals(station.getLine())) {
          connectionOnTheRoute++;
        }
      }
      previousStation = station;
    }

    double actual = RouteCalculator.calculateDuration(testRoute);
    double expected = (2.5 * ((testRoute.size() - 1) - connectionOnTheRoute))
        + (3.5 * connectionOnTheRoute);

    assertEquals(expected, actual, "Время расчитано не корректно.");
  }

  @Test
  @DisplayName("Тест метода calculateDuration(), маршрут с пересадкой.")
  public void testCalculateDurationOneConnection() {

    List<Station> testRoute = Arrays.asList(
        testSubway.getLine("1").getStationList().get(5),
        testSubway.getLine("1").getStationList().get(4),
        testSubway.getLine("4").getStationList().get(1),
        testSubway.getLine("4").getStationList().get(2),
        testSubway.getLine("4").getStationList().get(3));

    int connectionOnTheRoute = 0;
    Station previousStation = null;
    for (int i = 0; i < testRoute.size(); i++) {
      Station station = testRoute.get(i);
      if (i > 0) {
        if (!previousStation.getLine().equals(station.getLine())) {
          connectionOnTheRoute++;
        }
      }
      previousStation = station;
    }

    double actual = RouteCalculator.calculateDuration(testRoute);
    double expected = (2.5 * ((testRoute.size() - 1) - connectionOnTheRoute))
        + (3.5 * connectionOnTheRoute);
    assertEquals(expected, actual, "Время расчитано не корректно.");
  }

  @Test
  @DisplayName("Тест метода calculateDuration(), маршрут с 2мя пересадками.")
  public void testCalculateDurationTwoConnection() {

    List<Station> testRoute = Arrays.asList(
        testSubway.getLine("2").getStationList().get(6),
        testSubway.getLine("2").getStationList().get(5),
        testSubway.getLine("2").getStationList().get(4),
        testSubway.getLine("4").getStationList().get(3),
        testSubway.getLine("4").getStationList().get(2),
        testSubway.getLine("4").getStationList().get(1),
        testSubway.getLine("1").getStationList().get(4),
        testSubway.getLine("1").getStationList().get(3));

    int connectionOnTheRoute = 0;
    Station previousStation = null;
    for (int i = 0; i < testRoute.size(); i++) {
      Station station = testRoute.get(i);
      if (i > 0) {
        if (!previousStation.getLine().equals(station.getLine())) {
          connectionOnTheRoute++;
        }
      }
      previousStation = station;
    }

    double actual = RouteCalculator.calculateDuration(testRoute);
    double expected = (2.5 * ((testRoute.size() - 1) - connectionOnTheRoute))
        + (3.5 * connectionOnTheRoute);
    assertEquals(expected, actual, "Время расчитано не корректно.");
  }

  @Test
  @DisplayName("Тест метода calculateDuration(), случайный маршрут.")
  public void testCalculateDurationRandomRoute() {
    random = new Random();
    Line fromLine = testSubway.getLine(String.valueOf(random.nextInt(4) + 1));
    Station from = fromLine.getStationList().get(random.nextInt(fromLine.getStationList().size()));
    Line toLine = testSubway.getLine(String.valueOf(random.nextInt(4) + 1));
    Station to = toLine.getStationList().get(random.nextInt(toLine.getStationList().size()));

    List<Station> testRoute = testRouteCalculator.getShortestRoute(from, to);
    int connectionOnTheRoute = 0;
    Station previousStation = null;
    for (int i = 0; i < testRoute.size(); i++) {
      Station station = testRoute.get(i);
      if (i > 0) {
        if (!previousStation.getLine().equals(station.getLine())) {
          connectionOnTheRoute++;
        }
      }
      previousStation = station;
    }

    double actual = RouteCalculator.calculateDuration(testRoute);
    double expected = (2.5 * ((testRoute.size() - 1) - connectionOnTheRoute))
        + (3.5 * connectionOnTheRoute);
    assertEquals(expected, actual, "Время расчитано не корректно.");
  }

  @AfterEach
  void tearDown() {
    random = null;
    testSubway = null;
    testRouteCalculator = null;

    testFirstLine = null;
    testSecondLine = null;
    testThirdLine = null;
    testFourthLine = null;
  }
}
/* СХЕМА МЕТРОПОЛИТЕНА
                 I            II

                 0             0
                 |             |
                 1             1
                 |             |
III  0-----1----2/2-----3-----4/2----5
                 |             |
                 3             3
                 |             |
IV   0----------1/4-----2-----3/4------4
                 |             |
                 5             5
                 |             |
                 6             6
                               |
                               7

 */
