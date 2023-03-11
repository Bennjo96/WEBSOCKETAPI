package io.boxhit.socket.database.players;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public interface PlayerRepositoryCustom {

    List<Player> findPlayerBySessionToken(Set<String> sessionToken);

    List<Player> findAllPlayersByPredicate(Collection<Predicate<Player>> predicate);
}
