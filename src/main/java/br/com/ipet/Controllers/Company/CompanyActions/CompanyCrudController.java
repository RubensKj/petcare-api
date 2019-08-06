package br.com.ipet.Controllers.Company.CompanyActions;

import br.com.ipet.Models.Address;
import br.com.ipet.Models.Company;
import br.com.ipet.Payload.CompanySignUpForm;
import br.com.ipet.Services.AddressService;
import br.com.ipet.Services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = { "http://localhost:3000", "http://192.168.25.17:3000" })
@RestController
@RequestMapping("/api")
public class CompanyCrudController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private AddressService addressService;

    @PostMapping("/signup-petshop")
    public ResponseEntity<String> save(@Valid @RequestBody CompanySignUpForm companyForm) {
        if (companyService.existsByCnpj(companyForm.getCnpj())) {
            return new ResponseEntity<>("Já consta no sistema uma empresa com esse CNPJ!",
                    HttpStatus.BAD_REQUEST);
        }

        Company company = new Company(companyForm.getCnpj(), companyForm.getCompanyName(), companyForm.getDescription(), companyForm.getStatus(), companyForm.getAvatar(), companyForm.getRate());

        Set<Address> addresses = companyForm.getAddresses();
        Set<Address> addressesCompany = new HashSet<>();

        addresses.forEach(address -> {
            addressService.save(address);
            addressesCompany.add(address);
        });
        company.setAddresses(addressesCompany);

        companyService.save(company);
        return ResponseEntity.ok("Cadastro feito com sucesso!");
    }

    @PostMapping("/remove-petshop/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> removeById(@PathVariable long id) {
        companyService.removeById(id);
        return ResponseEntity.ok("Empresa deletada com sucesso!");
    }
}
