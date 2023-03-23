package io.boxhit.logic.subject;

public class Player {
    private final String playerID; //id from websocket connection
    private final String name;
    private int score;
    private String hexColor;
    private int positionX;
    private int positionY;
    private int coolDownForAttack;
    public final int ATTACK_RADIUS = 90;
    private State state;
    private int currentGameID;
    private int health = MAX_HEALTH;
    public static int MAX_HEALTH = 10;

    /**
     * Create a new Player
     * @param playerID the playerID
     * @param name the name
     * @param hexColor the color in hex
     */
    public Player(String playerID, String name, String hexColor) {
        this.playerID = playerID;
        this.name = name;
        this.score = 0;
        this.hexColor = hexColor;
        this.coolDownForAttack = 10;
    }

    /**
     * Get the player's id
     * @return the player's id
     */
    public String getPlayerID() {
        return playerID;
    }

    /**
     * Get the player's name
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the player's score
     * @return the player's score
     */
    public int getScore() {
        return score;
    }

    /**
     * Get the player's color in hex
     * @return the player's color in hex
     */
    public String getHexColor() {
        return hexColor;
    }

    /**
     * Get the player's x position
     * @return the player's x position
     */
    public int getPositionX() {
        return positionX;
    }

    /**
     * Get the player's y position
     * @return the player's y position
     */
    public int getPositionY() {
        return positionY;
    }

    /**
     * Get the player's cooldown for attacking
     * @return the player's cooldown for attacking
     */
    public int getCoolDownForAttack() {
        return coolDownForAttack;
    }

    /**
     * Set the player's score
     * @param score the new score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Set the player's x position
     * @param positionX the new x position
     */
    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getHealth() {
        return health;
    }

    public Player setHealth(int health) {
        this.health = health;
        return this;
    }

    /**
     * Set the player's y position
     * @param positionY the new y position
     */
    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    /**
     * Set the player's cooldown for attacking
     * @param coolDownForAttack the new cooldown for attacking
     */
    public void setCoolDownForAttack(int coolDownForAttack) {
        this.coolDownForAttack = coolDownForAttack;
    }

    public State getState() {
        return state;
    }

    public Player setState(State state) {
        this.state = state;
        return this;
    }

    public int getCurrentGameID() {
        return currentGameID;
    }

    public Player setCurrentGameID(int currentGameID) {
        this.currentGameID = currentGameID;
        return this;
    }

    /**
     * Moves the Player on the Screen
     * @param x the x position
     * @param y the y position
     */
    public void move(int x, int y) {
        this.positionX = this.positionX+x;
        this.positionY = this.positionY+y;
    }

    public enum State {
        IDLE, //player is either in the lobby, leaderboards, ...
        PLAYING, //player is in a running game
        DEAD, //player is dead
        WAITING, //player is waiting for the game to start
        JOINING  //player is joining a game - reserving a slot
    }
}
