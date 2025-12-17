package com.sportsaas.tenant.domain.repository;

import com.sportsaas.tenant.domain.model.Tenant;
import com.sportsaas.tenant.domain.model.TenantStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface de repository pour l'entité Tenant.
 *
 * <p>Cette interface définit les opérations de persistance pour les tenants.
 * L'implémentation se trouve dans le package infra.</p>
 */
public interface TenantRepository {

    /**
     * Sauvegarde un tenant.
     *
     * @param tenant le tenant à sauvegarder
     * @return le tenant sauvegardé avec son ID généré
     */
    Tenant save(Tenant tenant);

    /**
     * Trouve un tenant par son ID.
     *
     * @param id l'identifiant du tenant
     * @return le tenant ou vide si non trouvé
     */
    Optional<Tenant> findById(UUID id);

    /**
     * Trouve un tenant par son slug.
     *
     * @param slug le slug unique du tenant
     * @return le tenant ou vide si non trouvé
     */
    Optional<Tenant> findBySlug(String slug);

    /**
     * Trouve un tenant par son domaine.
     *
     * @param domain le domaine personnalisé
     * @return le tenant ou vide si non trouvé
     */
    Optional<Tenant> findByDomain(String domain);

    /**
     * Retourne tous les tenants.
     *
     * @return la liste de tous les tenants
     */
    List<Tenant> findAll();

    /**
     * Retourne tous les tenants avec un statut donné.
     *
     * @param status le statut recherché
     * @return la liste des tenants avec ce statut
     */
    List<Tenant> findByStatus(TenantStatus status);

    /**
     * Vérifie si un slug existe déjà.
     *
     * @param slug le slug à vérifier
     * @return true si le slug existe
     */
    boolean existsBySlug(String slug);

    /**
     * Vérifie si un domaine existe déjà.
     *
     * @param domain le domaine à vérifier
     * @return true si le domaine existe
     */
    boolean existsByDomain(String domain);

    /**
     * Supprime un tenant par son ID.
     *
     * @param id l'identifiant du tenant à supprimer
     */
    void deleteById(UUID id);
}
