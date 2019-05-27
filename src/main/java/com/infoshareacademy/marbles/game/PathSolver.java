package com.infoshareacademy.marbles.game;

import com.infoshareacademy.marbles.domain.MarbleGridProperty;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.util.Pair;

public class PathSolver {

  private LinkedList<Integer> nodesQueue;
  private List<List<Integer>> nodePaths;
  private List<Boolean> nodeVisited;

  // We use here Dijkstra's algorithm to find a shortest possible path between two fields.

  public List<Pair<Integer, Integer>> execute(int startCol, int startRow, int endCol, int endRow,
      MarbleGridProperty marbleGrid) {

    // Number of all nodes.
    int nodeCount = MarbleState.GRID_FIELD_HEIGHT * MarbleState.GRID_FIELD_WIDTH;

    // The number of the node we are starting from.
    int startNode = getNodeNumber(startCol, startRow);

    // The number of the node we'd like to go to.
    int endNode = getNodeNumber(endCol, endRow);

    // Initiate an empty path for each node and node visited indicators
    nodePaths = new ArrayList<>();
    nodeVisited = new ArrayList<>();
    for (int i = 0; i < nodeCount; i++) {
      nodePaths.add(new ArrayList<>());
      nodeVisited.add(false);
    }

    // FIFO queue for processed nodes.
    // We assume that there can be duplicates in the queue.
    // After node processing, we mark it as visited.
    nodesQueue = new LinkedList<>();

    // Put into the queue the first node (start position)
    nodesQueue.add(startNode);

    // The path for the starting node consist of that node.
    nodePaths.get(startNode).add(startNode);

    // Helper variables to be used withing the loop below.
    int row, column, node, nextNode, nodeValue;

    // As long as there is anything in the queue, process nodes.
    while (!nodesQueue.isEmpty()) {

      // Get the first node from the queue and remove it.
      node = nodesQueue.poll();

      // Process the node only if it has not been visited yet.
      // Take notice we may have duplicates in the queue.
      if (!nodeVisited.get(node)) {

        // Set current node as visited at the very beginning of processing.
        nodeVisited.set(node, true);

        // Current "coordinates" of the node (col/row from the grid representation).
        row = node / MarbleState.GRID_FIELD_WIDTH;
        column = node % MarbleState.GRID_FIELD_WIDTH;

        // Go up
        if (row > 0) {
          nodeValue = marbleGrid.getValue(column, row - 1);
          nextNode = getNodeNumber(column, row - 1);
          processNode(node, nextNode, nodeValue);
        }

        // Go right
        if (column + 1 < MarbleState.GRID_FIELD_WIDTH) {
          nodeValue = marbleGrid.getValue(column + 1, row);
          nextNode = getNodeNumber(column + 1, row);
          processNode(node, nextNode, nodeValue);
        }

        // Go down
        if (row + 1 < MarbleState.GRID_FIELD_HEIGHT) {
          nodeValue = marbleGrid.getValue(column, row + 1);
          nextNode = getNodeNumber(column, row + 1);
          processNode(node, nextNode, nodeValue);
        }

        // Go left
        if (column > 0) {
          nodeValue = marbleGrid.getValue(column - 1, row);
          nextNode = getNodeNumber(column - 1, row);
          processNode(node, nextNode, nodeValue);
        }
      }
    }

    // Return the path as col/row pairs.
    // nodePaths stores a list of nodes.
    return nodePaths.get(endNode).stream().map(number -> {
      int nodeColumn = number % MarbleState.GRID_FIELD_WIDTH;
      int nodeRow = number / MarbleState.GRID_FIELD_WIDTH;
      return new Pair<>(nodeColumn, nodeRow);
    }).collect(Collectors.toList());
  }

  private void processNode(int node, int nextNode, int nodeValue) {
    // Process the node only if:
    //   - It has not been visited yet.
    //   - There is a connection between node and nodeNext (nodeValue = 0, no marble on the field).
    if (!nodeVisited.get(nextNode) && nodeValue == 0) {
      // Get the currently known path for the node where we are on.
      List<Integer> currentNodePath = nodePaths.get(node);
      // Get the currently known path for the node where we would like to go to.
      List<Integer> nextNodePath = nodePaths.get(nextNode);

      // Every edge has value = 1. In short - the value is measured as the number of nodes.
      // The Dijkstra's algorithm assumes that the initial path to any node is equal to infinity.
      // In this case we assume that if the path is empty it means just infinity.
      if (currentNodePath.size() + 1 < nextNodePath.size() || nextNodePath.isEmpty()) {
        nextNodePath = new ArrayList<>(currentNodePath);
        nextNodePath.add(nextNode);
        nodePaths.set(nextNode, nextNodePath);
      }

      // Add to queue the node if it was not visited yet.
      if (!nodeVisited.get(nextNode)) {
        nodesQueue.add(nextNode);
      }
    }
  }

  private int getNodeNumber(int column, int row) {
    return row * MarbleState.GRID_FIELD_WIDTH + column;
  }
}
