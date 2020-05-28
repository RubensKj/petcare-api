package br.com.ipet.Controllers.Company;

import br.com.ipet.Controllers.File.FileController;
import br.com.ipet.Models.*;
import br.com.ipet.Payload.CompanyEditForm;
import br.com.ipet.Payload.CompanySignUpForm;
import br.com.ipet.Repository.RoleRepository;
import br.com.ipet.Security.JWT.JwtProvider;
import br.com.ipet.Services.AddressService;
import br.com.ipet.Services.CompanyService;
import br.com.ipet.Services.FileStorageService;
import br.com.ipet.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Supplier;

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
public class CompanyCrudController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AddressService addressService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private JwtProvider jwtProvider;


    @PostMapping("/signup-petshop")
    public ResponseEntity<String> save(@Valid @RequestBody CompanySignUpForm companyForm) {
        if (companyForm == null) {
            return new ResponseEntity<>("JSON está vázio",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (companyService.existsByCnpj(companyForm.getCnpj())) {
            return new ResponseEntity<>("Já consta no sistema uma empresa com esse CNPJ!",
                    HttpStatus.FORBIDDEN);
        }

        if (userService.existsByEmail(companyForm.getEmail())) {
            return new ResponseEntity<>("Já possui alguém com este email no sistema!",
                    HttpStatus.FORBIDDEN);
        }

        if (userService.existsByCpf(companyForm.getCpf())) {
            return new ResponseEntity<>("Já possui alguém com este cpf no sistema!",
                    HttpStatus.FORBIDDEN);
        }

        System.out.println(companyForm.getEmail());
        System.out.println(companyForm.getCompleteName());

        User user = new User(companyForm.getEmail(), encoder.encode(companyForm.getPassword()), companyForm.getCompleteName(), companyForm.getCpf(), companyForm.getPhoneNumber(), "");
        Company company = new Company(companyForm.getCnpj(), companyForm.getEmail(), companyForm.getCompanyName(), companyForm.getDescription(), "Fechado", "", 5.0);

        Address address = companyForm.getAddress();
        address.setState(address.getState().toUpperCase());
        addressService.save(address);
        company.setAddress(address);

        Set<String> strRoles = companyForm.getRole();
        Set<Role> roles = new HashSet<>();

        Supplier<RuntimeException> runtimeExceptionSupplier = () -> new RuntimeException("OWNER_ROLE não foi encontrada. (" + RoleName.ROLE_OWNER + ")");
        try {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("ADMIN_ROLE não foi encontrada. (" + RoleName.ROLE_ADMIN + ")"));
                        roles.add(adminRole);
                        break;
                    case "Owner":
                        Role ownerRole = roleRepository.findByName(RoleName.ROLE_OWNER)
                                .orElseThrow(runtimeExceptionSupplier);
                        roles.add(ownerRole);
                    default:
                        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("USER_ROLE não foi encontrada. (" + RoleName.ROLE_USER + ")"));
                        roles.add(userRole);
                        Role ownerRoleDefault = roleRepository.findByName(RoleName.ROLE_OWNER)
                                .orElseThrow(runtimeExceptionSupplier);
                        roles.add(ownerRoleDefault);
                }
            });
        } catch (NullPointerException e) {
            Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("USER_ROLE não foi encontrada. (" + RoleName.ROLE_USER + ")"));
            roles.add(userRole);
            Role ownerRoleDefault = roleRepository.findByName(RoleName.ROLE_OWNER)
                    .orElseThrow(runtimeExceptionSupplier);
            roles.add(ownerRoleDefault);
        }
        user.setRoles(roles);

        userService.save(user);
        companyService.save(company);
        return ResponseEntity.ok("Cadastro feito com sucesso!");
    }

    @PostMapping("/change-company-image/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<String> editImage(@PathVariable long id, MultipartFile file, HttpServletRequest req) {
        try {
            Company company = companyService.findById(id);
            String jwtToken = jwtProvider.getJwt(req);
            String emailFromJwtToken = jwtProvider.getEmailFromJwtToken(jwtToken);
            if (company.getEmail().equals(emailFromJwtToken)) {
                if (file != null && (file.getOriginalFilename().contains("jpeg") || file.getOriginalFilename().contains("png") || file.getOriginalFilename().contains("jpg"))) {
                    String removeContext = "/api/change-company-image";
                    String urlImage = FileController.saveImage(file, req, removeContext, fileStorageService);
                    company.setAvatar(urlImage);
                    companyService.save(company);
                }
                return ResponseEntity.ok("Avatar da empresa alterado com sucesso!");
            } else {
                return ResponseEntity.ok("Apenas o dono pode alterar o avatar da empresa.");
            }
        } catch (NullPointerException | NoSuchElementException e) {
            return ResponseEntity.ok("Não existe nenhuma empresa com esse id.");
        }
    }

    @PostMapping("/edit-company/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<String> editCompanyInformation(@PathVariable long id, @Valid @RequestBody CompanyEditForm companyEditForm, HttpServletRequest req) {
        if (companyEditForm != null && companyService.existsById(id)) {
            String jwtToken = jwtProvider.getJwt(req);
            String emailOwnerLogged = jwtProvider.getEmailFromJwtToken(jwtToken);
            Company company = companyService.findById(id);

            if (!emailOwnerLogged.isEmpty()) {
                if (companyEditForm.getCnpj() != null) {
                    company.setCnpj(companyEditForm.getCnpj());
                }

                if (companyEditForm.getCompanyName() != null) {
                    company.setCompanyName(companyEditForm.getCompanyName());
                }

                if (companyEditForm.getDescription() != null && companyEditForm.getDescription().length() > 0) {
                    company.setDescription(companyEditForm.getDescription());
                }

                if (companyEditForm.getAddress() != null) {
                    addressService.save(companyEditForm.getAddress());
                    company.setAddress(companyEditForm.getAddress());
                }
                companyService.save(company);
                return new ResponseEntity<>("Company was edited",
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Any company connected on application",
                        HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Company is empty",
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/remove-petshop/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<String> removeById(@PathVariable long id, HttpServletRequest req) {
        try {
            Company company = companyService.findById(id);
            String jwtToken = jwtProvider.getJwt(req);
            String emailFromJwtToken = jwtProvider.getEmailFromJwtToken(jwtToken);
            if (company.getEmail().equals(emailFromJwtToken)) {
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
