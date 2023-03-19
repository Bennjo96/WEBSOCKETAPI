package io.boxhit.socket.database.playlog;

import io.boxhit.socket.database.players.Player;
import io.boxhit.socket.database.players.PlayerRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

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

        entityManager.createNativeQuery("SELECT * FROM playlog WHERE userid = :userid", PlayLog.class)
                .setParameter("userid", playerId)
                .getResultList()
                .forEach(playLog -> playLogs.add((PlayLog) playLog));

        return playLogs;
    }
}
