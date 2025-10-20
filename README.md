# 📚 LibreShelf

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android" alt="Platform">
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin" alt="Language">
  <img src="https://img.shields.io/badge/License-GPL--3.0-blue?style=for-the-badge" alt="License">
</p>

Une application Android gratuite et open source pour lire et organiser facilement vos livres numériques (EPUB, PDF, CBZ/CBR).

## 🎯 Description

LibreShelf est une application Android moderne qui combine une interface fluide basée sur Material Design 3, une gestion avancée des bibliothèques locales et distantes, et des options de lecture personnalisables. Parfaite pour les romans, BD, mangas, documents et webtoons.

## ✨ Fonctionnalités principales

### 📖 Formats supportés
- **EPUB** - Romans et livres numériques
- **PDF** - Documents et livres
- **CBZ/CBR** - Bandes dessinées et mangas

### 🗂️ Gestion de bibliothèques
- Création de bibliothèques multiples (BD, romans, documents, etc.)
- Import depuis stockage local ou carte SD
- Support des partages réseau (SMB, WebDAV, FTP/SFTP)
- Métadonnées et couvertures détectées automatiquement
- Classement par auteur, série, genre, date
- Recherche rapide et filtres avancés

### 📱 Lecture personnalisable
- Modes d'affichage variés (page unique, double page, défilement continu)
- Reprise automatique de lecture
- Signets et marque-pages
- Modes de thème (clair, sombre, sépia, nuit)
- Ajustement de luminosité et contraste
- Historique de lecture complet

### 🌐 Connectivité
- Support SMB/CIFS (Windows, NAS)
- WebDAV (Nextcloud, Owncloud)
- FTP / SFTP
- Cache intelligent pour lecture fluide

### 🎨 Interface moderne
- Material Design 3
- Thèmes personnalisables
- Support tablettes et Android TV
- Interface intuitive et fluide

## 🚀 Technologies utilisées

- **Kotlin** - Langage principal
- **Jetpack Compose** - UI moderne et déclarative
- **Material Design 3** - Design system
- **Room** - Base de données locale
- **Hilt** - Injection de dépendances
- **Coroutines & Flow** - Programmation asynchrone
- **MVVM Architecture** - Architecture propre et maintenable

### Bibliothèques tierces
- **PDFBox Android** - Lecture de PDF
- **epublib** - Lecture d'EPUB
- **Coil** - Chargement d'images
- **SMBj** - Support SMB/CIFS
- **Sardine** - Support WebDAV
- **Commons Net** - Support FTP

## 📦 Installation

### Depuis les releases
1. Téléchargez le dernier APK depuis la page [Releases](https://github.com/juste-un-gars/LibreShelf/releases)
2. Installez l'APK sur votre appareil Android

### Compilation depuis les sources

#### Prérequis
- Android Studio Hedgehog (2023.1.1) ou plus récent
- JDK 17
- SDK Android (min: API 26, target: API 35)

#### Étapes
```bash
# Cloner le repository
git clone https://github.com/juste-un-gars/LibreShelf.git
cd LibreShelf

# Ouvrir avec Android Studio ou compiler en ligne de commande
./gradlew assembleDebug

# L'APK sera généré dans app/build/outputs/apk/debug/
```

## 🤝 Contribution

Les contributions sont les bienvenues ! N'hésitez pas à :

1. Fork le projet
2. Créer une branche pour votre fonctionnalité (`git checkout -b feature/AmazingFeature`)
3. Commit vos changements (`git commit -m 'Add some AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrir une Pull Request

### Guidelines de contribution
- Suivez les conventions de code Kotlin
- Utilisez Compose pour les nouvelles interfaces
- Documentez les nouvelles fonctionnalités
- Testez vos changements

## 📝 Roadmap

- [ ] Support complet EPUB avec styles personnalisés
- [ ] Lecteur PDF avancé avec annotations
- [ ] Support formats additionnels (MOBI, AZW)
- [ ] Synchronisation cloud (Google Drive, Dropbox)
- [ ] Statistiques de lecture détaillées
- [ ] Mode lecture vocale (TTS)
- [ ] Support des annotations et notes
- [ ] Partage de bibliothèques entre appareils
- [ ] Widget lecture en cours
- [ ] Support Android Auto pour audiobooks

## 📄 Licence

Ce projet est sous licence GPL-3.0. Voir le fichier [LICENSE](LICENSE) pour plus de détails.

## 🙏 Remerciements

- [PDFBox Android](https://github.com/TomRoush/PdfBox-Android) pour le support PDF
- [epublib](https://github.com/psiegman/epublib) pour le support EPUB
- Toutes les bibliothèques open source utilisées dans ce projet

## 📧 Contact

Projet LibreShelf - [@juste-un-gars](https://github.com/juste-un-gars)

Lien du projet : [https://github.com/juste-un-gars/LibreShelf](https://github.com/juste-un-gars/LibreShelf)

---

**Note** : Ce projet est en développement actif. Certaines fonctionnalités peuvent être incomplètes ou en cours d'implémentation.
