package codebenchers006.ecommerce.repository;

import codebenchers006.ecommerce.model.User;
import codebenchers006.ecommerce.model.UserShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserShippAddressRepo extends JpaRepository<UserShippingAddress,Integer> {

    public UserShippingAddress findByUser(User user);
}
