package io.boxhit.socket.database.playlog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayLogRepository extends JpaRepository<PlayLog, Long>, PlayLogRepositoryCustom {
}
