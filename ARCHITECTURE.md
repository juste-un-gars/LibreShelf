# Architecture de LibreShelf

Ce document décrit l'architecture globale de l'application LibreShelf.

## Vue d'ensemble

LibreShelf utilise une architecture **MVVM (Model-View-ViewModel)** avec une séparation claire des responsabilités, organisée en plusieurs couches.

## Structure des packages

```
com.libreshelf/
├── data/                    # Couche de données
│   ├── local/              # Base de données locale (Room)
│   │   ├── LibreShelfDatabase.kt
│   │   ├── *Dao.kt         # Data Access Objects
│   │   └── Converters.kt
│   ├── model/              # Entités de la base de données
│   │   ├── Book.kt
│   │   ├── Library.kt
│   │   ├── Bookmark.kt
│   │   ├── ReadingSession.kt
│   │   └── NetworkSource.kt
│   └── repository/         # Repositories (abstraction des sources de données)
│       ├── LibraryRepository.kt
│       └── BookRepository.kt
│
├── domain/                  # Couche métier
│   ├── reader/             # Lecteurs de fichiers
│   │   ├── EpubReader.kt
│   │   ├── PdfReader.kt
│   │   └── ComicReader.kt
│   └── network/            # Clients réseau
│       ├── NetworkManager.kt
│       ├── SmbClient.kt
│       ├── WebDavClient.kt
│       └── FtpClient.kt
│
├── presentation/            # Couche de présentation (UI)
│   ├── screens/            # Écrans de l'application
│   │   ├── libraries/      # Liste des bibliothèques
│   │   ├── library/        # Détails d'une bibliothèque
│   │   ├── reader/         # Lecteur de livres
│   │   └── settings/       # Paramètres
│   ├── components/         # Composants UI réutilisables
│   ├── navigation/         # Navigation de l'app
│   └── theme/              # Thème Material Design 3
│
├── di/                      # Injection de dépendances (Hilt)
│   ├── DatabaseModule.kt
│   ├── NetworkModule.kt
│   └── ReaderModule.kt
│
└── util/                    # Utilitaires
    ├── FileUtils.kt
    └── PermissionUtils.kt
```

## Couches de l'architecture

### 1. Data Layer (Couche de données)

#### Responsabilités
- Gestion de la persistance des données (Room)
- Accès aux sources de données (locale, réseau)
- Mise en cache

#### Composants
- **Entities** : Modèles de données avec annotations Room
- **DAOs** : Interfaces définissant les requêtes SQL
- **Database** : Instance de la base de données Room
- **Repositories** : Abstraction qui coordonne l'accès aux données

### 2. Domain Layer (Couche métier)

#### Responsabilités
- Logique métier de l'application
- Cas d'utilisation
- Transformation des données

#### Composants
- **Readers** : Lecture et parsing des différents formats de fichiers
- **Network Clients** : Gestion des connexions réseau (SMB, WebDAV, FTP)
- **Use Cases** (futurs) : Cas d'utilisation spécifiques

### 3. Presentation Layer (Couche de présentation)

#### Responsabilités
- Interface utilisateur (Jetpack Compose)
- Gestion des événements utilisateur
- État de l'UI

#### Composants
- **Screens** : Écrans complets de l'application
- **ViewModels** : Gestion de l'état et de la logique de présentation
- **Composables** : Composants UI réutilisables
- **Navigation** : Gestion de la navigation entre écrans

## Flux de données

```
View (Composable)
    ↓ (événement utilisateur)
ViewModel
    ↓ (appel de méthode)
Repository
    ↓ (requête)
DAO / Network Client
    ↓ (données brutes)
Repository (transformation)
    ↓ (Flow/LiveData)
ViewModel (mise à jour de l'état)
    ↓ (StateFlow)
View (re-composition)
```

## Technologies utilisées

### UI
- **Jetpack Compose** : Framework UI déclaratif
- **Material Design 3** : Design system
- **Navigation Compose** : Navigation entre écrans

### Architecture
- **ViewModel** : Gestion de l'état
- **Flow/StateFlow** : Programmation réactive
- **Hilt** : Injection de dépendances

### Persistence
- **Room** : Base de données SQLite
- **DataStore** : Préférences clé-valeur

### Réseau
- **OkHttp** : Client HTTP
- **Coil** : Chargement d'images

### Lecture de fichiers
- **PDFBox Android** : Lecture de PDF
- **epublib** : Lecture d'EPUB
- **Zip/Archive** : Lecture de CBZ/CBR

## Principes de design

### 1. Séparation des responsabilités
Chaque couche a une responsabilité spécifique et ne doit pas empiéter sur les autres.

### 2. Inversion de dépendances
Les couches supérieures ne dépendent que des abstractions (interfaces), pas des implémentations.

### 3. Single Source of Truth
Les repositories sont la source unique de vérité pour les données.

### 4. Unidirectional Data Flow
Les données circulent dans un sens : Data → ViewModel → UI
Les événements circulent dans l'autre : UI → ViewModel → Data

## Patterns utilisés

### Repository Pattern
Abstraction de l'accès aux données, permettant de changer facilement la source de données.

### Observer Pattern
Utilisation de Flow pour observer les changements de données et mettre à jour l'UI automatiquement.

### Dependency Injection
Utilisation de Hilt pour injecter les dépendances, facilitant les tests et la modularité.

### Factory Pattern
Création d'objets complexes (lecteurs de fichiers, clients réseau) via des factories.

## Gestion de l'état

### État de l'UI
- Géré par les **ViewModels** via **StateFlow**
- Survit aux changements de configuration (rotation d'écran)
- Accessible via `collectAsState()` dans les Composables

### État persistant
- Stocké dans **Room Database**
- Observé via **Flow** dans les repositories
- Automatiquement mis à jour dans l'UI

## Tests

### Tests unitaires
- ViewModels (logique de présentation)
- Repositories (coordination des données)
- Use Cases (logique métier)

### Tests d'intégration
- DAOs (requêtes Room)
- Network Clients (connexions réseau)

### Tests UI
- Composables (interface utilisateur)
- Navigation (flux de navigation)

## Évolutions futures

### Prévues
- Migration vers Kotlin Multiplatform (Android + Desktop)
- Ajout de Use Cases explicites pour la logique métier complexe
- Module séparé pour chaque lecteur de format
- Support offline-first avec synchronisation

### Considérées
- GraphQL pour l'API (si backend ajouté)
- Paging 3 pour les grandes bibliothèques
- WorkManager pour les tâches en arrière-plan
- Compose Multiplatform pour partage de code UI
