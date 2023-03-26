package io.boxhit.socket.database.stats;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.context.annotation.Primary;

@Entity
@Table(name = "stats")
public class Stats {
    @Id
    @Column(unique=true)
    private Long userid;

    @Column()
    private int score;

    @Column()
    private Long lastUpdated;


    public Long getUserid() {
        return userid;
    }

    public int getScore() {
        return score;
    }


    public Stats setUserid(Long userid) {
        this.userid = userid;
        return this;
    }

    public Stats setScore(int score) {
        this.score = score;
        return this;
    }
}
