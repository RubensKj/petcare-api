package br.com.ipet.Controllers.Product;

import br.com.ipet.Models.Company;
import br.com.ipet.Models.Product;
import br.com.ipet.Security.JWT.JwtProvider;
import br.com.ipet.Services.CompanyService;
import br.com.ipet.Services.FileStorageService;
import br.com.ipet.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.25.17:3000"})
@RestController
@RequestMapping("/api")
public class ProductCrudController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/products/{page}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<Page<Product>> listProducts(@PathVariable("page") int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 10);
        return ResponseEntity.ok(productService.findAllByPage(pageable));
    }

    @PostMapping("/create-product")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<String> saveProduct(@RequestParam("file") MultipartFile file, Product product, HttpServletRequest req) {
        if(product != null) {
            String jwtToken = jwtProvider.getJwt(req);
            String emailOwnerLogged = jwtProvider.getEmailFromJwtToken(jwtToken);

            if(file != null && (file.getOriginalFilename().contains("jpeg") || file.getOriginalFilename().contains("png") || file.getOriginalFilename().contains("jpg"))) {
                String fileName = fileStorageService.storeFile(file);
                Resource resource = fileStorageService.loadFileAsResource(fileName);
                URI contextUrl = URI.create(req.getRequestURL().toString()).resolve(req.getContextPath());
                String urlImage = contextUrl + "images/" + resource.getFilename();
                if(urlImage.contains("/api")) {
                    urlImage = urlImage.replace("/api", "");
                }
                product.setAvatar(urlImage);
            }

            if(!emailOwnerLogged.isEmpty()) {
                productService.save(product);
                Company company = companyService.findByOwnerEmail(emailOwnerLogged);
                company.getProducts().add(product.getId());
                companyService.save(company);
                return ResponseEntity.ok("Product was created");
            } else {
                return ResponseEntity.ok("Any company connected on application");
            }
        } else {
            return ResponseEntity.ok("Product was empty");
        }
    }
}
