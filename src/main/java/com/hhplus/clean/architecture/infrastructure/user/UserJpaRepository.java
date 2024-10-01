package com.hhplus.clean.architecture.infrastructure.user;

import com.hhplus.clean.architecture.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<User,Long> {
}
