package br.com.ipet.Services;

import br.com.ipet.Models.Company;
import br.com.ipet.Repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public void save(Company company) {
        companyRepository.save(company);
    }

    public void removeById(long id) {
        companyRepository.deleteById(id);
    }

    public Company findById(Long longID) {
        try {
            return companyRepository.findById(longID).get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public List<Company> findAll() { return companyRepository.findAll(); }

    public boolean existsByCnpj(String cnpj) { return companyRepository.existsByCnpj(cnpj); }
}
