package br.com.ipet.Repository;

import br.com.ipet.Models.Order;
import br.com.ipet.Models.StatusOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Set;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {
    Order findById(long id);
    Page<Order> findProductsByIdIn(Set<Long> ids, Pageable pageable);
    Page<Order> findByIdInAndStatusOrderIsNot(Set<Long> ids, StatusOrder statusOrder, Pageable pageable);
    Page<Order> findByIdInAndStatusOrderIs(Set<Long> ids, StatusOrder statusOrder, Pageable pageable);
}
