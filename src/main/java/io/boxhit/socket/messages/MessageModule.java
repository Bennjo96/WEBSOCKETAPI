package io.boxhit.socket.messages;

public enum MessageModule {

    SERVER_LIST("serverlist"),
    AUTHORIZATION("authorization"),
    ERROR("error"),
    REQUEST_JOIN_GAME("joinGameRequest"),
    REQUEST_GAME_DATA("gameDataRequest"),
    ACTION_JOIN_GAME_DENIED("joinGameDenied"),
    ACTION_JOIN_GAME("joinGame"),
    ACTION_GAME_DATA("gameData"),
    ACTION_JOIN_GAME_OTHER("joinGameOther"),
    ACTION_LEAVE_GAME("leaveGame"),
    ACTION_LEAVE_GAME_OTHER("leaveGameOther"),
    ACTION_GAME_INFO_MESSAGE("gameInfoMessage"),
    ACTION_GAME_MOVE("gameMovePlayer"),
    ACTION_GAME_MOVE_OTHER("gameMovePlayerOther"),
    ACTION_GAME_ATTACK("gameAttack"),
    ACTION_GAME_ATTACK_OTHER("gameAttackOther");

    private String module;

    private MessageModule(String module) {
        this.module = module;
    }

    public String getModule() {
        return module;
    }

    public static MessageModule fromText(String module) {
        for (MessageModule m : values()) {
            if (m.getModule().equalsIgnoreCase(module)) {
                return m;
            }
        }
        return null;
    }

}
