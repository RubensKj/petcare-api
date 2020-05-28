package br.com.ipet.Controllers.Product;

import br.com.ipet.Models.Product;
import br.com.ipet.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {
        "http://localhost:3000",
        "https://aw-petcare-client.herokuapp.com",
        "https://aw-petcare-business.herokuapp.com",
        "http://aw-petcare-client.herokuapp.com",
        "http://aw-petcare-business.herokuapp.com",
        "https://petcare-client.now.sh"
})
@RestController
@RequestMapping("/api")
public class ProductActionsController {

    @Autowired
    private ProductService productService;

    @GetMapping("/validate-ifCanAddOnCart/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<Boolean> checkIfCompanyIsOpen(@PathVariable("id") Long id) {
        // This id is from product when is going to add on cart
        if (id != null) {
            Product product = productService.findById(id);
            if ((product.getQuantityStore() - 1) >= 0) {
                return ResponseEntity.ok(true);
            } else {
                return ResponseEntity.ok(false);
            }
        } else {
            return ResponseEntity.ok(false);
        }
    }
}
