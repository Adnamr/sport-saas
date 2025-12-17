package com.sportsaas.tenant.infra.repository;

import com.sportsaas.tenant.domain.model.Tenant;
import com.sportsaas.tenant.domain.model.TenantStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository JPA pour l'entité Tenant.
 *
 * <p>Cette interface étend JpaRepository de Spring Data JPA
 * et fournit les méthodes de persistance pour les tenants.</p>
 */
@Repository
public interface JpaTenantRepository extends JpaRepository<Tenant, UUID> {

    /**
     * Trouve un tenant par son slug.
     */
    Optional<Tenant> findBySlug(String slug);

    /**
     * Trouve un tenant par son domaine.
     */
    Optional<Tenant> findByDomain(String domain);

    /**
     * Retourne tous les tenants avec un statut donné.
     */
    List<Tenant> findByStatus(TenantStatus status);

    /**
     * Vérifie si un slug existe déjà.
     */
    boolean existsBySlug(String slug);

    /**
     * Vérifie si un domaine existe déjà.
     */
    boolean existsByDomain(String domain);
}
