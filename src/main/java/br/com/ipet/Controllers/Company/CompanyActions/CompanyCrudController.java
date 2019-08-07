package br.com.ipet.Controllers.Company.CompanyActions;

import br.com.ipet.Models.*;
import br.com.ipet.Payload.CompanySignUpForm;
import br.com.ipet.Repository.RoleRepository;
import br.com.ipet.Security.JWT.JwtProvider;
import br.com.ipet.Services.AddressService;
import br.com.ipet.Services.CompanyService;
import br.com.ipet.Services.OwnerService;
import br.com.ipet.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.25.17:3000"})
@RestController
@RequestMapping("/api")
public class CompanyCrudController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/signup-petshop")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<String> save(@Valid @RequestBody CompanySignUpForm companyForm, HttpServletRequest req) {
        if (companyService.existsByCnpj(companyForm.getCnpj())) {
            return new ResponseEntity<>("Já consta no sistema uma empresa com esse CNPJ!",
                    HttpStatus.BAD_REQUEST);
        }

        String jwtToken = jwtProvider.getJwt(req);
        String emailFromJwtToken = jwtProvider.getEmailFromJwtToken(jwtToken);

        if(ownerService.existsByEmail(emailFromJwtToken)) {
            return ResponseEntity.ok("Já possui um dono com este email.");
        }

        User user = userService.findByEmail(emailFromJwtToken);
        Role ownerRole = roleRepository.findByName(RoleName.ROLE_OWNER)
                .orElseThrow(() -> new RuntimeException("OWNER_ROLE não foi encontrada. (" + RoleName.ROLE_OWNER + ")"));
        user.getRoles().add(ownerRole);

        Owner owner = new Owner(user.getEmail());

        Company company = new Company(owner, companyForm.getCnpj(), companyForm.getCompanyName(), companyForm.getDescription(), companyForm.getStatus(), companyForm.getAvatar(), companyForm.getRate());

        Set<Address> addresses = companyForm.getAddresses();
        Set<Address> addressesCompany = new HashSet<>();

        addresses.forEach(address -> {
            addressService.save(address);
            addressesCompany.add(address);
        });
        company.setAddresses(addressesCompany);

        ownerService.save(owner);
        userService.save(user);
        companyService.save(company);
        return ResponseEntity.ok("Cadastro feito com sucesso!");
    }

    @PostMapping("/remove-petshop/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<String> removeById(@PathVariable long id, HttpServletRequest req) {
        try {
            Company company = companyService.findById(id);
            String jwtToken = jwtProvider.getJwt(req);
            String emailFromJwtToken = jwtProvider.getEmailFromJwtToken(jwtToken);
            if (company.getOwner().getEmail().equals(emailFromJwtToken)) {
                companyService.removeById(id);
                return ResponseEntity.ok("Empresa deletada com sucesso!");
            } else {
                return ResponseEntity.ok("Apenas o dono pode deletar a empresa.");
            }
        } catch (NullPointerException | NoSuchElementException e) {
            return ResponseEntity.ok("Não existe nenhuma empresa com esse id.");
        }
    }
}
