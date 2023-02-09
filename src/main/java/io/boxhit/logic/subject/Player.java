package io.boxhit.logic.subject;

public class Player {

    /**
     * The player's id
     */
    private final int playerID;
    /**
     * The player's name
     */
    private final String name;
    /**
     * The player's score
     */
    private int score;
    /**
     * The player's color in hex
     */
    private String hexColor;
    /**
     * The player's x position
     */
    private int positionX;
    /**
     * The player's y position
     */
    private int positionY;
    /**
     * If the player is alive
     */
    boolean isAlive;
    /**
     * The player's cooldown for attacking
     */
    private int coolDownForAttack;

    /**
     * the Attack-Range
     */
    public final int ATTACK_RADIUS = 50;

    /**
     * Create a new Player
     * @param playerID the playerID
     * @param name the name
     * @param hexColor the color in hex
     */
    public Player(int playerID, String name, String hexColor) {
        this.playerID = playerID;
        this.name = name;
        this.score = 0;
        this.hexColor = hexColor;
        this.isAlive = true;
        this.coolDownForAttack = 10;
    }

    /**
     * Get the player's id
     * @return the player's id
     */
    public int getPlayerID() {
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
     * Get if the player is alive
     * @return if the player is alive
     */
    public boolean isAlive() {
        return isAlive;
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

    /**
     * Set the player's y position
     * @param positionY the new y position
     */
    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    /**
     * Set if the player is alive
     * @param isAlive if the player is alive
     */
    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    /**
     * Set the player's cooldown for attacking
     * @param coolDownForAttack the new cooldown for attacking
     */
    public void setCoolDownForAttack(int coolDownForAttack) {
        this.coolDownForAttack = coolDownForAttack;
    }

    /**
     * Moves the Player on the Screen
     * @param x the x position
     * @param y the y position
     */
    public void move(int x, int y) {
        this.positionX = x;
        this.positionY = y;
    }
}
