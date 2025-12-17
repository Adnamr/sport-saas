# 🏀 Sport Equipment SaaS

> Plateforme SaaS multi-tenant pour la gestion de matériels sportifs.

[![CI Backend](https://github.com/[ORG]/sport-saas/actions/workflows/ci-backend.yml/badge.svg)](https://github.com/[ORG]/sport-saas/actions)

---

## 🚀 Quick Start

```bash
# Cloner
git clone https://github.com/[ORG]/sport-saas.git
cd sport-saas

# Setup
make setup

# Démarrer
make up        # Services Docker (PostgreSQL, Mailhog)
make backend   # Backend Spring Boot (localhost:8080)
make frontend  # Frontend Angular (localhost:4200)
```

---

## 📚 Documentation

### 🆕 Nouveau dans l'équipe ?

| Étape | Document |
|-------|----------|
| 1 | [Guide de bienvenue](team/onboarding/WELCOME.md) |
| 2 | [Installation environnement](team/onboarding/SETUP.md) |
| 3 | [Règles de travail](team/rules/WORKFLOW.md) |
| 4 | [Premier ticket](team/onboarding/FIRST-TICKET.md) |

### 📖 Documentation technique

| Document | Description |
|----------|-------------|
| [Architecture](docs/architecture/ARCHITECTURE.md) | Structure technique du projet |
| [Base de données](docs/architecture/DATABASE.md) | Schéma et tables |
| [API](docs/architecture/API.md) | Documentation des endpoints |
| [Modules](docs/architecture/MODULES.md) | Responsabilités par module |

### 📋 Gestion de projet

| Document | Description |
|----------|-------------|
| [Backlog](docs/backlog/BACKLOG.md) | Liste des tickets |
| [Dépendances](docs/backlog/DEPENDENCIES.md) | Graphe des dépendances |
| [Sprints](docs/backlog/SPRINTS.md) | Planning |

---

## 🔗 Liens utiles

| Outil | URL |
|-------|-----|
| **OpenProject** | https://aam.openproject.com/ |
| **GitHub** | https://github.com/[ORG]/sport-saas |
| **Swagger** | http://localhost:8080/swagger-ui.html |
| **Mailhog** | http://localhost:8025 |

---

## 🛠️ Commandes Make

```bash
make help       # Voir toutes les commandes

# Docker
make up         # Démarrer les services
make down       # Arrêter les services
make logs       # Voir les logs

# Développement
make backend    # Lancer le backend
make frontend   # Lancer le frontend
make test       # Lancer les tests

# Git workflow
make ticket ID=42 NAME=E1-002-xxx   # Créer une branche
make qcommit MSG="description"       # Commit formaté
make pr                              # Push + lien PR
```

---

## 👥 Équipe

Voir [team/CONTACTS.md](team/CONTACTS.md) pour la liste des contacts.

---

## 📄 License

Propriétaire - Tous droits réservés
