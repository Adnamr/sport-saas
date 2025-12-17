package com.sportsaas.tenant.domain.model;

/**
 * Statut d'un tenant dans le système.
 */
public enum TenantStatus {
    /** Tenant actif et opérationnel */
    ACTIVE,

    /** Tenant temporairement suspendu */
    SUSPENDED,

    /** Tenant en attente d'activation */
    PENDING,

    /** Tenant désactivé définitivement */
    INACTIVE
}
