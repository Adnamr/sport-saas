package com.sportsaas.tenant.infra.repository;

import com.sportsaas.tenant.domain.model.Tenant;
import com.sportsaas.tenant.domain.model.TenantStatus;
import com.sportsaas.tenant.domain.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implémentation du TenantRepository utilisant JPA.
 *
 * <p>Cette classe fait le pont entre l'interface du domaine
 * et le repository JPA de Spring Data.</p>
 */
@Component
@RequiredArgsConstructor
public class TenantRepositoryImpl implements TenantRepository {

    private final JpaTenantRepository jpaTenantRepository;

    @Override
    public Tenant save(Tenant tenant) {
        return jpaTenantRepository.save(tenant);
    }

    @Override
    public Optional<Tenant> findById(UUID id) {
        return jpaTenantRepository.findById(id);
    }

    @Override
    public Optional<Tenant> findBySlug(String slug) {
        return jpaTenantRepository.findBySlug(slug);
    }

    @Override
    public Optional<Tenant> findByDomain(String domain) {
        return jpaTenantRepository.findByDomain(domain);
    }

    @Override
    public List<Tenant> findAll() {
        return jpaTenantRepository.findAll();
    }

    @Override
    public List<Tenant> findByStatus(TenantStatus status) {
        return jpaTenantRepository.findByStatus(status);
    }

    @Override
    public boolean existsBySlug(String slug) {
        return jpaTenantRepository.existsBySlug(slug);
    }

    @Override
    public boolean existsByDomain(String domain) {
        return jpaTenantRepository.existsByDomain(domain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaTenantRepository.deleteById(id);
    }
}
