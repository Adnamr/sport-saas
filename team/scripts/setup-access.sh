#!/bin/bash
# ═══════════════════════════════════════════════════════════════════
# Script: setup-access.sh
# Description: Configure les accès GitHub et OpenProject pour un dev
# Usage: ./team/scripts/setup-access.sh [DEV_ID]
# ═══════════════════════════════════════════════════════════════════

set -e

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
RED='\033[0;31m'
CYAN='\033[0;36m'
GRAY='\033[0;90m'
NC='\033[0m'

DEV_ID=${1:-$(whoami)}
CONFIG_DIR="$HOME/.sport-saas"
CONFIG_FILE="$CONFIG_DIR/config"

echo ""
echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"
echo -e "${BLUE}            🔐 CONFIGURATION DES ACCÈS - ${DEV_ID}${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"
echo ""

# Créer le dossier de config
mkdir -p "$CONFIG_DIR"

# ═══════════════════════════════════════════════════════════════════
# 1. GITHUB
# ═══════════════════════════════════════════════════════════════════

echo -e "${CYAN}[1/3] Configuration GitHub${NC}"
echo ""

# Vérifier si gh CLI est installé
if command -v gh &> /dev/null; then
    echo -e "  ${GREEN}✓${NC} GitHub CLI (gh) détecté"
    
    # Vérifier si déjà authentifié
    if gh auth status &> /dev/null; then
        GH_USER=$(gh api user --jq '.login' 2>/dev/null || echo "")
        echo -e "  ${GREEN}✓${NC} Connecté en tant que: ${YELLOW}${GH_USER}${NC}"
    else
        echo -e "  ${YELLOW}→${NC} Authentification GitHub requise"
        echo ""
        read -p "  Lancer l'authentification GitHub? (y/n) " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            gh auth login
        fi
    fi
else
    echo -e "  ${YELLOW}⚠️${NC} GitHub CLI non installé"
    echo ""
    echo -e "  ${GRAY}Installation:${NC}"
    echo -e "    macOS:  ${CYAN}brew install gh${NC}"
    echo -e "    Linux:  ${CYAN}sudo apt install gh${NC}"
    echo -e "    Windows: ${CYAN}winget install GitHub.cli${NC}"
    echo ""
    
    # Alternative : token manuel
    echo -e "  ${YELLOW}Alternative: Token personnel${NC}"
    echo -e "  1. Va sur: ${BLUE}https://github.com/settings/tokens${NC}"
    echo -e "  2. Crée un token avec les scopes: repo, workflow"
    echo ""
    
    read -p "  Entrer ton GitHub Personal Access Token (ou Entrée pour skip): " GH_TOKEN
    
    if [ -n "$GH_TOKEN" ]; then
        echo "GITHUB_TOKEN=$GH_TOKEN" >> "$CONFIG_FILE"
        echo -e "  ${GREEN}✓${NC} Token GitHub sauvegardé"
    fi
fi

# Configurer Git user si pas fait
GIT_NAME=$(git config --global user.name 2>/dev/null || echo "")
GIT_EMAIL=$(git config --global user.email 2>/dev/null || echo "")

if [ -z "$GIT_NAME" ]; then
    echo ""
    read -p "  Nom Git (ex: Alice Dupont): " GIT_NAME
    git config --global user.name "$GIT_NAME"
fi

if [ -z "$GIT_EMAIL" ]; then
    read -p "  Email Git: " GIT_EMAIL
    git config --global user.email "$GIT_EMAIL"
fi

echo -e "  ${GREEN}✓${NC} Git configuré: ${GIT_NAME} <${GIT_EMAIL}>"

# ═══════════════════════════════════════════════════════════════════
# 2. OPENPROJECT
# ═══════════════════════════════════════════════════════════════════

echo ""
echo -e "${CYAN}[2/3] Configuration OpenProject${NC}"
echo ""

OPENPROJECT_URL="https://aam.openproject.com"
echo -e "  URL: ${BLUE}${OPENPROJECT_URL}${NC}"
echo ""

# Vérifier si un token existe déjà
EXISTING_OP_TOKEN=$(grep "OPENPROJECT_TOKEN" "$CONFIG_FILE" 2>/dev/null | cut -d'=' -f2 || echo "")

if [ -n "$EXISTING_OP_TOKEN" ]; then
    echo -e "  ${GREEN}✓${NC} Token OpenProject déjà configuré"
    read -p "  Reconfigurer? (y/n) " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        SKIP_OP=true
    fi
fi

if [ "$SKIP_OP" != "true" ]; then
    echo -e "  ${YELLOW}Pour obtenir ton token API:${NC}"
    echo -e "  1. Va sur: ${BLUE}${OPENPROJECT_URL}/my/access_token${NC}"
    echo -e "  2. Clique sur 'Generate' ou 'Regenerate'"
    echo -e "  3. Copie le token"
    echo ""
    
    read -p "  Entrer ton OpenProject API Token (ou Entrée pour skip): " OP_TOKEN
    
    if [ -n "$OP_TOKEN" ]; then
        # Supprimer l'ancien token s'il existe
        grep -v "OPENPROJECT_TOKEN" "$CONFIG_FILE" > "$CONFIG_FILE.tmp" 2>/dev/null || true
        mv "$CONFIG_FILE.tmp" "$CONFIG_FILE" 2>/dev/null || true
        
        echo "OPENPROJECT_TOKEN=$OP_TOKEN" >> "$CONFIG_FILE"
        echo "OPENPROJECT_URL=$OPENPROJECT_URL" >> "$CONFIG_FILE"
        echo -e "  ${GREEN}✓${NC} Token OpenProject sauvegardé"
        
        # Tester le token
        echo -e "  ${GRAY}Test de connexion...${NC}"
        RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" -u "apikey:$OP_TOKEN" "${OPENPROJECT_URL}/api/v3/users/me" 2>/dev/null || echo "000")
        
        if [ "$RESPONSE" = "200" ]; then
            echo -e "  ${GREEN}✓${NC} Connexion OpenProject OK"
        else
            echo -e "  ${YELLOW}⚠️${NC} Impossible de vérifier (code: $RESPONSE)"
        fi
    fi
fi

# ═══════════════════════════════════════════════════════════════════
# 3. VÉRIFICATION FINALE
# ═══════════════════════════════════════════════════════════════════

echo ""
echo -e "${CYAN}[3/3] Vérification des accès${NC}"
echo ""

# Charger la config
if [ -f "$CONFIG_FILE" ]; then
    source "$CONFIG_FILE"
fi

# Status GitHub
if command -v gh &> /dev/null && gh auth status &> /dev/null; then
    echo -e "  ${GREEN}✓${NC} GitHub:      Connecté"
elif [ -n "$GITHUB_TOKEN" ]; then
    echo -e "  ${YELLOW}~${NC} GitHub:      Token configuré (non vérifié)"
else
    echo -e "  ${RED}✗${NC} GitHub:      Non configuré"
fi

# Status OpenProject
if [ -n "$OPENPROJECT_TOKEN" ]; then
    echo -e "  ${GREEN}✓${NC} OpenProject: Configuré"
else
    echo -e "  ${RED}✗${NC} OpenProject: Non configuré"
fi

# Status Git
if [ -n "$(git config --global user.name)" ]; then
    echo -e "  ${GREEN}✓${NC} Git:         $(git config --global user.name)"
else
    echo -e "  ${RED}✗${NC} Git:         Non configuré"
fi

# ═══════════════════════════════════════════════════════════════════
# RÉSUMÉ
# ═══════════════════════════════════════════════════════════════════

echo ""
echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"
echo -e "${GREEN}✅ CONFIGURATION TERMINÉE${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"
echo ""
echo -e "Fichier de config: ${GRAY}${CONFIG_FILE}${NC}"
echo ""
echo -e "${YELLOW}Prochaines étapes:${NC}"
echo -e "  1. ${GREEN}make dev DEV=${DEV_ID}${NC}    → Créer ton profil"
echo -e "  2. ${GREEN}make tickets${NC}             → Voir les tickets"
echo -e "  3. ${GREEN}make ticket TICKET=xxx${NC}   → Commencer à travailler"
echo ""

# Sécuriser le fichier de config
chmod 600 "$CONFIG_FILE" 2>/dev/null || true
