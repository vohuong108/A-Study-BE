package astudy.repositories;

import astudy.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(
            value = "SELECT * FROM role WHERE role.name = :roleName",
            nativeQuery = true)
    Role findRoleByName(@Param("roleName") String roleName);
}
