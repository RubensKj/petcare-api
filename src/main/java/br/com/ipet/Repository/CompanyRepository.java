package br.com.ipet.Repository;

import br.com.ipet.Models.Company;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CompanyRepository extends PagingAndSortingRepository<Company, Long> {
    Boolean existsByCnpj(String cnpj);
    Boolean existsByEmail(String email);
    Company findByEmail(String email);
    void deleteByCnpj(String cnpj);
}
