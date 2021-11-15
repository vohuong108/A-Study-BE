package astudy.repositories;

import astudy.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(
            value = "SELECT * FROM user u WHERE u.username = :username",
            nativeQuery = true)
    User findUserByUsername(@Param("username") String username);

    @Query(
            value = "SELECT username FROM user u WHERE u.ID = :userId",
            nativeQuery = true)
    String findUsernameById(@Param("userId") Long userId);

    @Query(
            value = "SELECT ID FROM user u WHERE u.username = :username",
            nativeQuery = true)
    Long findUserIdByUsername(@Param("username") String username);
}
