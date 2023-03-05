package codebenchers006.ecommerce.repository;

import codebenchers006.ecommerce.model.Order;
import codebenchers006.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findAllByUserOrderByCreatedDateDesc(User user);

    List<Order> findAllByUserOrderByIdDesc(User user);
}
