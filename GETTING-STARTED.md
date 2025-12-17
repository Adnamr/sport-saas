# 🚀 Guide de Lancement du Projet

## Vue d'ensemble

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                             │
│   ÉTAPE 1          ÉTAPE 2           ÉTAPE 3           ÉTAPE 4             │
│   ────────         ────────          ────────          ────────             │
│                                                                             │
│   Créer repo   →   Premier ticket  →  Docker up    →   Dev en cours        │
│   GitHub           E1-001             PostgreSQL       avec Claude          │
│   + Push           Spring Boot                         Code                 │
│   structure                                                                 │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 📋 Prérequis

Vérifie que tu as installé :

```bash
# Java 17
java -version
# → java version "17.x.x"

# Maven
mvn -version
# → Apache Maven 3.8+

# Node.js
node -version
# → v18.x.x ou v20.x.x

# Docker
docker -version
# → Docker version 24+

# Git
git -version
# → git version 2.x

# Claude Code CLI (optionnel mais recommandé)
claude --version
```

---

## 🎯 Étape 1 : Initialiser le projet

### 1.1 Extraire le ZIP

```bash
# Extraire le ZIP téléchargé
unzip sport-saas-structure.zip
cd sport-saas
```

### 1.2 Initialiser Git

```bash
# Initialiser le repo
git init
git checkout -b develop

# Premier commit
git add .
git commit -m "chore: initial project structure"
```

### 1.3 Créer le repo GitHub

```bash
# Créer le repo sur GitHub (via CLI ou interface web)
gh repo create sport-saas --private --source=. --push

# Ou manuellement :
# 1. Créer le repo sur github.com
# 2. Puis :
git remote add origin https://github.com/[TON_ORG]/sport-saas.git
git push -u origin develop
```

---

## 🎯 Étape 2 : Premier ticket avec Claude Code

Le projet est vide pour l'instant ! On va utiliser Claude Code pour générer le code.

### 2.1 Prendre le premier ticket

```bash
# Se mettre sur develop
git checkout develop

# Créer la branche pour E1-001
./team/scripts/take-ticket.sh E1-001 [TON_NOM] 1

# Exemple :
./team/scripts/take-ticket.sh E1-001 alice 1
```

### 2.2 Lancer Claude Code

```bash
# Option 1 : Claude Code CLI
claude

# Option 2 : Utiliser l'interface Claude.ai avec le contexte
# Copier le contenu de CLAUDE.md + le prompt
```

### 2.3 Envoyer le prompt de démarrage

Copie ce prompt et envoie-le à Claude :

```
Je démarre le projet Sport Equipment SaaS.

Ticket: E1-001 - Setup Spring Boot

## Contexte
- Projet Maven multi-modules
- Java 17, Spring Boot 3.3
- Structure Clean Architecture par module

## Critères d'acceptation
- [ ] Projet Maven multi-modules initialisé
- [ ] Structure de dossiers conforme (app, common, config, auth, tenant, catalog, inventory, order, billing, notification)
- [ ] Configuration Spring Boot de base
- [ ] Application démarre sans erreur

## À générer

1. **backend/pom.xml** - POM parent avec tous les modules
2. **backend/app/pom.xml** - Module principal
3. **backend/app/src/main/java/com/sportsaas/Application.java** - Classe main
4. **backend/app/src/main/resources/application.yml** - Config de base
5. **backend/common/pom.xml** - Module common
6. **backend/common/.../TenantAwareEntity.java** - Entité de base

Génère tous les fichiers nécessaires pour que `mvn spring-boot:run` fonctionne.
```

### 2.4 Appliquer le code généré

Claude va générer les fichiers. Crée-les dans ton projet :

```bash
# Exemple : créer un fichier
cat > backend/pom.xml << 'EOF'
[CONTENU GÉNÉRÉ PAR CLAUDE]
EOF

# Ou utiliser ton IDE pour créer les fichiers
```

### 2.5 Tester

```bash
# Compiler
cd backend
mvn clean compile

# Si ça compile, commit !
git add .
git commit -m "feat(E1-001): setup spring boot project - Refs #1"
```

---

## 🎯 Étape 3 : Démarrer Docker

### 3.1 Créer le docker-compose

Si pas encore fait, crée le fichier :

```bash
cat > docker-compose.yml << 'EOF'
version: '3.8'

services:
  postgres:
    image: postgres:16-alpine
    container_name: sport-saas-db
    environment:
      POSTGRES_DB: sportsaas
      POSTGRES_USER: sportsaas
      POSTGRES_PASSWORD: sportsaas
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  mailhog:
    image: mailhog/mailhog
    container_name: sport-saas-mail
    ports:
      - "1025:1025"
      - "8025:8025"

volumes:
  postgres_data:
EOF
```

### 3.2 Démarrer les services

```bash
# Démarrer PostgreSQL et Mailhog
docker compose up -d

# Vérifier
docker compose ps
```

---

## 🎯 Étape 4 : Lancer l'application

### 4.1 Backend

```bash
cd backend

# Lancer Spring Boot
./mvnw spring-boot:run -pl app

# Ou avec le profil dev
./mvnw spring-boot:run -pl app -Dspring-boot.run.profiles=dev
```

**Vérifier :**
- http://localhost:8080 → Doit répondre
- http://localhost:8080/swagger-ui.html → Interface Swagger (si configuré)

### 4.2 Frontend (plus tard, après le ticket E11-001)

```bash
cd frontend
npm install
npm start
```

**Vérifier :**
- http://localhost:4200

---

## 🎯 Étape 5 : Continuer le développement

### Workflow quotidien

```bash
# 1. Voir les tickets disponibles
cat team/tickets/current.yaml

# 2. Prendre un ticket
./team/scripts/take-ticket.sh E1-002 alice 42

# 3. Utiliser un prompt Claude
cat team/prompts/create-entity.md
# Adapter et envoyer à Claude Code

# 4. Coder avec l'aide de Claude

# 5. Tester
mvn test

# 6. Commit
git add .
git commit -m "feat(E1-002): configure postgresql - Refs #42"

# 7. Terminer
./team/scripts/complete-ticket.sh E1-002 alice

# 8. Créer la PR sur GitHub
```

---

## 📋 Ordre des premiers tickets

```
Layer 0 (Pas de dépendances - À faire en parallèle)
├── E1-001  Setup Spring Boot        → Alice
├── E11-001 Setup Angular            → Bob
├── E11-002 Design System CSS        → Bob
└── E12-001 Dockerfile Backend       → Charlie

Layer 1 (Après E1-001)
├── E1-002  PostgreSQL + Flyway
├── E1-004  Module Common
├── E1-005  OpenAPI / Swagger
└── E1-006  Logs structurés

Layer 2 (Après E1-002)
├── E2-001  Modèle Tenant
└── E2-005  BaseEntity avec tenant_id

... et ainsi de suite
```

---

## 🛠️ Commandes utiles

```bash
# Docker
docker compose up -d          # Démarrer
docker compose down           # Arrêter
docker compose logs -f        # Logs

# Backend
cd backend
./mvnw clean compile          # Compiler
./mvnw test                   # Tests
./mvnw spring-boot:run -pl app # Lancer

# Frontend
cd frontend
npm install                   # Installer dépendances
npm start                     # Lancer
npm test                      # Tests

# Git
git checkout develop && git pull    # Sync
./team/scripts/take-ticket.sh ...   # Prendre ticket
./team/scripts/complete-ticket.sh   # Terminer ticket
```

---

## ❓ Problèmes courants

### "Port 5432 already in use"
```bash
# Arrêter le PostgreSQL local
sudo systemctl stop postgresql
# Ou changer le port dans docker-compose.yml
```

### "mvn: command not found"
```bash
# Utiliser le wrapper Maven inclus
./mvnw au lieu de mvn
```

### "Application won't start"
```bash
# Vérifier que PostgreSQL tourne
docker compose ps

# Vérifier les logs
docker compose logs postgres
```

### "Claude Code ne comprend pas le contexte"
```bash
# S'assurer que CLAUDE.md est à la racine
# Envoyer le contenu de CLAUDE.md en premier
cat CLAUDE.md
```

---

## 🎉 C'est parti !

```bash
# Résumé pour démarrer maintenant :

# 1. Extraire et initialiser
unzip sport-saas-structure.zip && cd sport-saas
git init && git checkout -b develop

# 2. Démarrer Docker
docker compose up -d

# 3. Prendre le premier ticket
./team/scripts/take-ticket.sh E1-001 [ton_nom] 1

# 4. Lancer Claude Code et générer le code
claude  # ou utiliser Claude.ai

# 5. Let's go! 🚀
```
