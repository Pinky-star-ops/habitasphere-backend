package com.habitasphere.habitasphere.repository;
import com.habitasphere.habitasphere.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);

    
} 
