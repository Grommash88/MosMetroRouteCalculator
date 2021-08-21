package ru.lakeev.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Metro implements Serializable {

  private List<Line> lines;
  private ArrayList<Connection> connections;
}
