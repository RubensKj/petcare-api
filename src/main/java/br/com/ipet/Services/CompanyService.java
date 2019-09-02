package br.com.ipet.Services;

import br.com.ipet.Models.Company;
import br.com.ipet.Repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Set;

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

    public Iterable<Company> findAll(Pageable pageable) {
        return companyRepository.findAll(pageable);
    }

    public Iterable<Company> findByBetterRate() {
        return companyRepository.findAll(Sort.by(Sort.Direction.DESC, "rate"));
    }

    public boolean existsByEmail(String email) { return companyRepository.existsByEmail(email); }

    public Company findByOwnerEmail(String email) {
        return companyRepository.findByEmail(email);
    }

    public boolean existsByCnpj(String cnpj) {
        return companyRepository.existsByCnpj(cnpj);
    }

    public boolean existsById(Long id) {
        return companyRepository.existsById(id);
    }

    public Page<Company> findAllByIds(Set<Long> favorites, Pageable pageable) {
        return companyRepository.findByIdIn(favorites, pageable);
    }
}
