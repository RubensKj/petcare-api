package br.com.ipet.Controllers.Company;

import br.com.ipet.Models.Company;
import br.com.ipet.Security.JWT.JwtProvider;
import br.com.ipet.Services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = { "http://localhost:3000", "http://192.168.25.17:3000", "http://192.168.0.73:3000" })
@RestController
@RequestMapping("/api")
public class CompanyActionsController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private JwtProvider jwtProvider;

    @PutMapping("/change-company-status")
    public ResponseEntity<String> getProfileCompanyLogged(HttpServletRequest req) {
        String tokenJWT = jwtProvider.getJwt(req);
        if (tokenJWT != null) {
            String emailOwner = jwtProvider.getEmailFromJwtToken(tokenJWT);
            Company company = companyService.findByOwnerEmail(emailOwner);
            if(company.getStatus().equalsIgnoreCase("Aberto")) {
                company.setStatus("Fechado");
            } else {
                company.setStatus("Aberto");
            }

            companyService.save(company);
            return ResponseEntity.ok("Status was changed successfully");
        } else {
            return ResponseEntity.ok("Something went wrong during the change of company's status");
        }
    }
}
