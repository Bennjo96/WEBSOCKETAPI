package io.boxhit.socket.database.playlog;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "playlog")
public class PlayLog {
    @Id
    private Long id;

    @Column()
    private Long userid;

    @Column()
    private int playercount;

    @Column()
    private int score;

    @Column()
    private Timestamp lastplayed;

    public Long getId() {
        return id;
    }

    public Long getUserid() {
        return userid;
    }

    public int getPlayercount() {
        return playercount;
    }

    public int getScore() {
        return score;
    }

    public Timestamp getLastplayed() {
        return lastplayed;
    }

    public PlayLog setId(Long id) {
        this.id = id;
        return this;
    }

    public PlayLog setUserid(Long userid) {
        this.userid = userid;
        return this;
    }

    public PlayLog setPlayercount(int playercount) {
        this.playercount = playercount;
        return this;
    }

    public PlayLog setScore(int score) {
        this.score = score;
        return this;
    }

    public PlayLog setLastplayed(Timestamp lastplayed) {
        this.lastplayed = lastplayed;
        return this;
    }
}
