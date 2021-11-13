package astudy.repositories;

import astudy.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(
            value = "SELECT * FROM category WHERE category.name = :name",
            nativeQuery = true)
    Category findByName(@Param("name") String name);

    @Override
    @Query(
            value = "SELECT * FROM category WHERE category.ID = :categoryId",
            nativeQuery = true)
    Category getById(@Param("categoryId") Long categoryId);
}
