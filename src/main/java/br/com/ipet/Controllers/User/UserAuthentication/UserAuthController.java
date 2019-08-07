package br.com.ipet.Controllers.User.UserAuthentication;

import br.com.ipet.Models.Address;
import br.com.ipet.Models.Role;
import br.com.ipet.Models.RoleName;
import br.com.ipet.Models.User;
import br.com.ipet.Payload.UserCompleteForm;
import br.com.ipet.Payload.UserOrOwnerForm;
import br.com.ipet.Repository.AddressRepository;
import br.com.ipet.Repository.RoleRepository;
import br.com.ipet.Security.JWT.JwtProvider;
import br.com.ipet.Security.JWT.JwtResponse;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static br.com.ipet.Helpers.AuthMethods.logoutMethod;

@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.25.17:3000"})
@RestController
@RequestMapping("/api/auth")
public class UserAuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AddressRepository addressRepository;

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
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> unauthenticateUser(HttpServletRequest req) {
        return logoutMethod(req);
    }

    @PostMapping("/validate-user")
    public ResponseEntity<String> validateUser(@Valid @RequestBody UserCompleteForm user) {
        if (userService.existsByEmail(user.getEmail())) {
            return new ResponseEntity<>("Email já está sendo usado!",
                    HttpStatus.BAD_REQUEST);
        }
        return null;
    }


    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserCompleteForm signUpRequest) {
        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>("Email já está sendo usado!",
                    HttpStatus.BAD_REQUEST);
        }

        if (userService.existsByCpf(signUpRequest.getCpf())) {
            return new ResponseEntity<>("CPF já está sendo usado!",
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getCompleteName(), signUpRequest.getCpf(), signUpRequest.getDdd(), signUpRequest.getPhoneNumber(), signUpRequest.getAvatar());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        try {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("ADMIN_ROLE não foi encontrada. (" + RoleName.ROLE_ADMIN + ")"));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("USER_ROLE não foi encontrada. (" + RoleName.ROLE_USER + ")"));
                        roles.add(userRole);
                }
            });
        } catch (NullPointerException e) {
            Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("USER_ROLE não foi encontrada. (" + RoleName.ROLE_USER + ")"));
            roles.add(userRole);
        }
        user.setRoles(roles);

        List<Address> addresses = signUpRequest.getAddress();
        List<Address> addressUser = new ArrayList<>();

        addresses.forEach(address -> {
            addressRepository.save(address);
            addressUser.add(address);
        });
        user.setAddress(addressUser);

        userService.save(user);

        return ResponseEntity.ok().body("Usuário foi registrado com sucesso!");
    }
}
