package br.com.ipet.Services;

import br.com.ipet.Models.Owner;
import br.com.ipet.Repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {

    @Autowired
    private OwnerRepository ownerRepository;

    public void save(Owner owner) {
        ownerRepository.save(owner);
    }

    public void remove(Owner owner) {
        ownerRepository.delete(owner);
    }

    public Owner findByEmailOwner(String email) {
        return ownerRepository.findByEmail(email).get();
    }

    public boolean existsByEmail(String email) { return ownerRepository.existsByEmail(email); }

}
