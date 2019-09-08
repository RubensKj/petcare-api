package br.com.ipet.Services;

import br.com.ipet.Models.Order;
import br.com.ipet.Models.StatusOrder;
import br.com.ipet.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public void save(Order order) {
        orderRepository.save(order);
    }

    public void removeById(long id) { orderRepository.deleteById(id); }

    public Order findById(long id) { return orderRepository.findById(id); }

    public boolean existsById(Long id) {
        return orderRepository.existsById(id);
    }

    public Page<Order> findByIdInAndStatusOrderIsNot(Set<Long> ids, StatusOrder statusOrder, Pageable pageable) {
        return orderRepository.findByIdInAndStatusOrderIsNot(ids, statusOrder, pageable);
    }

    public Page<Order> findByIdInAndStatusOrderIs(Set<Long> ids, StatusOrder statusOrder, Pageable pageable) {
        return orderRepository.findByIdInAndStatusOrderIs(ids, statusOrder, pageable);
    }
}
