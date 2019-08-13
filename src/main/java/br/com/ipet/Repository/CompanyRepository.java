package br.com.ipet.Repository;

import br.com.ipet.Models.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Boolean existsByCnpj(String cnpj);
    Company findByUserEmail(String userEmail);
    void deleteByCnpj(String cnpj);
}
