package io.boxhit.socket.database.stats;

import io.boxhit.socket.database.playlog.PlayLog;
import io.boxhit.socket.database.playlog.PlayLogRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class StatsRepositoryCustomImpl implements StatsRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    public List<PlayLog> getPlayLogFromPlayerId(Long playerId) {
        List<PlayLog> playLogs = new ArrayList<>();
        System.out.println("PlayLogRepositoryCustomImpl.getPlayLogFromPlayerId()");

        entityManager.createNativeQuery("SELECT * FROM playlog WHERE userid = :userid ORDER BY lastplayed DESC", PlayLog.class)
                .setParameter("userid", playerId)
                .getResultList()
                .forEach(playLog -> playLogs.add((PlayLog) playLog));

        System.out.println("PlayLogRepositoryCustomImpl.getPlayLogFromPlayerId() playLogs.size() = " + playLogs.size());

        return playLogs;
    }

    @Transactional
    public void insertPlayLog(PlayLog playLog) {
        entityManager.createNativeQuery("INSERT INTO playlog (userid, playercount, score, lastplayed) VALUES (:userid, :playercount, :score, :lastplayed)")
                .setParameter("userid", playLog.getUserid())
                .setParameter("playercount", playLog.getPlayercount())
                .setParameter("score", playLog.getScore())
                .setParameter("lastplayed", playLog.getLastplayed())
                .executeUpdate();
    }

    @Override
    public List<String> getScore() {
        return null;
    }

    @Override
    public int getPlayerScore(Long playerId) {
        return 0;
    }

    @Override
    @Transactional
    public void upsert(Stats stats) {
        entityManager.createNativeQuery("INSERT INTO stats (userid, score) VALUES (:userid, :score) ON DUPLICATE KEY UPDATE score = :score")
                .setParameter("userid", stats.getUserid())
                .setParameter("score", stats.getScore())
                .executeUpdate();
    }

    @Override
    public Stats getStatsFromPlayerId(Long playerId) {
        System.out.println("StatsRepositoryCustomImpl.getStatsFromPlayerId()");

        Stats stat = (Stats) entityManager.createNativeQuery("SELECT * FROM stats WHERE userid = :userid", Stats.class)
                .setParameter("userid", playerId)
                .getSingleResult();
        return stat;
    }
}
