package br.com.ipet.Controllers.Company;

import br.com.ipet.Payload.UserOrOwnerForm;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static br.com.ipet.Helpers.AuthMethods.logoutMethod;

@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.25.17:3000", "http://192.168.0.73:3000", "https://aw-petcare-client.herokuapp.com/", "https://aw-petcare-business.herokuapp.com/"})
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
    private JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateOwner(@Valid @RequestBody UserOrOwnerForm loginRequest) {
        if (companyService.existsByEmail(loginRequest.getEmail())) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtProvider.generateJwtToken(authentication);
            return ResponseEntity.ok(new JwtResponse(jwt));
        } else {
            return new ResponseEntity<>("Não existe nenhuma empresa com este email!",
                    HttpStatus.NOT_FOUND);
        }
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
}
