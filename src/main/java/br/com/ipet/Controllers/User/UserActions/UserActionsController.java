package br.com.ipet.Controllers.User.UserActions;

import br.com.ipet.Models.Company;
import br.com.ipet.Models.User;
import br.com.ipet.Security.JWT.JwtProvider;
import br.com.ipet.Services.CompanyService;
import br.com.ipet.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class UserActionsController {

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/companies/favorite/{id}")
    @Transactional
    public ResponseEntity<Company> addFavorite(@PathVariable Long id, HttpServletRequest req) {
        String jwtToken = jwtProvider.getJwt(req);
        if (jwtToken != null) {
            // find user by token of user logged on application
            String emailUserLogged = jwtProvider.getUserNameFromJwtToken(jwtToken);

            // find the company by id
            Company company = companyService.findById(id);
            User user = userService.findByEmail(emailUserLogged);

            try {
                user.getFavorites().add(company.getId());
            } catch (Exception e) {
                return ResponseEntity.ok(company);
            }

            return ResponseEntity.ok(company);
        } else {
            return ResponseEntity.ok(null);
        }
    }

    @PostMapping("/companies/removeFavorite/{id}")
    @Transactional
    public ResponseEntity<Company> removeFavorite(@PathVariable Long id, HttpServletRequest req) {
        String jwtToken = jwtProvider.getJwt(req);
        if (jwtToken != null) {
            // find user by token of user logged on application
            String emailUserLogged = jwtProvider.getUserNameFromJwtToken(jwtToken);

            // find the company by id
            Company company = companyService.findById(id);
            User user = userService.findByEmail(emailUserLogged);

            try {
                user.getFavorites().remove(company.getId());
            } catch (Exception e) {
                return ResponseEntity.ok(company);
            }

            return ResponseEntity.ok(company);
        } else {
            return ResponseEntity.ok(null);
        }
    }
}
