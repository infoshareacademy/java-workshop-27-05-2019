package com.infoshareacademy.marbles.game;

import com.infoshareacademy.marbles.domain.MarbleGridProperty;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

public class PathSolver {

  private int[][] state;
  private List<Pair<Integer, Integer>> path;
  private int columnsCount;
  private int rowsCount;
  private int endColumn;
  private int endRow;

  // We use here DFS algorithm to find a possible path between two fields.

  public List<Pair<Integer, Integer>> execute(int startCol, int startRow, int endCol, int endRow,
      MarbleGridProperty marbleGrid) {

    // Set up the grid dimensions.
    columnsCount = MarbleState.GRID_FIELD_WIDTH;
    rowsCount = MarbleState.GRID_FIELD_HEIGHT;

    // Prepare a copy of the grid state for algorithm's purposes.
    state = new int[columnsCount][rowsCount];
    for (int i = 0; i < columnsCount; i++) {
      for (int j = 0; j < rowsCount; j++) {
        state[i][j] = marbleGrid.getValue(i, j);
      }
    }

    // Set up the target field
    endColumn = endCol;
    this.endRow = endRow;

    // Empty path
    path = new ArrayList<>();

    // Run the algorithm. As a result, path list should be filled in the steps of a path.
    // If eventually the path is empty, it means that there is no path at all between the
    // two points.
    findPath(startCol, startRow);

    // Return path, empty list if not found.
    return path;
  }

  private void findPath(int startCol, int startRow) {
    step(startCol, startRow);
  }

  private boolean step(int column, int row) {
    // Set field as visited
    path.add(new Pair<>(column, row));
    state[column][row] = -1;

    // Target point found - return true!
    if (column == endColumn && row == endRow) {
      return true;
    }

    // Found is set to true if the target point has been reached.
    boolean found = false;

    // Up
    int nextCol = column;
    int nextRow = row - 1;
    if (nextRow >= 0 && state[nextCol][nextRow] == 0) {
      found = step(nextCol, nextRow);
    }

    // Right
    nextCol = column + 1;
    nextRow = row;
    if (!found && nextCol < columnsCount && state[nextCol][nextRow] == 0) {
      found = step(nextCol, nextRow);
    }

    // Down
    nextCol = column;
    nextRow = row + 1;
    if (!found && nextRow < rowsCount && state[nextCol][nextRow] == 0) {
      found = step(nextCol, nextRow);
    }

    // Left
    nextCol = column - 1;
    nextRow = row;
    if (!found && nextCol >= 0 && state[nextCol][nextRow] == 0) {
      found = step(nextCol, nextRow);
    }

    // There is no move from this point that can reach the target.
    // Remove the point from path.
    if (!found) {
      path.remove(path.size() - 1);
    }

    return found;
  }
}
