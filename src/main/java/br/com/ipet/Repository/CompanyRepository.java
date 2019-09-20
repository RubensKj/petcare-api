package br.com.ipet.Repository;

import br.com.ipet.Models.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Set;

public interface CompanyRepository extends PagingAndSortingRepository<Company, Long> {
    Boolean existsByCnpj(String cnpj);
    Boolean existsByEmail(String email);
    Company findByEmail(String email);
    void deleteByCnpj(String cnpj);
    Page<Company> findByIdIn(Set<Long> ids, Pageable pageable);
    Company findByCnpj(String cnpj);

    Page<Company> findByCompanyNameContainingIgnoreCaseAndAddress_StateIgnoreCaseAndAddress_CityIgnoreCase(String companyName, String state, String city, Pageable pageable);

    Page<Company> findByCompanyNameContainingIgnoreCase(String companyName, Pageable pageable);

    Page<Company> findByAddress_StateIgnoreCaseAndAddress_CityIgnoreCaseAndAddress_NeighborhoodIgnoreCase(String state, String city, String neighborhood, Pageable pageable);

    Page<Company> findByRateIsLessThanEqual(double rate, Pageable pageable, Sort sort);
}
