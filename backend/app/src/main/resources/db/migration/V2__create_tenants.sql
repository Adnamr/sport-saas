-- ═══════════════════════════════════════════════════════════════════
--                    V2: TABLE TENANTS
-- ═══════════════════════════════════════════════════════════════════

CREATE TABLE tenants (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(50) NOT NULL UNIQUE,
    domain VARCHAR(255),
    logo_url VARCHAR(500),
    primary_color VARCHAR(7) DEFAULT '#1976d2',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    settings JSONB DEFAULT '{}',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Index pour recherche par slug
CREATE INDEX idx_tenants_slug ON tenants(slug);

-- Index pour recherche par domain
CREATE INDEX idx_tenants_domain ON tenants(domain);

-- Index pour filtrer par status
CREATE INDEX idx_tenants_status ON tenants(status);

-- Commentaires
COMMENT ON TABLE tenants IS 'Table des organisations/entreprises (multi-tenant)';
COMMENT ON COLUMN tenants.slug IS 'Identifiant unique URL-friendly';
COMMENT ON COLUMN tenants.domain IS 'Domaine personnalisé optionnel';
COMMENT ON COLUMN tenants.settings IS 'Configuration JSON spécifique au tenant';
