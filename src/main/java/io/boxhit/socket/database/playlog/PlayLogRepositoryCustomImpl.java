package io.boxhit.socket.database.playlog;

import io.boxhit.socket.database.players.Player;
import io.boxhit.socket.database.players.PlayerRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class PlayLogRepositoryCustomImpl implements PlayLogRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<PlayLog> getPlayLogFromPlayerId(Long playerId) {
        List<PlayLog> playLogs = new ArrayList<>();
        System.out.println("PlayLogRepositoryCustomImpl.getPlayLogFromPlayerId() "+playerId);

        entityManager.createNativeQuery("SELECT * FROM playlog WHERE userid = :userid ORDER BY lastplayed DESC", PlayLog.class)
                .setParameter("userid", playerId)
                .getResultList()
                .forEach(playLog ->
                        playLogs.add((PlayLog) playLog)
                );

        System.out.println("PlayLogRepositoryCustomImpl.getPlayLogFromPlayerId() playLogs.size() = " + playLogs.size());

        return playLogs;
    }

    @Transactional
    @Override
    public void insertPlayLog(PlayLog playLog) {
        entityManager.createNativeQuery("INSERT INTO playlog (userid, playercount, score, lastplayed) VALUES (:userid, :playercount, :score, :lastplayed)")
                .setParameter("userid", playLog.getUserid())
                .setParameter("playercount", playLog.getPlayercount())
                .setParameter("score", playLog.getScore())
                .setParameter("lastplayed", playLog.getLastplayed())
                .executeUpdate();
    }
}
