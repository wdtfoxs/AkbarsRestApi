package ru.akbarsdigital.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akbarsdigital.restapi.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(nativeQuery = true, value = "SELECT EXISTS (SELECT 1 FROM app_user WHERE email = :email)")
    boolean existsByEmail(@Param("email") String email);

    @Query(nativeQuery = true, value = "SELECT EXISTS (SELECT 1 FROM app_user WHERE phone = :phone)")
    boolean existsByPhone(@Param("phone") String phone);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE app_user SET confirmed = TRUE WHERE phone = :phone")
    void confirmUser(@Param("phone") String phone);

    Optional<User> findByPhone(String phone);

    Optional<User> findByEmail(String email);
}
