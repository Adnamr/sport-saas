package com.sportsaas.tenant.context;

import java.util.Optional;
import java.util.UUID;

/**
 * Contexte du tenant pour la requête courante.
 *
 * <p>Cette classe utilise un ThreadLocal pour stocker l'identifiant du tenant
 * associé à la requête HTTP courante. Elle permet d'accéder au tenant ID
 * depuis n'importe quelle couche de l'application sans passer explicitement
 * le paramètre.</p>
 *
 * <p>Le cycle de vie typique est :</p>
 * <ol>
 *   <li>Un filtre HTTP résout le tenant (via header, domaine, etc.)</li>
 *   <li>Le filtre appelle {@link #setCurrentTenantId(UUID)}</li>
 *   <li>Les services accèdent au tenant via {@link #getCurrentTenantId()}</li>
 *   <li>À la fin de la requête, le filtre appelle {@link #clear()}</li>
 * </ol>
 *
 * <p><strong>Important :</strong> Toujours appeler {@link #clear()} dans un bloc finally
 * pour éviter les fuites de mémoire et la contamination entre requêtes.</p>
 */
public final class TenantContext {

    private static final ThreadLocal<UUID> CURRENT_TENANT = new ThreadLocal<>();

    private TenantContext() {
        // Utility class - no instantiation
    }

    /**
     * Définit l'identifiant du tenant pour le thread courant.
     *
     * @param tenantId l'identifiant du tenant
     */
    public static void setCurrentTenantId(UUID tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    /**
     * Retourne l'identifiant du tenant pour le thread courant.
     *
     * @return l'identifiant du tenant ou vide si non défini
     */
    public static Optional<UUID> getCurrentTenantId() {
        return Optional.ofNullable(CURRENT_TENANT.get());
    }

    /**
     * Retourne l'identifiant du tenant pour le thread courant.
     *
     * @return l'identifiant du tenant
     * @throws IllegalStateException si aucun tenant n'est défini
     */
    public static UUID requireCurrentTenantId() {
        UUID tenantId = CURRENT_TENANT.get();
        if (tenantId == null) {
            throw new IllegalStateException("No tenant ID set in current context");
        }
        return tenantId;
    }

    /**
     * Vérifie si un tenant est défini pour le thread courant.
     *
     * @return true si un tenant est défini
     */
    public static boolean hasTenant() {
        return CURRENT_TENANT.get() != null;
    }

    /**
     * Supprime l'identifiant du tenant du thread courant.
     *
     * <p>Cette méthode doit être appelée à la fin de chaque requête
     * pour éviter les fuites de mémoire.</p>
     */
    public static void clear() {
        CURRENT_TENANT.remove();
    }
}
