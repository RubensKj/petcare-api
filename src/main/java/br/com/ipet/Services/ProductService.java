package br.com.ipet.Services;

import br.com.ipet.Models.Product;
import br.com.ipet.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public void save(Product product) {
        productRepository.save(product);
    }

    public void remove(Product product) { productRepository.delete(product); }

    public void removeById(long id) { productRepository.deleteById(id); }

    public Product findById(long id) { return productRepository.findById(id); }

    public Boolean existsById(long id) { return productRepository.existsById(id); }

    public Page<Product> findAllByPage(Pageable page) { return productRepository.findAll(page); }

    public Product findByProduct(String name) {
        return productRepository.findByName(name);
    }

    public Page<Product> findAllProducts(Set<Long> ids, Pageable pageable) { return productRepository.findProductsByIdIn(ids, pageable); }
}
