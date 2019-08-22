package br.com.ipet.Repository;

import br.com.ipet.Models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Set;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
    Product findByName(String name);
    Page<Product> findProductsByIdIn(Set<Long> ids, Pageable pageable);
}
