package astudy.repositories;

import astudy.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    @Query(
            value = "SELECT user_role FROM user u WHERE u.username = :username",
            nativeQuery = true)
    Long findRoleIdByUsername(@Param("username") String username);

    @Query(
            value = "SELECT u.ID, u.email, u.username, r.name AS role ,u.status " +
                    "FROM user u JOIN role r " +
                    "ON u.user_role = r.ID",
            nativeQuery = true)
    String[][] findAllUser();

    @Modifying
    @Transactional
    @Query(
            value = "UPDATE user " +
                    "SET status = :type " +
                    "WHERE user.ID = :userId",
            nativeQuery = true)
    void updateUserStatus(@Param("userId") Long userId, @Param("type") String type);

    @Modifying
    @Transactional
    @Query(
            value = "UPDATE user " +
                    "SET user_role = (SELECT role.ID FROM role WHERE role.name = :type LIMIT 1) " +
                    "WHERE user.ID = :userId",
            nativeQuery = true)
    void updateUserRole(@Param("userId") Long userId, @Param("type") String type);
}
