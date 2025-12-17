#!/bin/bash
# ═══════════════════════════════════════════════════════════════════
# Script: complete-ticket.sh
# Usage: ./team/scripts/complete-ticket.sh [DEV_ID]
# ═══════════════════════════════════════════════════════════════════

set -e

# Couleurs
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'

# Arguments
DEV_ID=${1:-$(whoami)}
DEV_FILE="team/devs/${DEV_ID}.yaml"

echo ""
echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"
echo -e "${BLUE}                    ✅ FINALISATION DU TICKET${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"
echo ""

# ═══════════════════════════════════════════════════════════════════
# 1. Récupérer le ticket en cours
# ═══════════════════════════════════════════════════════════════════

echo -e "${CYAN}[1/5] Vérification du ticket en cours...${NC}"

if [ ! -f "$DEV_FILE" ]; then
    echo -e "  ${RED}❌ Fichier développeur non trouvé: ${DEV_FILE}${NC}"
    exit 1
fi

TICKET_ID=$(grep -A 10 "^current_ticket:" "$DEV_FILE" 2>/dev/null | grep "ticket_id:" | head -1 | grep -o '"[^"]*"' | tr -d '"' || echo "")
OPENPROJECT_ID=$(grep -A 10 "^current_ticket:" "$DEV_FILE" 2>/dev/null | grep "openproject_id:" | head -1 | awk '{print $2}' || echo "0")

if [ -z "$TICKET_ID" ] || [ "$TICKET_ID" = "null" ]; then
    echo -e "  ${RED}❌ Aucun ticket en cours pour ${DEV_ID}${NC}"
    echo ""
    echo -e "  Pour prendre un ticket: ${YELLOW}make ticket TICKET=xxx${NC}"
    exit 1
fi

echo -e "  ${GREEN}✓ Ticket: ${TICKET_ID}${NC}"

# ═══════════════════════════════════════════════════════════════════
# 2. Vérifier la branche
# ═══════════════════════════════════════════════════════════════════

echo ""
echo -e "${CYAN}[2/5] Vérification de la branche...${NC}"

CURRENT_BRANCH=$(git branch --show-current 2>/dev/null || echo "")

if [ -z "$CURRENT_BRANCH" ]; then
    echo -e "  ${RED}❌ Pas sur une branche Git${NC}"
    exit 1
fi

echo -e "  ${GREEN}✓ Branche: ${CURRENT_BRANCH}${NC}"

# ═══════════════════════════════════════════════════════════════════
# 3. Gérer les changements non commités
# ═══════════════════════════════════════════════════════════════════

echo ""
echo -e "${CYAN}[3/5] Vérification des changements...${NC}"

if [ -n "$(git status --porcelain)" ]; then
    echo -e "  ${YELLOW}⚠️ Changements non commités détectés:${NC}"
    git status --short | head -10
    echo ""
    
    read -p "  Commiter maintenant? (y/n) " -n 1 -r
    echo
    
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        read -p "  Message du commit: " COMMIT_MSG
        git add .
        
        # Format du commit
        TYPE="feat"
        if [[ "$COMMIT_MSG" == *"fix"* ]]; then
            TYPE="fix"
        elif [[ "$COMMIT_MSG" == *"test"* ]]; then
            TYPE="test"
        fi
        
        FORMATTED_MSG="${TYPE}(${TICKET_ID}): ${COMMIT_MSG} - Refs #${OPENPROJECT_ID}"
        git commit -m "$FORMATTED_MSG"
        echo -e "  ${GREEN}✓ Commit: ${FORMATTED_MSG}${NC}"
    else
        echo -e "  ${RED}❌ Commite d'abord: make commit MSG=\"...\"${NC}"
        exit 1
    fi
else
    echo -e "  ${GREEN}✓ Pas de changements non commités${NC}"
fi

# ═══════════════════════════════════════════════════════════════════
# 4. Push
# ═══════════════════════════════════════════════════════════════════

echo ""
echo -e "${CYAN}[4/5] Push de la branche...${NC}"

git push -u origin "$CURRENT_BRANCH" 2>/dev/null || git push origin "$CURRENT_BRANCH"
echo -e "  ${GREEN}✓ Branche pushée${NC}"

# ═══════════════════════════════════════════════════════════════════
# 5. Mettre à jour les fichiers de suivi
# ═══════════════════════════════════════════════════════════════════

echo ""
echo -e "${CYAN}[5/5] Mise à jour du suivi...${NC}"

TIMESTAMP=$(date -u +"%Y-%m-%dT%H:%M:%S")
CONFIG_FILE="$HOME/.sport-saas/config"

# Réinitialiser current_ticket dans le fichier dev
cat > "$DEV_FILE" << EOF
dev:
  id: "${DEV_ID}"
  name: "${DEV_ID}"
  email: "${DEV_ID}@example.com"
  slack: "@${DEV_ID}"
  role: "Développeur"
  joined_at: "$(date +%Y-%m-%d)"

current_ticket: null

claude_preferences:
  explanation_level: "normal"
  auto_generate_tests: true

history:
  - ticket_id: "${TICKET_ID}"
    completed_at: "${TIMESTAMP}"
    branch: "${CURRENT_BRANCH}"
EOF

echo -e "  ${GREEN}✓${NC} Fichiers locaux mis à jour"

# Mettre à jour OpenProject si configuré
if [ "$OPENPROJECT_ID" != "0" ] && [ -f "$CONFIG_FILE" ]; then
    source "$CONFIG_FILE"
    if [ -n "$OPENPROJECT_TOKEN" ]; then
        echo -e "  ${GRAY}→ Mise à jour OpenProject...${NC}"
        
        # Passer en "In Review"
        ./team/scripts/openproject-api.sh review "$OPENPROJECT_ID" 2>/dev/null && \
            echo -e "  ${GREEN}✓${NC} OpenProject → In Review" || \
            echo -e "  ${YELLOW}⚠️${NC} OpenProject non mis à jour"
        
        # Ajouter un commentaire avec le lien PR
        REMOTE_URL=$(git remote get-url origin 2>/dev/null | sed 's/\.git$//' | sed 's/git@github.com:/https:\/\/github.com\//')
        PR_COMMENT="PR: ${REMOTE_URL}/compare/develop...${CURRENT_BRANCH}"
        ./team/scripts/openproject-api.sh comment "$OPENPROJECT_ID" "$PR_COMMENT" 2>/dev/null || true
    fi
fi

# Commit des fichiers de suivi
git add team/ 2>/dev/null || true
git commit -m "chore: ${DEV_ID} completes ticket ${TICKET_ID}" --allow-empty 2>/dev/null || true
git push origin "$CURRENT_BRANCH" 2>/dev/null || true

echo -e "  ${GREEN}✓${NC} Suivi mis à jour"

# ═══════════════════════════════════════════════════════════════════
# Résumé
# ═══════════════════════════════════════════════════════════════════

# Générer le lien PR
REMOTE_URL=$(git remote get-url origin 2>/dev/null | sed 's/\.git$//' | sed 's/git@github.com:/https:\/\/github.com\//')
PR_URL="${REMOTE_URL}/compare/develop...${CURRENT_BRANCH}?expand=1"

echo ""
echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"
echo -e "${GREEN}✅ TICKET ${TICKET_ID} PRÊT POUR REVIEW !${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"
echo ""
echo -e "${YELLOW}📋 Prochaines étapes:${NC}"
echo ""
echo -e "   1. ${CYAN}Créer la PR sur GitHub:${NC}"
echo -e "      ${BLUE}${PR_URL}${NC}"
echo ""
echo -e "   2. ${CYAN}Mettre à jour OpenProject:${NC}"
echo -e "      Status → 'In Review'"
echo -e "      Ajouter le lien de la PR en commentaire"
echo ""
echo -e "   3. ${CYAN}Après le merge:${NC}"
echo -e "      ${GREEN}make sync${NC}  (retour sur develop)"
echo ""

# Ouvrir automatiquement le lien PR si possible
if command -v xdg-open &> /dev/null; then
    read -p "Ouvrir la page de création de PR? (y/n) " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        xdg-open "$PR_URL" 2>/dev/null || true
    fi
elif command -v open &> /dev/null; then
    read -p "Ouvrir la page de création de PR? (y/n) " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        open "$PR_URL" 2>/dev/null || true
    fi
fi
