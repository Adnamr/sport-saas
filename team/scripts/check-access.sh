#!/bin/bash
# ═══════════════════════════════════════════════════════════════════
# Script: check-access.sh
# Description: Vérifie que les accès sont configurés avant de travailler
# Usage: ./team/scripts/check-access.sh [--quiet]
# ═══════════════════════════════════════════════════════════════════

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m'

QUIET=${1:-""}
CONFIG_FILE="$HOME/.sport-saas/config"

ERRORS=0

# Charger la config si elle existe
if [ -f "$CONFIG_FILE" ]; then
    source "$CONFIG_FILE"
fi

# ═══════════════════════════════════════════════════════════════════
# Vérifier GitHub
# ═══════════════════════════════════════════════════════════════════

GITHUB_OK=false

if command -v gh &> /dev/null && gh auth status &> /dev/null 2>&1; then
    GITHUB_OK=true
elif [ -n "$GITHUB_TOKEN" ]; then
    GITHUB_OK=true
fi

if [ "$GITHUB_OK" = false ]; then
    if [ "$QUIET" != "--quiet" ]; then
        echo -e "${RED}✗ GitHub non configuré${NC}"
        echo -e "  → Lance: ${YELLOW}make access${NC}"
    fi
    ERRORS=$((ERRORS + 1))
fi

# ═══════════════════════════════════════════════════════════════════
# Vérifier OpenProject
# ═══════════════════════════════════════════════════════════════════

if [ -z "$OPENPROJECT_TOKEN" ]; then
    if [ "$QUIET" != "--quiet" ]; then
        echo -e "${RED}✗ OpenProject non configuré${NC}"
        echo -e "  → Lance: ${YELLOW}make access${NC}"
    fi
    ERRORS=$((ERRORS + 1))
fi

# ═══════════════════════════════════════════════════════════════════
# Vérifier Git config
# ═══════════════════════════════════════════════════════════════════

if [ -z "$(git config user.name 2>/dev/null)" ] || [ -z "$(git config user.email 2>/dev/null)" ]; then
    if [ "$QUIET" != "--quiet" ]; then
        echo -e "${RED}✗ Git user non configuré${NC}"
        echo -e "  → Lance: ${YELLOW}make access${NC}"
    fi
    ERRORS=$((ERRORS + 1))
fi

# ═══════════════════════════════════════════════════════════════════
# Résultat
# ═══════════════════════════════════════════════════════════════════

if [ $ERRORS -gt 0 ]; then
    if [ "$QUIET" != "--quiet" ]; then
        echo ""
        echo -e "${YELLOW}⚠️  Configure tes accès avant de continuer${NC}"
    fi
    exit 1
else
    if [ "$QUIET" != "--quiet" ]; then
        echo -e "${GREEN}✓ Tous les accès sont configurés${NC}"
    fi
    exit 0
fi
