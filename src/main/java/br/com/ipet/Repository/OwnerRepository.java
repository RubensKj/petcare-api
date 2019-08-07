package br.com.ipet.Repository;

import br.com.ipet.Models.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByEmail(String email);
    Boolean existsByEmail(String email);
}
