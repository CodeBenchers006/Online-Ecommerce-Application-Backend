package codebenchers006.ecommerce.repository;

import codebenchers006.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Integer> {
    public User findByEmail(String email);
}
