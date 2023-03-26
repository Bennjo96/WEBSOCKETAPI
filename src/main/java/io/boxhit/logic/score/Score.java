package io.boxhit.logic.score;

public enum Score {
    ATTACK(5), //depending on how many players were hit
    KILL(10),
    DEATH(-10),
    WIN(20),
    DRAW(5);

    private final int score;

    Score(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
