package codebenchers006.ecommerce.repository;

import codebenchers006.ecommerce.model.User;
import codebenchers006.ecommerce.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepo extends JpaRepository<Wishlist,Integer> {
    List<Wishlist> findAllByUserOrderByCreatedDateDesc(User user);
}
