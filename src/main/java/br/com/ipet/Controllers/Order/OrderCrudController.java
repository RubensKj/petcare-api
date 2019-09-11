package br.com.ipet.Controllers.Order;

import br.com.ipet.Models.*;
import br.com.ipet.Security.JWT.JwtProvider;
import br.com.ipet.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
    private CompanyService companyService;

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

        if (order.getProductsIdsCart().isEmpty() && order.getServicesIdsCart().isEmpty()) {
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

    @GetMapping("/get-orders-company/{page}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('OWNER')")
    public Page<Order> getCompaniesOrder(@PathVariable("page") int pageNumber, HttpServletRequest req) {
        String jwtToken = jwtProvider.getJwt(req);
        String emailCompanyLogged = jwtProvider.getEmailFromJwtToken(jwtToken);
        Company company = companyService.findByOwnerEmail(emailCompanyLogged);
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by("createdOrderAt").descending());
        if (company != null || emailCompanyLogged != null) {
            return orderService.findByIdInAndStatusOrderIsNot(company.getOrders(), StatusOrder.FINISHED, pageable);
        }
        return null;
    }

    @GetMapping("/orders/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<?> getOrderById(@PathVariable("id") long id, HttpServletRequest req) {
        String jwtToken = jwtProvider.getJwt(req);
        String emailUserLogged = jwtProvider.getEmailFromJwtToken(jwtToken);
        Order order = orderService.findById(id);
        if (order != null && emailUserLogged.equals(order.getEmailOrderUser())) {
            return ResponseEntity.ok(order);
        }
        return new ResponseEntity<>("Any company connected on application",
                HttpStatus.FORBIDDEN);
    }

    @GetMapping("/orders-to-company/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<?> getOrderByIdInCompany(@PathVariable("id") long id, HttpServletRequest req) {
        String jwtToken = jwtProvider.getJwt(req);
        String emailUserLogged = jwtProvider.getEmailFromJwtToken(jwtToken);
        Order order = orderService.findById(id);
        if (order != null && emailUserLogged != null) {
            return ResponseEntity.ok(order);
        }
        return new ResponseEntity<>("Any company connected on application",
                HttpStatus.FORBIDDEN);
    }

    @PutMapping("/orders-process/{id}/{numberProcess}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public void setProcessOfOrder(@PathVariable("id") long id, @PathVariable("numberProcess") int number, HttpServletRequest req) {
        String jwtToken = jwtProvider.getJwt(req);
        String emailUserLogged = jwtProvider.getEmailFromJwtToken(jwtToken);
        Order order = orderService.findById(id);
        if (order != null && emailUserLogged != null) {
            switch (number) {
                case 1:
                    order.setStatusOrder(StatusOrder.PAID);
                    orderService.save(order);
                    break;
                case 2:
                    order.setStatusOrder(StatusOrder.PROCESS);
                    orderService.save(order);
                    break;
                case 3:
                    order.setStatusOrder(StatusOrder.DEVELIVERYING);
                    orderService.save(order);
                    break;
                case 4:
                    order.setStatusOrder(StatusOrder.FINISHED);
                    orderService.save(order);
                    break;
            }
        }
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
        Company company = companyService.findByCnpj(order.getCnpj());
        user.getOrders().add(order.getId());
        company.getOrders().add(order.getId());
        userService.save(user);
        companyService.save(company);
    }
}
