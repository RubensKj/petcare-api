package br.com.ipet.Controllers;

import br.com.ipet.Models.Address;
import br.com.ipet.Models.Role;
import br.com.ipet.Models.RoleName;
import br.com.ipet.Models.User;
import br.com.ipet.Payload.UserForm;
import br.com.ipet.Payload.UserRegisterForm;
import br.com.ipet.Repository.AddressRepository;
import br.com.ipet.Repository.RoleRepository;
import br.com.ipet.Repository.UserRepository;
import br.com.ipet.Security.JWT.JwtProvider;
import br.com.ipet.Security.JWT.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class AuthRestAPI {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody UserForm loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> UnauthenticateUser(HttpServletRequest req) {
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        try {
            req.logout();
        } catch (ServletException e) {
            return ResponseEntity.ok("false");
        }
        return ResponseEntity.ok("true");
    }


    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegisterForm signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>("Fail -> Username is already taken!",
                    HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>("Fail -> Email is already in use!",
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()), signUpRequest.getEmail(),
                signUpRequest.getFirstName(), signUpRequest.getLastName(), signUpRequest.getCpf(), signUpRequest.getDdd(), signUpRequest.getPhoneNumber(), signUpRequest.getAddress());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        try {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                        roles.add(userRole);
                }
            });
        } catch (NullPointerException e) {
            Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
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

        userRepository.save(user);

        return ResponseEntity.ok().body("User registered successfully!");
    }
}
