package ru.lakeev.enumirations;

public enum AppPaths {

  JSON_FILE_PATH("src/main/resources/mosMetro.json"),
  WEB_URL("https://www.moscowmap.ru/metro.html#lines");

  private final String path;

  AppPaths(String path) {
    this.path = path;
  }

  public String getPath() {
    return path;
  }

}
