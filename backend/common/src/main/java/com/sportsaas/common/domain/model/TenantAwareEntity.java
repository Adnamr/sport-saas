package com.sportsaas.common.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Classe de base pour toutes les entités multi-tenant.
 *
 * <p>Toutes les entités du projet DOIVENT hériter de cette classe
 * pour assurer l'isolation des données par tenant.</p>
 *
 * <p>Hérite de {@link BaseEntity} pour les champs communs (id, createdAt, updatedAt)
 * et ajoute le tenant_id obligatoire.</p>
 */
@MappedSuperclass
@Getter
@Setter
public abstract class TenantAwareEntity extends BaseEntity {

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;
}
