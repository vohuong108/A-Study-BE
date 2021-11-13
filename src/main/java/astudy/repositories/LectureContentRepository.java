package astudy.repositories;

import astudy.models.LectureContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureContentRepository extends JpaRepository<LectureContent, Long> {
}
