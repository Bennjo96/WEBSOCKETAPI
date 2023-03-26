package io.boxhit.socket.database.stats;

import io.boxhit.socket.database.playlog.PlayLog;
import io.boxhit.socket.database.playlog.PlayLogRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatsRepository extends JpaRepository<Stats, Long>, StatsRepositoryCustom {
    boolean existsByUserid(Long userid);

}
