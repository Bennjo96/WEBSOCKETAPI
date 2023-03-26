package io.boxhit.socket.database.stats;

import io.boxhit.socket.database.playlog.PlayLog;

import java.util.List;

public interface StatsRepositoryCustom {

    List<String> getScore();

    int getPlayerScore(Long playerId);

    void upsert(Stats stats);

    Stats getStatsFromPlayerId(Long playerId);

}
