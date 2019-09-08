package br.com.ipet.Controllers.Order;

import br.com.ipet.Models.Order;
import br.com.ipet.Models.Product;
import br.com.ipet.Models.StatusOrder;
import br.com.ipet.Models.User;
import br.com.ipet.Security.JWT.JwtProvider;
import br.com.ipet.Services.AddressService;
import br.com.ipet.Services.OrderService;
import br.com.ipet.Services.ProductService;
import br.com.ipet.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Set;

@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.25.17:3000", "http://192.168.0.73:3000"})
@RestController
@RequestMapping("/api")
public class OrderCrudController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ProductService productService;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/finishing-order")
    @Transactional
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<String> saveOrder(@Valid @RequestBody Order order, HttpServletRequest req) {
        if (order.getNameOfCompany().isEmpty()) {
            return ResponseEntity.ok("Order without company's name.");
        }

        if (order.getEmailOrderUser().isEmpty()) {
            return ResponseEntity.ok("Order without user's email.");
        }

        if (order.getProductsIdsCart().isEmpty() || order.getServicesIdsCart().isEmpty()) {
            return ResponseEntity.ok("Order without items!");
        }

        if (order.getPaymentMethod() != null) {
            Set<Product> allProductsByIds = productService.findAllProductsByIds(order.getProductsIdsCart());
            allProductsByIds.forEach(product -> {
                product.setQuantityStore(product.getQuantityStore() - 1);
                productService.save(product);
            });
            System.out.println(order.getPaymentMethod());
            switch (order.getPaymentMethod()) {
                case MONEY:
                    order.setStatusOrder(StatusOrder.NOT_PAID);
                    saveOrderAndAddOnUserOrders(order, req);
                case CREDIT_CARD:
                    // Method to pay
                    order.setStatusOrder(StatusOrder.NOT_PAID);
                    saveOrderAndAddOnUserOrders(order, req);
                case DEBIT_CARD:
                    // Method to pay
                    order.setStatusOrder(StatusOrder.NOT_PAID);
                    saveOrderAndAddOnUserOrders(order, req);
            }
        }
        return ResponseEntity.ok("Order was created");
    }

    @GetMapping("/get-orders/{page}")
    @Transactional
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('OWNER')")
    public Page<Order> getOrdersFromUser(@PathVariable("page") int pageNumber, HttpServletRequest req) {
        User user = getUserLogger(req);
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by("createdOrderAt").descending());
        return orderService.findByIdInAndStatusOrderIsNot(user.getOrders(), StatusOrder.FINISHED, pageable);
    }

    @GetMapping("/get-orders-finished/{page}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('OWNER')")
    public Page<Order> getOrdersFinishedFromUser(@PathVariable("page") int pageNumber, HttpServletRequest req) {
        User user = getUserLogger(req);
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by("createdOrderAt").descending());
        return orderService.findByIdInAndStatusOrderIs(user.getOrders(), StatusOrder.FINISHED, pageable);
    }

    @GetMapping("/orders/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<Order> getOrderById(@PathVariable("id") long id, HttpServletRequest req) {
        String jwtToken = jwtProvider.getJwt(req);
        String emailUserLogged = jwtProvider.getEmailFromJwtToken(jwtToken);
        Order order = orderService.findById(id);
        if (order != null && emailUserLogged.equals(order.getEmailOrderUser())) {
            return ResponseEntity.ok(order);
        }
        return null;
    }

    private User getUserLogger(HttpServletRequest req) {
        String jwtToken = jwtProvider.getJwt(req);
        String emailUserLogged = jwtProvider.getEmailFromJwtToken(jwtToken);
        return userService.findByEmail(emailUserLogged);
    }

    private void saveOrderAndAddOnUserOrders(Order order, HttpServletRequest req) {
        // Save order to get ID
        orderService.save(order);
        addressService.save(order.getCompanyOrderAddress());

        // get user logged in application
        String jwtToken = jwtProvider.getJwt(req);
        String emailUserLogged = jwtProvider.getEmailFromJwtToken(jwtToken);

        // find user and add on his orders
        User user = userService.findByEmail(emailUserLogged);
        user.getOrders().add(order.getId());
        userService.save(user);
    }
}
