package br.com.ipet.Services;

import br.com.ipet.Repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    public void save(br.com.ipet.Models.Service product) {
        serviceRepository.save(product);
    }

    public void remove(br.com.ipet.Models.Service product) { serviceRepository.delete(product); }

    public void removeById(long id) { serviceRepository.deleteById(id); }

    public br.com.ipet.Models.Service findById(long id) { return serviceRepository.findById(id); }

    public Boolean existsById(long id) { return serviceRepository.existsById(id); }

    public Page<br.com.ipet.Models.Service> findAllByPage(Pageable page) { return serviceRepository.findAll(page); }

    public br.com.ipet.Models.Service findServiceByName(String name) {
        return serviceRepository.findByName(name);
    }

    public Page<br.com.ipet.Models.Service> findAllServices(Set<Long> ids, Pageable pageable) { return serviceRepository.findProductsByIdIn(ids, pageable); }
}
