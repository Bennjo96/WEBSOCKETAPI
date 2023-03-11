package io.boxhit.socket.database.players;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PlayerRepository extends JpaRepository<Player, Long>, PlayerRepositoryCustom {

    @Query("SELECT p FROM Player p WHERE p.sessionToken = ?1")
    Player findPlayerBySessionToken(String sessionToken);

    @Modifying
    @Query("UPDATE Player p SET p.sessionToken = ?1 WHERE p.id = ?2")
    void updatePlayerSessionToken(@Param("sessionToken") String sessionToken, @Param("id") Long id);

}
