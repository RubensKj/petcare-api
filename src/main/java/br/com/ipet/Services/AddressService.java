package br.com.ipet.Services;

import br.com.ipet.Models.Address;
import br.com.ipet.Repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public void save(Address address) {
        addressRepository.save(address);
    }

    public void remove(Address address) {
        addressRepository.delete(address);
    }

    public Address findById(int id) {
        Optional<Address> addressOptional = addressRepository.findById(id);
        return addressOptional.orElse(null);
    }

    public boolean existsById(int id) { return addressRepository.existsById(id); }
}
