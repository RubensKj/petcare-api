package br.com.ipet.Controllers.Address.AddressActions;

import br.com.ipet.Models.Address;
import br.com.ipet.Security.JWT.JwtProvider;
import br.com.ipet.Services.AddressService;
import br.com.ipet.Services.CompanyService;
import br.com.ipet.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = { "http://localhost:3000", "http://192.168.25.17:3000" })
@RestController
@RequestMapping("/api")
public class AddressActionsController {

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/companies/address")
    public ResponseEntity<Address> changeAddressCompany(@Valid @RequestBody Address address) {
        if(addressService.existsById(address.getId())) {
            addressService.save(address);
        }
        return ResponseEntity.ok(address);
    }

    @PostMapping("/users/address")
    public ResponseEntity<Address> changeAddressUser(@Valid @RequestBody Address address) {
        if(addressService.existsById(address.getId())) {
            addressService.save(address);
        }
        return ResponseEntity.ok(address);
    }
}
