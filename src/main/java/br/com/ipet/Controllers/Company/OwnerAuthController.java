package br.com.ipet.Controllers.Company;

import br.com.ipet.Models.Role;
import br.com.ipet.Models.RoleName;
import br.com.ipet.Models.User;
import br.com.ipet.Payload.UserCompleteForm;
import br.com.ipet.Payload.UserOrOwnerForm;
import br.com.ipet.Repository.RoleRepository;
import br.com.ipet.Security.JWT.JwtProvider;
import br.com.ipet.Security.JWT.JwtResponse;
import br.com.ipet.Services.CompanyService;
import br.com.ipet.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

import static br.com.ipet.Helpers.AuthMethods.logoutMethod;

@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.25.17:3000"})
@RestController
@RequestMapping("/api/company-auth")
public class OwnerAuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody UserOrOwnerForm loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/logout")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<?> unauthenticateOwner(HttpServletRequest req) {
        return logoutMethod(req);
    }

    @PostMapping("/validate-owner-email")
    public ResponseEntity<String> validateEmail(@RequestBody String email) {
        if (userService.existsByEmail(email)) {
            return new ResponseEntity<>("Email já está sendo usado!",
                    HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @PostMapping("/validate-cnpj")
    public ResponseEntity<String> validateCnpj(@RequestBody String cnpj) {
        if (companyService.existsByCnpj(cnpj)) {
            return new ResponseEntity<>("Já existe uma empresa com o mesmo CNPJ",
                    HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserCompleteForm signUpRequest) {
        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>("Email já está sendo usado, ou você já é um usuário, ou seja, basta logar e clicar no botão 'Criar Empresa'!",
                    HttpStatus.BAD_REQUEST);
        }

        // Creating owner's account
        User owner = new User(signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getCompleteName(), signUpRequest.getCpf(), signUpRequest.getDdd(), signUpRequest.getPhoneNumber(), signUpRequest.getAvatar());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        try {
            strRoles.forEach(role -> {
                switch (role) {
                    case "user":
                        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("ROLE_USER não foi encontrada. (" + RoleName.ROLE_USER + ")"));
                        roles.add(userRole);
                        break;
                    case "owner":
                        Role ownerRole = roleRepository.findByName(RoleName.ROLE_OWNER)
                                .orElseThrow(() -> new RuntimeException("OWNER_ROLE não foi encontrada. (" + RoleName.ROLE_OWNER + ")"));
                        roles.add(ownerRole);
                        break;
                    default:
                        Role userOwner = roleRepository.findByName(RoleName.ROLE_OWNER)
                                .orElseThrow(() -> new RuntimeException("ROLE_USER_OWNER não foi encontrada. (" + RoleName.ROLE_OWNER + ")"));
                        roles.add(userOwner);
                }
            });
        } catch (NullPointerException e) {
            Role userOwner = roleRepository.findByName(RoleName.ROLE_OWNER)
                    .orElseThrow(() -> new RuntimeException("ROLE_USER_OWNER não foi encontrada. (" + RoleName.ROLE_OWNER + ")"));
            roles.add(userOwner);
        }
        owner.setRoles(roles);

        userService.save(owner);

        return ResponseEntity.ok().body("Dono foi registrado com sucesso!");
    }
}
