package codebenchers006.ecommerce.service;

import codebenchers006.ecommerce.dto.ProductDTO;
import codebenchers006.ecommerce.model.User;
import codebenchers006.ecommerce.model.Wishlist;
import codebenchers006.ecommerce.repository.WishlistRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WishlistService {

    @Autowired
    WishlistRepo wrepo;

    @Autowired
    ProductService productService;

    public void createWishList(Wishlist wishlist) {

        wrepo.save(wishlist);

    }

    public List<ProductDTO> getWishListItemsForUser(User user) {
        final List<Wishlist> wishlist = wrepo.findAllByUserOrderByCreatedDateDesc(user);
        List<ProductDTO> prods = new ArrayList<>();

        for(Wishlist list : wishlist ){
            prods.add(productService.getProductDto(list.getProduct()));
        }

        return prods;
    }
}
