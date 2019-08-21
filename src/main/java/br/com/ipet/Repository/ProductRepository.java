package br.com.ipet.Repository;

import br.com.ipet.Models.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
    Product findByName(String name);
}
