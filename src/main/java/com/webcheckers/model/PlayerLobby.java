package com.webcheckers.model;

import java.util.ArrayList;

/**
 * Maintains a record of all Player objects signed in to the webpage.
 * Allows for interaction between all players and the ability to
 * start a game with another player given that they are not yet in
 * a game.
 *
 * @author <cjn9414@rit.edu> Carter Nesbitt
 */
public class PlayerLobby {
    // Maintains a list of all players in the lobby.
    private ArrayList<Player> players;

    /**
     * Creates a new instance of a player lobby and
     * instantiates the structure to maintain all players in the lobby.
     */
    public PlayerLobby() {
        this.players = new ArrayList<>();
    }

    /**
     * Adds a new player to the game lobby.
     * @param player: Player object that is
     *             entering the game lobby.
     */
    public void playerJoin(Player player) {
        players.add(player);
    }


    /**
     * Retrieves the number of players in the lobby.
     * @return The number of players in the private list.
     */
    public int size() {
        return players.size();
    }

    /**
     * Fetches all players in the lobby.
     * @return ArrayList object of all players in the lobby.
     */
    public ArrayList<Player> fetchPlayers() {
        return players;
    }

    public Player fetchByUsername(String username) {
        for (Player player : players) {
            if (player.getUsername().equals(username)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Removes a player that is currently in the game lobby.
     * @param player: Player object that is being removed
     *              from the game lobby.
     * @return True if Player object successfully removed
     *         False if Player object not found in lobby.
     */
    public boolean removePlayer(Player player) {
        if (playerExists(player)) {
            players.remove(player);
            return true;
        }
        return false;
    }


    /**
     * Removes a player from the game lobby.
     * @param username: String of username of the player to remove.
     * @return True if player was successfully removed.
     *          False if player not found.
     */
    public boolean removePlayer(String username) {
        for (Player playerInLobby : players) {
            if (playerInLobby.getUsername().equals(username)) {
                players.remove(playerInLobby);
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if a player exists in the lobby.
     * @param player: Player checking for attendance in lobby.
     * @return True if player is in the lobby.
     *         False if player is not in the lobby.
     */
    public boolean playerExists(Player player) {
        for (Player playerInLobby : players) {
            if (playerInLobby.equals(player)) {
                return true;
            }
        }
        return false;
    }
}
