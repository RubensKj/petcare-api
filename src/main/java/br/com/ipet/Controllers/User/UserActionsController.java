package br.com.ipet.Controllers.User;

import br.com.ipet.Helpers.UserHelper;
import br.com.ipet.Models.Company;
import br.com.ipet.Models.User;
import br.com.ipet.Payload.UserCompleteForm;
import br.com.ipet.Payload.UserDTO;
import br.com.ipet.Security.JWT.JwtProvider;
import br.com.ipet.Services.AddressService;
import br.com.ipet.Services.CompanyService;
import br.com.ipet.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
@RequestMapping("/api/users")
public class UserActionsController {

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private JwtProvider jwtProvider;

    @GetMapping("/profile-user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('OWNER')")
    public UserDTO profileUser(HttpServletRequest req) {
        return UserDTO.of(UserHelper.getUserLogged(req, userService, jwtProvider));
    }

    @PostMapping("/edit")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<User> editUser(@RequestBody UserCompleteForm userJSON, HttpServletRequest req) {
        User user = UserHelper.getUserLogged(req, userService, jwtProvider);
        User userValidated = UserHelper.updateValidation(user, userJSON);
        if (userJSON.getAddress() != null) {
            userValidated.setAddress(userJSON.getAddress());
            addressService.save(userValidated.getAddress());
        }
        userService.save(userValidated);
        return ResponseEntity.ok(userValidated);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> deleteUser(@PathVariable long id) {
        User user = userService.findById(id);
        userService.remove(user);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<Boolean> deleteUserLogged(HttpServletRequest req) {
        User user = UserHelper.getUserLogged(req, userService, jwtProvider);
        userService.remove(user);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/favorites-list/{page}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<Page<Company>> getFavoritesFromUserLogged(@PathVariable("page") int pageNumber, HttpServletRequest req) {
        User user = UserHelper.getUserLogged(req, userService, jwtProvider);
        Pageable pageable = PageRequest.of(pageNumber, 10);
        if (!user.getFavorites().isEmpty()) {
            return ResponseEntity.ok(companyService.findAllByIds(user.getFavorites(), pageable));
        } else {
            return ResponseEntity.ok(null);
        }
    }

    @PostMapping("/favorite/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<Company> addFavorite(HttpServletRequest req, @PathVariable Long id) {
        try {
            Company company = companyService.findById(id);
            User user = UserHelper.getUserLogged(req, userService, jwtProvider);

            if (user != null && company != null) {
                user.getFavorites().add(company.getId());
                company.getUserFavorites().add(user.getId());
                companyService.save(company);
                userService.save(user);
                return ResponseEntity.ok(company);
            } else {
                return ResponseEntity.ok(null);
            }
        } catch (Exception e) {
            return ResponseEntity.ok(null);
        }
    }

    @PostMapping("/removeFavorite/{id}")
    @Transactional
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<Company> removeFavorite(@PathVariable Long id, HttpServletRequest req) {
        try {
            Company company = companyService.findById(id);
            User user = UserHelper.getUserLogged(req, userService, jwtProvider);

            if (user != null && company != null) {
                user.getFavorites().remove(company.getId());
                company.getUserFavorites().remove(user.getId());
                companyService.save(company);
                userService.save(user);
                return ResponseEntity.ok(company);
            } else {
                return ResponseEntity.ok(null);
            }
        } catch (Exception e) {
            return ResponseEntity.ok(null);
        }
    }
}
