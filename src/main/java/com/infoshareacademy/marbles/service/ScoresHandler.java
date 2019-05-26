package com.infoshareacademy.marbles.service;

import com.infoshareacademy.marbles.domain.Score;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ScoresHandler {

  private static final String SCORES_HISTORY_FILE_PATH = "scores";

  public void saveScore(int score, String name, Long time) throws IOException {
    Path path = Paths.get(SCORES_HISTORY_FILE_PATH);
    byte[] strToBytes = (name + "#" + time + "#" + score + "\n")
        .getBytes();

    Files.write(path, strToBytes, StandardOpenOption.APPEND);
  }

  public List<Score> loadScores() throws IOException {
    List<Score> scores = new ArrayList<>();
    Path path = Paths.get(SCORES_HISTORY_FILE_PATH);
    List<String> scoresLines = Files.readAllLines(path);

    scoresLines.forEach(s -> {

      Object[] scoreParts = s.split("#");
      Score score = new Score(String.valueOf(scoreParts[0]),
          Long.valueOf(String.valueOf(scoreParts[1])),
          Integer.valueOf(String.valueOf(scoreParts[2])));
      scores.add(score);
    });

    scores.sort(Comparator.comparing(Score::getScore).reversed());

    return scores.subList(0, scores.size() > 10 ? 10 : scores.size());
  }

  public void clearStats() throws IOException {
    Path path = Paths.get(SCORES_HISTORY_FILE_PATH);
    Files.write(path, "".getBytes());
  }
}
