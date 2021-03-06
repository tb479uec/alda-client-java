package alda.repl.commands;

import alda.AldaServer;
import jline.console.ConsoleReader;

/**
 * Handles the :quit alda command
 * Simply exits the repl
 */
public  class ReplQuit implements ReplCommand {
  @Override
  public void act(String args, StringBuffer history, AldaServer server, ConsoleReader reader) {
    // Bye! =)
    System.exit(0);
  }
  @Override
  public String docSummary() {
    return "Exits the Alda REPL session.";
  }
  @Override
  public String key() {
    return "quit";
  }
}
