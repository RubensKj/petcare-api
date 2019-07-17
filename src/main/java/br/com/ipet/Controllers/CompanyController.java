package br.com.ipet.Controllers;

import br.com.ipet.Models.Company;
import br.com.ipet.Repository.CompanyRepository;
import br.com.ipet.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class CompanyController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(companyRepository.findAll());
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<Company> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(companyRepository.findById(id).get());
    }

}
