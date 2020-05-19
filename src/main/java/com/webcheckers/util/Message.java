package com.webcheckers.util;

import com.webcheckers.model.Player;
import com.webcheckers.model.Position;

import java.util.logging.Logger;

/**
 * A UI-friendly representation of a message to the user.
 *
 * <p>
 * This is a <a href='https://en.wikipedia.org/wiki/Domain-driven_design'>DDD</a>
 * <a href='https://en.wikipedia.org/wiki/Value_object'>Value Object</a>.
 * This implementation is immutable and also supports a JSON representation.
 * </p>
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public final class Message {
  private static final Logger LOG = Logger.getLogger(Message.class.getName());
  private static final String GAME_WON_MSG = "You won the game against ";
  private static final String GAME_LOST_MSG = "You lost the game against ";
  private static final String RESIGNED_MSG = " has resigned";
  private static final String PREVIOUS_MOVE_MADE_MSG = "A previous move has already been made!";
  private static final String JUMP_AVAILABLE_MSG_ROW = "A jump move is available with your piece at ROW: ";
  private static final String JUMP_AVAILABLE_MSG_CELL = ", COL: ";
  private static final String VALID_MOVE_MSG = "A valid move!";
  private static final String BAD_MOVE_SINGLE_MSG = "A single piece may not move in such a way";
  private static final String BAD_MOVE_KING_MSG = "A king piece may not move in such a way";
  private static final String NO_JUMP_AFTER_SINGLE_MSG = "You may not jump after a standard move.";
  private static final String REALLY_BAD_MOVE_MSG = "You definitely cannot move like that, wow.";
  private static final String BAD_JUMP_SINGLE_MSG = "A single piece may not jump like that";
  private static final String BAD_JUMP_KING_MSG = "A king piece may not jump like that";
  private static final String NO_BACKTRACKING_MSG = "Backtracking is not allowed!";

  //
  // Static Factory methods
  //

  /**
   * A static helper method to create new error messages.
   *
   * @param message  the text of the message
   *
   * @return a new {@link Message}
   */
  public static Message error(final String message) {
    return new Message(message, Type.ERROR);
  }

  /**
   * A static helper method to create new informational messages.
   *
   * @param message  the text of the message
   *
   * @return a new {@link Message}
   */
  public static Message info(final String message) {
    return new Message(message, Type.INFO);
  }

  //
  // Inner Types
  //

  /**
   * The type of {@link Message}; either information or an error.;
   */
  public enum Type {
    INFO, ERROR
  }

  //
  // Attributes
  //

  private final String text;
  private final Type type;

  //
  // Constructor
  //

  /**
   * Create a new message.
   *
   * @param message  the text of the message
   * @param type  the type of message
   */
  private Message(final String message, final Type type) {
    this.text = message;
    this.type = type;
    LOG.finer(this + " created.");
  }

  //
  // Public methods
  //

  /**
   * Get the text of the message.
   */
  public String getText() {
    return text;
  }

  /**
   * Get the type of the message.
   */
  public Type getType() {
    return type;
  }

  /**
   * Query whether this message was generated from a successful
   * action; ie, not an {@link Type#ERROR}.
   *
   * @return true if not an error
   */
  public boolean isSuccessful() {
    return !type.equals(Type.ERROR);
  }


  public static Message getWinningMessage(Player opponent) {
    return Message.info(GAME_WON_MSG + opponent.getUsername());
  }

  public static Message getLosingMessage(Player opponent) {
    return Message.info(GAME_LOST_MSG + opponent.getUsername());
  }

  public static Message getResigningMessage(Player opponent) {
    return Message.info(opponent.getUsername() + RESIGNED_MSG);
  }

  public static Message getPreviousMoveMadeMessage() {
  	return Message.error(PREVIOUS_MOVE_MADE_MSG);
  }

  public static Message getJumpMoveAvailableMessage(Position jumpPosition) {
  	return Message.error(JUMP_AVAILABLE_MSG_ROW + jumpPosition.getRow() +
			JUMP_AVAILABLE_MSG_CELL + jumpPosition.getCell());
  }

  public static Message getInvalidMoveMessage() {
  	return Message.error(REALLY_BAD_MOVE_MSG);
  }

  public static Message getBadMoveSingleMessage() {
  	return Message.error(BAD_MOVE_SINGLE_MSG);
  }

  public static Message getBadMoveKingMessage() {
  	return Message.error(BAD_MOVE_KING_MSG);
  }

  public static Message getValidMoveMessage() {
  	return Message.info(VALID_MOVE_MSG);
  }

  public static Message getNoJumpAfterSingleMessage() {
  	return Message.error(NO_JUMP_AFTER_SINGLE_MSG);
  }

  public static Message getBadJumpSingleMessage() {
  	return Message.error(BAD_JUMP_SINGLE_MSG);
  }

  public static Message getBadJumpKingMessage() {
  	return Message.error(BAD_JUMP_KING_MSG);
  }

  public static Message getNoBacktrackingMessage() {
  	return Message.error(NO_BACKTRACKING_MSG);
  }

  //
  // Object methods
  //

  @Override
  public String toString() {
    return "{Msg " + type + " '" + text + "'}";
  }

}
