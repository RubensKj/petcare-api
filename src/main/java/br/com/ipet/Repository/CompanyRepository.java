package br.com.ipet.Repository;

import br.com.ipet.Models.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Boolean existsByCnpj(String cnpj);
    Boolean existsByEmail(String email);
    Company findByEmail(String email);
    void deleteByCnpj(String cnpj);
}
