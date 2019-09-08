package br.com.ipet.Repository;

import br.com.ipet.Models.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Set;

public interface ServiceRepository extends PagingAndSortingRepository<Service, Long> {
    Service findByName(String name);
    Service findById(long id);
    Page<Service> findProductsByIdIn(Set<Long> ids, Pageable pageable);

    Page<Service> findByIdIn(Set<Long> ids, Pageable pageable);

    Set<Service> findProductsByIdIn(Set<Long> ids);
}
