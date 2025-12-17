package com.sportsaas.tenant.domain.model;

import com.sportsaas.common.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashMap;
import java.util.Map;

/**
 * Entité représentant un tenant (organisation/entreprise) dans le système multi-tenant.
 *
 * <p>Un Tenant représente une organisation cliente qui utilise la plateforme.
 * Chaque tenant a ses propres données isolées des autres tenants.</p>
 *
 * <p>Note: Cette entité hérite de {@link BaseEntity} et non de TenantAwareEntity
 * car c'est l'entité racine du système multi-tenant.</p>
 */
@Entity
@Table(name = "tenants")
@Getter
@Setter
@NoArgsConstructor
public class Tenant extends BaseEntity {

    /**
     * Nom de l'organisation.
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * Identifiant unique URL-friendly (ex: "mon-entreprise").
     * Utilisé pour les URLs et l'identification.
     */
    @Column(name = "slug", nullable = false, unique = true, length = 50)
    private String slug;

    /**
     * Domaine personnalisé optionnel (ex: "app.monentreprise.com").
     */
    @Column(name = "domain", length = 255)
    private String domain;

    /**
     * URL du logo de l'organisation.
     */
    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    /**
     * Couleur principale pour la personnalisation de l'interface.
     */
    @Column(name = "primary_color", length = 7)
    private String primaryColor = "#1976d2";

    /**
     * Statut du tenant.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private TenantStatus status = TenantStatus.ACTIVE;

    /**
     * Configuration JSON spécifique au tenant.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "settings", columnDefinition = "jsonb")
    private Map<String, Object> settings = new HashMap<>();

    /**
     * Constructeur avec les champs obligatoires.
     */
    public Tenant(String name, String slug) {
        this.name = name;
        this.slug = slug;
    }

    /**
     * Vérifie si le tenant est actif.
     */
    public boolean isActive() {
        return TenantStatus.ACTIVE.equals(this.status);
    }

    /**
     * Active le tenant.
     */
    public void activate() {
        this.status = TenantStatus.ACTIVE;
    }

    /**
     * Suspend le tenant.
     */
    public void suspend() {
        this.status = TenantStatus.SUSPENDED;
    }

    /**
     * Désactive le tenant.
     */
    public void deactivate() {
        this.status = TenantStatus.INACTIVE;
    }
}
