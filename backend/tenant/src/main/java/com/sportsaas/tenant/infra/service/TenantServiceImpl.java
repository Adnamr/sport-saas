package com.sportsaas.tenant.infra.service;

import com.sportsaas.common.exception.NotFoundException;
import com.sportsaas.common.exception.ValidationException;
import com.sportsaas.tenant.domain.model.Tenant;
import com.sportsaas.tenant.domain.model.TenantStatus;
import com.sportsaas.tenant.domain.repository.TenantRepository;
import com.sportsaas.tenant.domain.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implémentation du TenantService.
 *
 * <p>Cette classe fournit la logique métier pour la gestion des tenants.</p>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;

    @Override
    @Transactional
    public Tenant create(Tenant tenant) {
        validateSlugUniqueness(tenant.getSlug(), null);
        if (tenant.getDomain() != null && !tenant.getDomain().isBlank()) {
            validateDomainUniqueness(tenant.getDomain(), null);
        }
        return tenantRepository.save(tenant);
    }

    @Override
    @Transactional
    public Tenant update(UUID id, Tenant tenant) {
        Tenant existing = findByIdOrThrow(id);

        if (!existing.getSlug().equals(tenant.getSlug())) {
            validateSlugUniqueness(tenant.getSlug(), id);
        }
        if (tenant.getDomain() != null && !tenant.getDomain().isBlank()) {
            if (!tenant.getDomain().equals(existing.getDomain())) {
                validateDomainUniqueness(tenant.getDomain(), id);
            }
        }

        existing.setName(tenant.getName());
        existing.setSlug(tenant.getSlug());
        existing.setDomain(tenant.getDomain());
        existing.setLogoUrl(tenant.getLogoUrl());
        existing.setPrimaryColor(tenant.getPrimaryColor());
        existing.setSettings(tenant.getSettings());

        return tenantRepository.save(existing);
    }

    @Override
    public Optional<Tenant> findById(UUID id) {
        return tenantRepository.findById(id);
    }

    @Override
    public Optional<Tenant> findBySlug(String slug) {
        return tenantRepository.findBySlug(slug);
    }

    @Override
    public Optional<Tenant> findByDomain(String domain) {
        return tenantRepository.findByDomain(domain);
    }

    @Override
    public List<Tenant> findAll() {
        return tenantRepository.findAll();
    }

    @Override
    public List<Tenant> findByStatus(TenantStatus status) {
        return tenantRepository.findByStatus(status);
    }

    @Override
    @Transactional
    public Tenant activate(UUID id) {
        Tenant tenant = findByIdOrThrow(id);
        tenant.activate();
        return tenantRepository.save(tenant);
    }

    @Override
    @Transactional
    public Tenant suspend(UUID id) {
        Tenant tenant = findByIdOrThrow(id);
        tenant.suspend();
        return tenantRepository.save(tenant);
    }

    @Override
    @Transactional
    public Tenant deactivate(UUID id) {
        Tenant tenant = findByIdOrThrow(id);
        tenant.deactivate();
        return tenantRepository.save(tenant);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (tenantRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Tenant", id);
        }
        tenantRepository.deleteById(id);
    }

    @Override
    public boolean isSlugAvailable(String slug) {
        return !tenantRepository.existsBySlug(slug);
    }

    @Override
    public boolean isDomainAvailable(String domain) {
        return !tenantRepository.existsByDomain(domain);
    }

    private Tenant findByIdOrThrow(UUID id) {
        return tenantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tenant", id));
    }

    private void validateSlugUniqueness(String slug, UUID excludeId) {
        tenantRepository.findBySlug(slug)
                .filter(t -> excludeId == null || !t.getId().equals(excludeId))
                .ifPresent(t -> {
                    throw new ValidationException("Le slug '" + slug + "' est déjà utilisé");
                });
    }

    private void validateDomainUniqueness(String domain, UUID excludeId) {
        tenantRepository.findByDomain(domain)
                .filter(t -> excludeId == null || !t.getId().equals(excludeId))
                .ifPresent(t -> {
                    throw new ValidationException("Le domaine '" + domain + "' est déjà utilisé");
                });
    }
}
