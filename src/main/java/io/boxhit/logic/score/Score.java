package io.boxhit.logic.score;

public enum Score {
    ATTACK(5),
    KILL(10),
    DEATH(-10),
    WIN(100),
    LOSE(-100);

    private final int score;

    Score(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
