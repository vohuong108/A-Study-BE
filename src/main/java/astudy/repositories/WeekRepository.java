package astudy.repositories;

import astudy.models.Week;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeekRepository extends JpaRepository<Week, Long> {
}
