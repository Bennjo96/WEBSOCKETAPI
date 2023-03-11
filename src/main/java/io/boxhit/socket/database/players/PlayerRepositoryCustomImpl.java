package io.boxhit.socket.database.players;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class PlayerRepositoryCustomImpl implements PlayerRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Player> findPlayerBySessionToken(Set<String> sessionToken) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Player> cq = cb.createQuery(Player.class);
        Root<Player> root = cq.from(Player.class);

        Path<String> sessionTokenPath = root.get("sessionToken");
        List<Predicate> predicates = new ArrayList<>();

        for (String token : sessionToken) {
            predicates.add(cb.like(sessionTokenPath, token));
        }

        cq.select(root).where(cb.or(predicates.toArray(new Predicate[predicates.size()])));

        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<Player> findAllPlayersByPredicate(Collection<java.util.function.Predicate<Player>> predicate) {
        List<Player> allPlayers = entityManager.createQuery("SELECT p FROM Player p", Player.class).getResultList();
        Stream<Player> allPlayersStream = allPlayers.stream();
        for (java.util.function.Predicate<Player> playerPredicate : predicate) {
            allPlayersStream = allPlayersStream.filter(playerPredicate);
        }
        return allPlayersStream.toList();
    }
}
