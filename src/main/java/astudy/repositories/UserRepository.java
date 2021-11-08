package astudy.repositories;

import astudy.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(
            value = "SELECT * FROM user u WHERE u.username = :username",
            nativeQuery = true)
    User findUserByUsername(@Param("username") String username);

    @Query(
            value = "SELECT * FROM user u WHERE u.ID = :userId",
            nativeQuery = true)
    User findUserById(@Param("userId") String userId);
}
