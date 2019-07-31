package br.com.ipet.Services;

import br.com.ipet.Models.Address;
import br.com.ipet.Repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
