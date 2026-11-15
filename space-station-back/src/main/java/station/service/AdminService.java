package station.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import station.model.Admin;
import station.repo.AdminRepository;

@Service
@Transactional
public class AdminService {
    private static final Logger log = LoggerFactory.getLogger(AdminService.class);
    private final AdminRepository repository;

    public AdminService(AdminRepository repository) {
        this.repository = repository;
    }

    public List<Admin> findAll() {
        log.debug("Liste des admins ...");

        return this.repository.findAll();
    }

    public Admin findById(Integer id) {
        log.debug("Recherche du admin {} ...", id);

        return this.repository.findById(id).orElseThrow(AdminNotFoundException::new);
    }

    public Admin save(CreateOrUpdateAdminRequest request) {
        log.debug("Création d'un admin ...");

        Admin admin = this.save(new Admin(), request);

        log.debug("Admin {} ajouté !", admin.getId());

        return admin;
    }

    public Admin save(Integer id, CreateOrUpdateAdminRequest request) {
        log.debug("Modification du admin {} ...", id);

        Admin admin = this.findById(id);

        admin = this.save(admin, request);

        log.debug("Admin {} modifié !", id);

        return admin;
    }

    public void deleteById(Integer id) {
        log.debug("Suppression du admin {} ...", id);

        this.repository.deleteById(id);

        log.debug("Admin {} supprimé !", id);
    }

    private Admin save(Admin admin, CreateOrUpdateAdminRequest request) {
        admin.setId(request.getId());
        admin.setLogin(request.getLogin());
        admin.setPassword(request.getPassword());

        return this.repository.save(admin);
    }
    
}