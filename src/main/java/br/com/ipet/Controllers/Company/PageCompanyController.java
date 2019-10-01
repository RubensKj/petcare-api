package br.com.ipet.Controllers.Company;

import br.com.ipet.Models.Company;
import br.com.ipet.Models.User;
import br.com.ipet.Security.JWT.JwtProvider;
import br.com.ipet.Services.CompanyService;
import br.com.ipet.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.25.17:3000", "http://192.168.0.73:3000", "https://aw-petcare-client.herokuapp.com", "https://aw-petcare-business.herokuapp.com", "http://aw-petcare-client.herokuapp.com", "http://aw-petcare-business.herokuapp.com"})
@RestController
@RequestMapping("/api/")
public class PageCompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    @GetMapping("/profile-company")
    public ResponseEntity<Company> getProfileCompanyLogged(HttpServletRequest req) {
        String tokenJWT = jwtProvider.getJwt(req);
        if (tokenJWT != null) {
            String emailOwner = jwtProvider.getEmailFromJwtToken(tokenJWT);
            return ResponseEntity.ok(companyService.findByOwnerEmail(emailOwner));
        } else {
            return ResponseEntity.ok(null);
        }
    }

    @GetMapping("/companies/{page}")
    public ResponseEntity<Iterable<Company>> getAllCompanies(@PathVariable int page) {
        Pageable pageable = PageRequest.of(page, 9);
        return ResponseEntity.ok(companyService.findAll(pageable));
    }

    @GetMapping("/companies-list/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.findById(id));
    }

    @GetMapping("/companies-searched/{page}/{search}")
    public Page<Company> getCompaniesByNameAndAddress(@PathVariable("page") int pageNumber, @PathVariable("search") String searchText, HttpServletRequest req) {
        String tokenJWT = jwtProvider.getJwt(req);
        Pageable pageable = PageRequest.of(pageNumber, 10);
        if (searchText != null && tokenJWT != null) {
            String emailUser = jwtProvider.getEmailFromJwtToken(tokenJWT);
            User user = userService.findByEmail(emailUser);
            if (user.getAddress() != null && user.getAddress().getState() != null && user.getAddress().getCity() != null) {
                Page<Company> byNameAndAddress = companyService.findByNameAndAddress(searchText, user.getAddress().getState(), user.getAddress().getCity(), pageable);
                if (!byNameAndAddress.getContent().isEmpty()) {
                    return byNameAndAddress;
                } else {
                    return companyService.findByName(searchText, pageable);
                }
            } else {
                return companyService.findByName(searchText, pageable);
            }
        } else {
            return companyService.findByName(searchText, pageable);
        }
    }

    @GetMapping("/companies-nearby/{page}")
    public Page<Company> getCompanyNearby(@PathVariable("page") int pageNumber, HttpServletRequest req) {
        String tokenJWT = jwtProvider.getJwt(req);
        Pageable pageable = PageRequest.of(pageNumber, 10);
        if (tokenJWT != null) {
            String emailUser = jwtProvider.getEmailFromJwtToken(tokenJWT);
            User user = userService.findByEmail(emailUser);
            if (user.getAddress() != null && user.getAddress().getState() != null && user.getAddress().getCity() != null && user.getAddress().getNeighborhood() != null) {
                Page<Company> byNameAndNearby = companyService.findByNameAndNear(user.getAddress().getState(), user.getAddress().getCity(), user.getAddress().getNeighborhood(), pageable);
                if (!byNameAndNearby.getContent().isEmpty()) {
                    return byNameAndNearby;
                } else {
                    return companyService.findNearByCity(user.getAddress().getState(), user.getAddress().getCity(), pageable);
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @GetMapping("/companies-most-rated/{page}")
    public Page<Company> getCompanyMostRated(@PathVariable("page") int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.DESC, "rate"));
        return companyService.findMostRateds(pageable);
    }
}
