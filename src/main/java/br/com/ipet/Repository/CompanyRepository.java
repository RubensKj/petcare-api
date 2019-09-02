package br.com.ipet.Repository;

import br.com.ipet.Models.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Set;

public interface CompanyRepository extends PagingAndSortingRepository<Company, Long> {
    Boolean existsByCnpj(String cnpj);
    Boolean existsByEmail(String email);
    Company findByEmail(String email);
    void deleteByCnpj(String cnpj);
    Page<Company> findByIdIn(Set<Long> ids, Pageable pageable);
}
