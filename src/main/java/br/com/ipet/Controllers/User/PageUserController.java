package br.com.ipet.Controllers.User;

import br.com.ipet.Helpers.UserHelper;
import br.com.ipet.Models.Company;
import br.com.ipet.Models.User;
import br.com.ipet.Security.JWT.JwtProvider;
import br.com.ipet.Services.CompanyService;
import br.com.ipet.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = {
        "http://localhost:3000",
        "https://aw-petcare-client.herokuapp.com",
        "https://aw-petcare-business.herokuapp.com",
        "http://aw-petcare-client.herokuapp.com",
        "http://aw-petcare-business.herokuapp.com",
        "https://petcare-client.vercel.app/",
        "https://petcare-client.vercel.app",
        "https://petcare-business.vercel.app"
})
@RestController
@RequestMapping("/api/")
public class PageUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private JwtProvider jwtProvider;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<User> getUserInformationToProfile(HttpServletRequest req) {
        String jwtToken = jwtProvider.getJwt(req);
        String usernameUserLogged = jwtProvider.getEmailFromJwtToken(jwtToken);
        User user = userService.findByEmail(usernameUserLogged);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/user/favorites")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<List<Company>> getFavoritesFromUser(HttpServletRequest req) {
        User user = UserHelper.getUserLogged(req, userService, jwtProvider);
        if (user != null) {
            List<Company> companies = new ArrayList<>();
            user.getFavorites().forEach(id -> {
                Company company = companyService.findById(id);
                companies.add(company);
            });
            return ResponseEntity.ok(companies);
        }
        return null;
    }

    @GetMapping("/api/test/admin")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public String adminAccess() {
        return ">>> Admin Contents";
    }
}
