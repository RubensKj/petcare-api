package br.com.ipet.Controllers.Company;

import br.com.ipet.Models.Company;
import br.com.ipet.Security.JWT.JwtProvider;
import br.com.ipet.Services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = { "http://localhost:3000", "http://192.168.25.17:3000" })
@RestController
@RequestMapping("/api/")
public class PageCompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private JwtProvider jwtProvider;

    @GetMapping("/profile-company")
    public ResponseEntity<Company> getProfileCompanyLogged(HttpServletRequest req) {
        String tokenJWT = jwtProvider.getJwt(req);
        if(tokenJWT != null) {
            String emailOwner = jwtProvider.getEmailFromJwtToken(tokenJWT);
            return ResponseEntity.ok(companyService.findByOwnerEmail(emailOwner));
        } else {
            return ResponseEntity.ok(null);
        }
    }

    @GetMapping("/companies")
    public ResponseEntity<Iterable<Company>> getAllCompanies() {
        Pageable pageable = PageRequest.of(0, 9);
        return ResponseEntity.ok(companyService.findAll(pageable));
    }

//    @GetMapping("/companies-rate")
//    public ResponseEntity<List<Company>> getCompaniesWithBetterRate() {
//        return ResponseEntity.ok(companyService.findByBetterRate());
//    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.findById(id));
    }
}
