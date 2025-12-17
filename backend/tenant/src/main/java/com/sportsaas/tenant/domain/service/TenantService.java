package com.sportsaas.tenant.domain.service;

import com.sportsaas.tenant.domain.model.Tenant;
import com.sportsaas.tenant.domain.model.TenantStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service pour la gestion des tenants.
 *
 * <p>Cette interface définit les opérations métier pour les tenants.
 * L'implémentation se trouve dans le package infra.</p>
 */
public interface TenantService {

    /**
     * Crée un nouveau tenant.
     *
     * @param tenant le tenant à créer
     * @return le tenant créé avec son ID généré
     * @throws IllegalArgumentException si le slug ou domaine existe déjà
     */
    Tenant create(Tenant tenant);

    /**
     * Met à jour un tenant existant.
     *
     * @param id l'identifiant du tenant
     * @param tenant les nouvelles données du tenant
     * @return le tenant mis à jour
     * @throws IllegalArgumentException si le tenant n'existe pas
     */
    Tenant update(UUID id, Tenant tenant);

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
     * Active un tenant.
     *
     * @param id l'identifiant du tenant
     * @return le tenant activé
     * @throws IllegalArgumentException si le tenant n'existe pas
     */
    Tenant activate(UUID id);

    /**
     * Suspend un tenant.
     *
     * @param id l'identifiant du tenant
     * @return le tenant suspendu
     * @throws IllegalArgumentException si le tenant n'existe pas
     */
    Tenant suspend(UUID id);

    /**
     * Désactive un tenant définitivement.
     *
     * @param id l'identifiant du tenant
     * @return le tenant désactivé
     * @throws IllegalArgumentException si le tenant n'existe pas
     */
    Tenant deactivate(UUID id);

    /**
     * Supprime un tenant.
     *
     * @param id l'identifiant du tenant
     * @throws IllegalArgumentException si le tenant n'existe pas
     */
    void delete(UUID id);

    /**
     * Vérifie si un slug est disponible.
     *
     * @param slug le slug à vérifier
     * @return true si le slug est disponible
     */
    boolean isSlugAvailable(String slug);

    /**
     * Vérifie si un domaine est disponible.
     *
     * @param domain le domaine à vérifier
     * @return true si le domaine est disponible
     */
    boolean isDomainAvailable(String domain);
}
