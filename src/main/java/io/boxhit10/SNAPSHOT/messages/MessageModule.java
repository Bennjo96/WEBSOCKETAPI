package io.boxhit10.SNAPSHOT.messages;

public enum MessageModule {

    /**
     * Servers responses to client login requests
     */
    RESPONSE_LOGIN("loginResponse"),
    /**
     * Servers responses to client color change requests
     */
    RESPONSE_CHANGE_COLOR("changeColorResponse"),
    /**
     * Servers sends a list of all available game instances
     */
    MESSAGE_SERVER_LIST("serverListMessage"),
    /**
     * Servers sends a list of the leaderboard from a specific game instance
     */
    MESSAGE_LEADERBOARD("leaderboardMessage"),
    /**
     * Servers resonses to client's game join requests
     */
    RESPONSE_ACTION_JOIN_GAME("joinGameResponse"),
    /**
     * Servers sends a join game message to all other clients in the game
     */
    MESSAGE_ACTION_JOIN_GAME_OTHER("joinGameOtherResponse"),
    /**
     * Servers responses to client's game leave requests
     */
    RESPONSE_ACTION_LEAVE_GAME("leaveGameResponse"),
    /**
     * Server sends a ping message to all clients
     */
    MESSAGE_PING("ping");

    private String module;

    private MessageModule(String module) {
        this.module = module;
    }

    public String getModule() {
        return module;
    }

}
