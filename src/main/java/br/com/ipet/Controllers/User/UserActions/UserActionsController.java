package br.com.ipet.Controllers.User.UserActions;

import br.com.ipet.Helpers.UserHelper;
import br.com.ipet.Models.Company;
import br.com.ipet.Models.User;
import br.com.ipet.Payload.UserCompleteForm;
import br.com.ipet.Security.JWT.JwtProvider;
import br.com.ipet.Services.CompanyService;
import br.com.ipet.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.25.17:3000"})
@RestController
@RequestMapping("/api/users")
public class UserActionsController {

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/edit")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<User> editUser(@RequestBody UserCompleteForm userJSON, HttpServletRequest req) {
        User user = UserHelper.getUserLogged(req, userService, jwtProvider);
        User userValidated = UserHelper.updateValidation(user, userJSON);
        userService.save(userValidated);
        return ResponseEntity.ok(userValidated);
    }

    @PostMapping("/favorite/{id}")
    @Transactional
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Company> addFavorite(@PathVariable Long id, HttpServletRequest req) {
        Company company = companyService.findById(id);
        User user = UserHelper.getUserLogged(req, userService, jwtProvider);

        try {
            if (user != null) {
                user.getFavorites().add(company.getId());
                company.getUserFavorites().put(user.getEmail(), true);
            }
        } catch (Exception e) {
            return ResponseEntity.ok(company);
        }

        return ResponseEntity.ok(company);
    }

    @PostMapping("/removeFavorite/{id}")
    @Transactional
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Company> removeFavorite(@PathVariable Long id, HttpServletRequest req) {
        Company company = companyService.findById(id);
        User user = UserHelper.getUserLogged(req, userService, jwtProvider);

        try {
            if (user != null) {
                user.getFavorites().remove(company.getId());
                company.getUserFavorites().remove(user.getEmail());
            }
        } catch (Exception e) {
            return ResponseEntity.ok(company);
        }

        return ResponseEntity.ok(company);
    }
}
