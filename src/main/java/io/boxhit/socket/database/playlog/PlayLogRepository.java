package io.boxhit.socket.database.playlog;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayLogRepository extends JpaRepository<PlayLog, Long>, PlayLogRepositoryCustom {
}
