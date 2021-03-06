package alda.repl.commands;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.FileAlreadyExistsException;

import java.io.IOException;
import java.io.UncheckedIOException;

import java.util.stream.Stream;

import alda.AldaServer;
import alda.Util;
import jline.console.ConsoleReader;

/**
 * Handles the :load command.
 * Takes a filename as a paramter.
 */
public class ReplLoad implements ReplCommand {
  @Override
  public void act(String args, StringBuffer history, AldaServer server, ConsoleReader reader) {
    if (args == "") {
      usage();
      return;
    }
    Stream<String> fLines = null;
    boolean error = false;

    try {
      fLines = Files.lines(Paths.get(args));
      StringBuffer newHistory = new StringBuffer();
      fLines.forEach(x -> {
          newHistory.append(x);
          newHistory.append("\n");
        });

      // Check if the score we just loaded is valid (prevent further errors)
      try {
        String mode = Util.scoreMode(false, true);
        String res = server.parseRaw(newHistory.toString(), mode, false);
      } catch (Throwable e) {
        server.error(e.getMessage());
        // Don't change 'history'
        error = true;
      }

      if (!error) {
        history.delete(0, history.length());
        history.append(newHistory);
      }
    } catch (IOException|UncheckedIOException e) {
      System.err.println("There was an error reading '" + args + "'");
    } finally {
      if (fLines != null)
        fLines.close();
    }
  }
  @Override
  public String docSummary() {
    return "Loads an Alda score into the current REPL session.";
  }
  @Override
  public String docDetails() {
    return "Usage:\n\n" +
      "  :load test/examples/bach_cello_suite_no_1.alda\n" +
      "  :load /Users/rick/Scores/love_is_alright_tonite.alda";
  }
  @Override
  public String key() {
    return "load";
  }
}
