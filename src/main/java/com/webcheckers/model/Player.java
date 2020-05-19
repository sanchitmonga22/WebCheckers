package com.webcheckers.model;

import com.webcheckers.util.Replay;

import java.util.ArrayList;
import java.util.List;

/**
 * This class that is to represent a user joining the web server
 * to spectate or compete in a checkers game. The user may have
 * a name (and eventually password), along with a count of wins
 * and losses. Maintains characteristics of a given user even
 * when the user signs out and back in.
 *
 * @author <cjn9414@rit.edu> Carter Nesbitt
 */
public class Player {
    /**
     * Username assigned when object is instantiated.
     */
    private final String name;

    /**
     * Private recount of global wins and losses.
     */
    private int wins, losses;
    public boolean inMatch, spectating;
    public Replay replay;
    public boolean inReplay;

    /**
     * Constructs the player object.
     * @param name: Mandatory unique identifier for a given user.
     */
    public Player(String name) {
        this.name = name;
        this.wins = 0;
        this.losses = 0;
        this.inMatch = false;
        this.inReplay = false;
        this.replay = null;
    }

    /**
     * Fetches the username associated with the given User object.
     * @return String representing the username assigned to a user.
     */
    public String getUsername() {
        return this.name;
    }

    /**
     * Calculates the win-loss ratio of a given user.
     * @return Floating point value to represent a
     *         user's win-loss ratio.
     */
    public float fetchWinLoss() {
        if (this.losses == 0) {
            return -1.0f;
        } else {
            return (float) this.wins / (float) this.losses;
        }
    }

    /**
     * Performs comparison on two player objects.
     * @param other: A second object being compared.
     * @return True if the username of both objects are equal.
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Player) {
            Player player = (Player) other;
            return this.getUsername().equals(player.getUsername());
        } else {
            return false;
        }
    }
}
