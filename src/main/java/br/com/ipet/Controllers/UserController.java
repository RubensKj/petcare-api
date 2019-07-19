package br.com.ipet.Controllers;

import br.com.ipet.Models.User;
import br.com.ipet.Repository.UserRepository;
import br.com.ipet.Security.JWT.JwtProvider;
import br.com.ipet.Services.UserAuthPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/finishSignUp")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<User> editUser(@Valid @RequestBody User user, Authentication authentication) {
        UserAuthPrincipal userAuth = (UserAuthPrincipal) authentication.getPrincipal();
        User userLogged = userRepository.findByUsername(userAuth.getUsername()).get();

        userLogged.setFirstName(user.getFirstName());
        userLogged.setLastName(user.getLastName());
        userLogged.setCpf(user.getCpf());

        userRepository.save(userLogged);
        return ResponseEntity.ok(userLogged);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<User> getUserInformationToProfile(HttpServletRequest req) {
        String jwtToken = jwtProvider.getJwt(req);
        String usernameUserLogged = jwtProvider.getUserNameFromJwtToken(jwtToken);
        Optional<User> optionalUser = userRepository.findByUsername(usernameUserLogged);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            return ResponseEntity.ok(user);
        } else {
            return null;
        }
    }


    @GetMapping("/users")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userRepository.findById(id).get());
    }

    @GetMapping("/api/test/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return ">>> Admin Contents";
    }
}
