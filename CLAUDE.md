# 🤖 Guide Claude - LibreShelf

Ce document sert de référence pour Claude lors des sessions de développement sur LibreShelf.

## 📋 Résumé du projet

**LibreShelf** est une application Android open source pour lire et organiser des livres numériques (EPUB, PDF, CBZ/CBR).

- **Langage** : Kotlin
- **UI** : Jetpack Compose + Material Design 3
- **Architecture** : MVVM avec Hilt DI
- **Base de données** : Room
- **Min SDK** : 26 (Android 8.0)
- **Target SDK** : 35 (Android 15)

## 🏗️ Architecture clé

### Structure des packages
```
com.libreshelf/
├── data/                    # Couche de données
│   ├── local/              # Room Database + DAOs
│   ├── model/              # Entités (Book, Library, etc.)
│   └── repository/         # Repositories
├── domain/                  # Logique métier
│   ├── reader/             # Lecteurs EPUB/PDF/Comic
│   └── network/            # Clients réseau SMB/WebDAV/FTP
├── presentation/            # UI
│   ├── screens/            # Écrans Compose
│   ├── navigation/         # Navigation
│   └── theme/              # Material Design 3
├── di/                      # Injection de dépendances
└── util/                    # Utilitaires
```

### Entités principales

| Entité | Description | Fichier |
|--------|-------------|---------|
| `Library` | Bibliothèque de livres | `data/model/Library.kt` |
| `Book` | Livre avec métadonnées | `data/model/Book.kt` |
| `Bookmark` | Marque-page | `data/model/Bookmark.kt` |
| `ReadingSession` | Session de lecture | `data/model/ReadingSession.kt` |
| `NetworkSource` | Source réseau (SMB/WebDAV/FTP) | `data/model/NetworkSource.kt` |

## ⚠️ Implémentations personnalisées (IMPORTANT)

### 1. EPUB Reader (`domain/reader/EpubReader.kt`)

**Pourquoi personnalisé** : La bibliothèque `epublib` n'est pas disponible sur Maven Central (erreur 401 depuis JitPack).

**Implémentation** :
- Utilise `ZipFile` (EPUB = fichier ZIP)
- Parse XML avec `XmlPullParser` (Android natif)
- Lit `META-INF/container.xml` → trouve fichier OPF
- Parse OPF pour métadonnées, manifest, spine

**Limitations** :
- Pas de table des matières NCX (seulement spine)
- Pas de rendu HTML/CSS
- Support EPUB 2 principalement

**Documentation** : Voir `EPUB_IMPLEMENTATION.md`

### 2. WebDAV Client (`domain/network/WebDavClient.kt`)

**Pourquoi personnalisé** : La bibliothèque `sardine-android` n'est pas disponible sur Maven Central.

**Implémentation** :
- Utilise `OkHttp` (déjà dans le projet)
- Méthodes WebDAV : PROPFIND, GET, OPTIONS
- Parse XML WebDAV avec `XmlPullParser`
- Authentification HTTP Basic

**Compatible avec** :
- Nextcloud
- Owncloud
- Serveurs WebDAV standards

**Documentation** : Voir `WEBDAV_IMPLEMENTATION.md`

## 🔑 Décisions techniques importantes

### Dépendances
- ✅ **Pas de JitPack** : Toutes les dépendances viennent de Google ou Maven Central
- ✅ **Implémentations natives** : EPUB et WebDAV utilisent les outils Android natifs
- ✅ **PDFBox Android** : Pour le PDF (stable sur Maven Central)
- ✅ **Coil 3** : Pour le chargement d'images

### Patterns utilisés
- **Repository Pattern** : Abstraction de l'accès aux données
- **Dependency Injection** : Hilt pour toutes les dépendances
- **StateFlow** : Pour l'état UI réactif
- **Flow** : Pour les données de Room

### Navigation
- **Jetpack Navigation Compose**
- Routes définies dans `presentation/navigation/Screen.kt`
- NavGraph dans `presentation/navigation/NavGraph.kt`

## 📝 Conventions de code

### Kotlin
- Style officiel Kotlin
- Noms explicites (pas d'abréviations obscures)
- Maximum 100-120 caractères par ligne
- Commentaires pour la logique complexe

### Compose
- State hoisting vers les ViewModels
- `remember` et `derivedStateOf` pour les calculs
- Composables réutilisables dans `presentation/components/`

### ViewModels
- Un ViewModel par écran principal
- StateFlow pour l'état UI
- Pas de référence au Context
- Nettoyage dans `onCleared()`

### Git
Format des commits :
```
type(scope): description

feat: Nouvelle fonctionnalité
fix: Correction de bug
docs: Documentation
refactor: Refactoring
test: Tests
chore: Maintenance
```

## 🚀 Commandes utiles

### Build
```bash
./gradlew assembleDebug           # Compiler APK debug
./gradlew assembleRelease         # Compiler APK release
./gradlew clean                   # Nettoyer
./gradlew build                   # Build complet
```

### Tests
```bash
./gradlew test                    # Tests unitaires
./gradlew connectedAndroidTest    # Tests d'intégration
```

### Dépendances
```bash
./gradlew dependencies            # Voir l'arbre des dépendances
./gradlew --refresh-dependencies  # Rafraîchir
```

### Git
```bash
git log --oneline                 # Historique
git status --short                # Statut court
```

## 🎯 Prochaines étapes prioritaires

### 1. Interface du lecteur (URGENT)
**Fichier** : À créer `presentation/screens/reader/ReaderScreen.kt`

**Besoins** :
- Affichage du contenu (WebView pour EPUB/HTML, Image pour PDF/Comic)
- Contrôles : Page précédente/suivante
- Barre de progression
- Menu : TOC, signets, paramètres
- Gestion des thèmes (clair, sombre, sépia)

**ViewModel** : Déjà créé dans `presentation/screens/reader/ReaderViewModel.kt`

### 2. Import de fichiers
**Fichier** : À créer `presentation/screens/import/ImportScreen.kt`

**Besoins** :
- Sélecteur de fichiers (Storage Access Framework)
- Support drag & drop
- Import multiple
- Extraction métadonnées automatique
- Copie vers stockage interne

### 3. Extraction de métadonnées
**Fichier** : À créer `domain/metadata/MetadataExtractor.kt`

**Besoins** :
- Extraire titre, auteur depuis EPUB/PDF
- Générer/extraire couverture
- Détection de série et numéro
- Support ISBN

### 4. Tests
**À créer** :
- Tests unitaires pour ViewModels
- Tests pour Repositories
- Tests pour lecteurs EPUB/PDF
- Tests UI Compose

### 5. Icônes
**Manquant** : Icônes de l'application dans `res/mipmap-*/`

**Besoin** :
- ic_launcher.png (48dp, 72dp, 96dp, 144dp, 192dp)
- ic_launcher_round.png
- ic_launcher_foreground.xml (vecteur)
- ic_launcher_background.xml

## ⚙️ Configuration

### local.properties (à créer si nécessaire)
```properties
sdk.dir=/path/to/Android/Sdk
```

### Variables d'environnement
```bash
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin
export PATH=$PATH:$ANDROID_HOME/platform-tools
```

## 🐛 Points d'attention

### 1. Pas de bibliothèques JitPack
**JAMAIS** ajouter de dépendances depuis JitPack. Elles causent des erreurs 401.

### 2. Gestion de la mémoire
- Les lecteurs (EPUB, PDF, Comic) doivent être fermés dans `onCleared()`
- Éviter les fuites mémoire avec les Bitmaps (PDF/Comic)
- Utiliser `remember` dans Compose avec précaution

### 3. Permissions
- READ_EXTERNAL_STORAGE (Android ≤ 12)
- READ_MEDIA_IMAGES/VIDEO/AUDIO (Android ≥ 13)
- INTERNET pour les sources réseau
- Utiliser Storage Access Framework quand possible

### 4. Threading
- Room : Automatique avec Flow/suspend
- Lecteurs : Toujours en background (Dispatchers.IO)
- Network : Toujours en background
- UI : Seulement sur Main thread

## 📚 Fichiers de référence importants

| Fichier | Description |
|---------|-------------|
| `app/build.gradle.kts` | Configuration Gradle, dépendances |
| `MainActivity.kt` | Point d'entrée de l'app |
| `LibreShelfApplication.kt` | Application class (Hilt) |
| `LibreShelfDatabase.kt` | Configuration Room |
| `NavGraph.kt` | Configuration navigation |
| `Screen.kt` | Définition des routes |

## 🔍 Debug

### Logs utiles
```kotlin
// Dans les ViewModels et repositories
.catch { e ->
    e.printStackTrace()
    Log.e("TAG", "Error", e)
}
```

### Compose Preview
```kotlin
@Preview(showBackground = true)
@Composable
fun MyScreenPreview() {
    LibreShelfTheme {
        MyScreen()
    }
}
```

## 📖 Documentation à consulter

1. **README.md** - Vue d'ensemble
2. **QUICK_START.md** - Démarrage rapide
3. **ARCHITECTURE.md** - Architecture détaillée
4. **EPUB_IMPLEMENTATION.md** - Détails EPUB
5. **WEBDAV_IMPLEMENTATION.md** - Détails WebDAV
6. **CONTRIBUTING.md** - Guide contribution

## 💡 Astuces pour Claude

### Lors de l'ajout de dépendances
1. ✅ Vérifier qu'elle existe sur Maven Central ou Google
2. ❌ JAMAIS utiliser JitPack
3. ✅ Préférer les implémentations natives si possible

### Lors de l'ajout de features
1. Créer les modèles de données d'abord
2. Créer le Repository/DAO
3. Créer le ViewModel avec StateFlow
4. Créer le Composable UI
5. Ajouter la navigation

### Lors du refactoring
1. Respecter l'architecture MVVM
2. Ne pas mélanger les couches
3. Garder les ViewModels sans Context
4. Utiliser les repositories pour les données

### Pour les lecteurs (EPUB/PDF/Comic)
1. Toujours fermer les ressources
2. Gestion d'erreur robuste (try/catch)
3. Threading approprié (IO dispatcher)
4. Pas de stockage en mémoire prolongé

---

**Ce fichier doit être mis à jour** quand des décisions architecturales importantes sont prises ou que de nouvelles conventions sont établies.

Dernière mise à jour : 2025-10-20
