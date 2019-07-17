package br.com.ipet.Repository;

import br.com.ipet.Models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
