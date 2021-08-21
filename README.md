# MosMetroRouteCalculator
The program for making a route and calculating the travel time (approximate) along the Moscow metro:

    -The program supports commands:
        1 ROUTE - making a route from point A to point B and calculating the approximate travel time,
        with the output of the results to the console.
        2 REPORT - output to the console of metro statistics (Lines, Stations, Transfers, their number).
        3 EXIT - shutdown.

    -After starting, the presence of a JSON file describing the metropolitan model is checked if it is not,
    the page of the site is parsed (https://www.moscowmap.ru/metro.html#lines)
    and writing the data to a JSON file (src / main / resources / mosMetro.json).
    Parsing is implemented in the PageParser class, serialization to JSON and saving to a file, in the MetroSerializer class,
    creating a subway model in the MetroDeserializer class, a subway model in the StationIndex class.
    Based on the resource file, a metro model is created.
    The user is prompted to enter a command.

    -If the ROUTE command is entered, to compose and calculate the route, you must enter the departure and destination stations in the console,
    if one of the stations does not exist in the metro model, a StationNameException is thrown.
    Route compilation and travel time calculation is implemented in the RouteCalculator class, the class is covered with tests.

    -If the user entered REPORT, metro statistics are displayed in the console.
    Displaying statistics and other messages to the user, as well as logging errors to the logs / exception.log file are implemented in the MetroLogger class.
    User message templates and exception message templates, in the ConsoleMsgTemplates and ExceptionMsgTemplates classes, respectively.

Программа составления маршрута и расчета времени в пути(приблизительного) по метро Москвы:

    -Программа поддерживает команды:
        1 ROUTE - составление маршрута из точки А в точку Б и расчет примерного времени в пути, 
        с выводом результатов в консоль.
        2 REPORT - вывод в консоль статистики по метрополитену(Линии, Станции, Пересадки, их количество). 
        3 EXIT - завершение работы.

    -После запуска проверяется наличие JSON-файла описывающего модель метрополина если его нет, 
    происходит парсинг страницы сайта(https://www.moscowmap.ru/metro.html#lines) 
    и запись данных в JSON-файл (src/main/resources/mosMetro.json).
    Парсинг реализован в классе PageParser, серриалиазация в JSON и сохранение в файл, в классе MetroSerializer,
    создание модели метрополитена в классе MetroDeserializer, модель метрополитена в классе StationIndex.
    На основе ресурсного файла происходит создание модели метрополитена.
    Пользотелю предлогается ввести комманду.

    -Если введена комманда ROUTE, для составления и расчета маршрута необходимо ввести в консоль станции отправления и назначения, 
    если одной из станций не существует в моделе метрополитена, выбрасывается StationNameException. 
    Составление маршрута и расчет времени пути реализованно в классе RouteCalculator, класс покрыт тестами.

    -Если пользователь ввел REPORT, в консоль выводится статистика метрополитена. 
    Вывод статистики и других сообщений пользователю, а так же, логгирование ошибок в файл logs/exception.log реализованы в классе MetroLogger.
    Шаблоны сообщений пользователю и шаблоны сообщений исключений, в классах ConsoleMsgTemplates и ExceptionMsgTemplates, соответсвенно.
