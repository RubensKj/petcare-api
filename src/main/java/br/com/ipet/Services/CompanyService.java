package br.com.ipet.Services;

import br.com.ipet.Models.Company;
import br.com.ipet.Repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return companyRepository.findById(longID).get();
    }

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public List<Company> findByBetterRate() {
        return companyRepository.findAll(Sort.by(Sort.Direction.DESC, "rate"));
    }

    public Company findByUserEmail(String email) {
        return companyRepository.findByEmail(email);
    }

    public boolean existsByCnpj(String cnpj) {
        return companyRepository.existsByCnpj(cnpj);
    }
}
