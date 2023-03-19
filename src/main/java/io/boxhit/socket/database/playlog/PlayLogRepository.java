package io.boxhit.socket.database.playlog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayLogRepository extends JpaRepository<PlayLog, Long>, PlayLogRepositoryCustom {


    //@Modifying
    //@Query("INSERT INTO PlayLog (userid, playcount, score, lastplayed, color) VALUES (:userid, :playcount, :score, :lastplayed, :color)")
    //void insertPlayLog(@Param("userid") Long userid, @Param("playcount") Integer playcount, @Param("score") Integer score, @Param("lastplayed") Long lastplayed, @Param("color") String color);

    //get whole playlog for a player in a list




}
