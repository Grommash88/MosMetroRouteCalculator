package ru.lakeev.enumirations;

public enum ExceptionMsgTemplates {

  STATION_NAME_EXCEPTION_TEMP("%s incorrect meaning. Station name can't contain numbers."),
  STATION_NOT_FOUND_TEMP("Station % not exist on the line %s.");

  private final String template;

  ExceptionMsgTemplates(String template) {

    this.template = template;
  }

  public String getTemplate() {

    return template;
  }
}
