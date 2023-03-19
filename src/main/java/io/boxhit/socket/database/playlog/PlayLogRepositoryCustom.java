package io.boxhit.socket.database.playlog;

import io.boxhit.socket.database.players.Player;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public interface PlayLogRepositoryCustom {

    List<PlayLog> getPlayLogFromPlayerId(Long playerId);

    void insertPlayLog(PlayLog playLog);

}
