package ru.lakeev.enumirations;

public enum ConsoleMsgTemplates {

  UNRECOGNIZED_COMMAND_MSG_TEMPLATE("Unrecognized command."),
  CONNECTION_ON_THE_ROUTE_MSG_TEMPLATE("\tПереход на станцию %s (%s)"),
  ROUTE_DURATION_MSG_TEMPLATE("Длительность: %.2f минут"),
  COMMANDS_LIST_MSG_TEMPLATE(
      "Enter \"EXIT\" to terminate the program.".concat(System.lineSeparator())
          .concat(" \t\t\t\t\t\t\t\t\t\t\t\t Enter \"REPORT\" to display metro statistics."
              .concat(System.lineSeparator())
              .concat(" \t\t\t\t\t\t\t\t\t\t\t\t Enter \"ROUTE\" to build a route.")
              .concat(System.lineSeparator())));

  private final String template;

  ConsoleMsgTemplates(String template) {

    this.template = template;
  }

  public String getTemplate() {

    return template;
  }
}
