package br.com.ipet.Controllers.Service;

import br.com.ipet.Models.Company;
import br.com.ipet.Models.Order;
import br.com.ipet.Models.Service;
import br.com.ipet.Security.JWT.JwtProvider;
import br.com.ipet.Services.CompanyService;
import br.com.ipet.Services.OrderService;
import br.com.ipet.Services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@CrossOrigin(origins = {
        "http://localhost:3000",
        "https://aw-petcare-client.herokuapp.com",
        "https://aw-petcare-business.herokuapp.com",
        "http://aw-petcare-client.herokuapp.com",
        "http://aw-petcare-business.herokuapp.com",
        "https://petcare-client.now.sh/",
        "https://petcare-client.now.sh",
        "https://petcare-business.now.sh"
})
@RestController
@RequestMapping("/api")
public class ServiceCrudController {

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private JwtProvider jwtProvider;

    @GetMapping("/services/{page}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<Page<Service>> listServices(@PathVariable("page") int pageNumber, HttpServletRequest req) {
        String jwtToken = jwtProvider.getJwt(req);
        String emailOwnerLogged = jwtProvider.getEmailFromJwtToken(jwtToken);
        if (emailOwnerLogged != null) {
            Company company = companyService.findByOwnerEmail(emailOwnerLogged);
            Pageable pageable = PageRequest.of(pageNumber, 10);
            if (company != null) {
                return ResponseEntity.ok(serviceService.findAllServices(company.getServices(), pageable));
            } else {
                return ResponseEntity.ok(null);
            }
        } else {
            return ResponseEntity.ok(null);
        }
    }

    @GetMapping("/company-services/{id}/{page}")
    public ResponseEntity<Page<Service>> listIdsToServices(@PathVariable("id") long id, @PathVariable("page") int pageNumber) {
        if (companyService.existsById(id)) {
            Company company = companyService.findById(id);
            Pageable pageable = PageRequest.of(pageNumber, 10);
            return ResponseEntity.ok(serviceService.findByIds(company.getServices(), pageable));
        } else {
            return ResponseEntity.ok(null);
        }
    }

    @GetMapping("/order-services/{id}/{page}")
    public ResponseEntity<Page<Service>> listIdsToOrderServices(@PathVariable("id") long id, @PathVariable("page") int pageNumber) {
        if (orderService.existsById(id)) {
            Order order = orderService.findById(id);
            Pageable pageable = PageRequest.of(pageNumber, 10);
            return ResponseEntity.ok(serviceService.findByIds(order.getServicesIdsCart(), pageable));
        } else {
            return ResponseEntity.ok(null);
        }
    }

    @PostMapping("/create-service")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<String> saveProduct(@Valid @RequestBody Service service, HttpServletRequest req) {
        if (service != null) {
            String jwtToken = jwtProvider.getJwt(req);
            String emailOwnerLogged = jwtProvider.getEmailFromJwtToken(jwtToken);

            if (!emailOwnerLogged.isEmpty()) {
                serviceService.save(service);
                Company company = companyService.findByOwnerEmail(emailOwnerLogged);
                company.getServices().add(service.getId());
                companyService.save(company);
                return ResponseEntity.ok("Service was created");
            } else {
                return ResponseEntity.ok("Any company connected on application");
            }
        } else {
            return ResponseEntity.ok("Product was empty");
        }
    }

    @GetMapping("/services-list/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<?> findProductById(@PathVariable long id, HttpServletRequest req) {
        String jwtToken = jwtProvider.getJwt(req);
        String emailOwnerLogged = jwtProvider.getEmailFromJwtToken(jwtToken);

        if (emailOwnerLogged != null) {
            if (serviceService.existsById(id)) {
                return ResponseEntity.ok(serviceService.findById(id));
            } else {
                return new ResponseEntity<>("Doesn't exists any service with this id!",
                        HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Any company connected on application",
                    HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/edit-service/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<?> editProduct(@PathVariable long id, @Valid @RequestBody Service service, HttpServletRequest req) {
        if (service != null && serviceService.existsById(id)) {
            String jwtToken = jwtProvider.getJwt(req);
            String emailOwnerLogged = jwtProvider.getEmailFromJwtToken(jwtToken);

            if (!emailOwnerLogged.isEmpty()) {
                serviceService.save(service);
                return new ResponseEntity<>("Service was edited",
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Any company connected on application",
                        HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Product is empty",
                    HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/delete-service/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<String> deleteService(@PathVariable long id, HttpServletRequest req) {
        String jwtToken = jwtProvider.getJwt(req);
        String emailOwnerLogged = jwtProvider.getEmailFromJwtToken(jwtToken);

        if (emailOwnerLogged != null) {
            serviceService.removeById(id);
            return ResponseEntity.ok("Service was deleted successfully");
        } else {
            return ResponseEntity.ok("Owner not loggedin");
        }
    }
}
