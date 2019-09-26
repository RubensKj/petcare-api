package br.com.ipet.Controllers.Address;

import br.com.ipet.Models.Address;
import br.com.ipet.Services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.25.17:3000", "http://192.168.0.73:3000", "https://aw-petcare-client.herokuapp.com", "https://aw-petcare-business.herokuapp.com", "http://aw-petcare-client.herokuapp.com", "http://aw-petcare-business.herokuapp.com"})
@RestController
@RequestMapping("/api")
public class AddressActionsController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/address/edit")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Address> changeAddress(@Valid @RequestBody Address address) {
        if (addressService.existsById(address.getId())) {
            addressService.save(address);
        }
        return ResponseEntity.ok(address);
    }

}
