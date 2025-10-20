# 🚀 Démarrage rapide - LibreShelf

## ✅ État du projet

Le projet LibreShelf est **entièrement configuré** et prêt à être compilé dans Android Studio.

### Ce qui a été créé

- ✅ **63+ fichiers sources** (Kotlin, XML, Markdown)
- ✅ **Architecture MVVM complète** avec Hilt DI
- ✅ **Base de données Room** (5 entités)
- ✅ **Interface Jetpack Compose** (Material Design 3)
- ✅ **Lecteurs personnalisés** (EPUB, PDF, CBZ/CBR)
- ✅ **Support réseau** (SMB, WebDAV, FTP)
- ✅ **Documentation complète**
- ✅ **Configuration Git** avec commits propres

## 📋 Prérequis

Pour compiler LibreShelf, vous avez besoin de :

1. **Android Studio** Hedgehog (2023.1.1) ou plus récent
   - Téléchargement : https://developer.android.com/studio

2. **Android SDK** (installé automatiquement avec Android Studio)
   - Min SDK: API 26 (Android 8.0)
   - Target SDK: API 35 (Android 15)
   - Compile SDK: API 35

3. **JDK 17** (inclus avec Android Studio)

## 🔨 Compilation dans Android Studio

### Étape 1 : Ouvrir le projet

```bash
# Si ce n'est pas déjà fait, clonez ou naviguez vers le projet
cd /home/franck/LibreShelf

# Ouvrez Android Studio et sélectionnez "Open"
# Puis naviguez vers ce dossier
```

### Étape 2 : Sync Gradle

1. Android Studio va automatiquement détecter le projet
2. Cliquez sur "Sync Project with Gradle Files" si demandé
3. Attendez que toutes les dépendances soient téléchargées

### Étape 3 : Compiler

**Via l'interface** :
- Menu : `Build > Make Project` (Ctrl+F9)
- Ou : `Build > Build Bundle(s) / APK(s) > Build APK(s)`

**Via le terminal** :
```bash
./gradlew assembleDebug
```

L'APK sera généré dans : `app/build/outputs/apk/debug/app-debug.apk`

## 🖥️ Sans Android Studio (ligne de commande)

Si vous voulez compiler sans Android Studio :

### 1. Installer le SDK Android

```bash
# Sur Linux (Fedora/RHEL)
sudo dnf install android-tools

# Ou télécharger les command line tools
wget https://dl.google.com/android/repository/commandlinetools-linux-latest.zip
unzip commandlinetools-linux-latest.zip -d android-sdk
```

### 2. Configurer ANDROID_HOME

```bash
# Ajouter dans ~/.bashrc ou ~/.bash_profile
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin
export PATH=$PATH:$ANDROID_HOME/platform-tools
```

### 3. Installer les SDK nécessaires

```bash
sdkmanager "platforms;android-35"
sdkmanager "build-tools;35.0.0"
sdkmanager "platform-tools"
```

### 4. Compiler

```bash
./gradlew assembleDebug
```

## 🐛 Résolution de problèmes

### "SDK location not found"

**Solution** : Créer `local.properties` :

```bash
echo "sdk.dir=/chemin/vers/Android/Sdk" > local.properties
```

Ou définir `ANDROID_HOME` :
```bash
export ANDROID_HOME=$HOME/Android/Sdk
```

### "Could not resolve dependencies"

**Solution** : Vérifier votre connexion internet et sync Gradle :
```bash
./gradlew --refresh-dependencies
```

### "Gradle sync failed"

**Solution** : Nettoyer et rebuild :
```bash
./gradlew clean
./gradlew build
```

## 📦 Publier sur GitHub

Le projet a déjà été initialisé avec Git. Pour publier :

```bash
# Ajouter le remote (remplacez par votre URL)
git remote add origin https://github.com/juste-un-gars/LibreShelf.git

# Pousser vers GitHub
git push -u origin main
```

## 📱 Installer sur un appareil

### Via ADB

```bash
# Activer le débogage USB sur votre appareil
# Puis :
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Via fichier APK

1. Transférer l'APK sur votre appareil
2. Activer "Sources inconnues" dans les paramètres
3. Ouvrir l'APK et installer

## 🎯 Prochaines étapes

Le projet compile mais certaines fonctionnalités nécessitent encore du développement :

1. **Interface du lecteur** : Créer l'UI complète avec contrôles
2. **Import de fichiers** : Implémenter le sélecteur de fichiers
3. **Extraction de métadonnées** : Automatiser l'extraction depuis les livres
4. **Icônes** : Ajouter les icônes de l'application (`ic_launcher`)
5. **Tests** : Ajouter tests unitaires et d'intégration

Consultez le fichier [ROADMAP.md](ROADMAP.md) pour la liste complète.

## 📚 Documentation

- [README.md](README.md) - Vue d'ensemble du projet
- [ARCHITECTURE.md](ARCHITECTURE.md) - Architecture technique
- [CONTRIBUTING.md](CONTRIBUTING.md) - Guide de contribution
- [BUILD_INSTRUCTIONS.md](BUILD_INSTRUCTIONS.md) - Instructions détaillées
- [EPUB_IMPLEMENTATION.md](EPUB_IMPLEMENTATION.md) - Détails sur le lecteur EPUB
- [WEBDAV_IMPLEMENTATION.md](WEBDAV_IMPLEMENTATION.md) - Détails sur WebDAV

## 💬 Support

Pour toute question :
- Ouvrir une [issue sur GitHub](https://github.com/juste-un-gars/LibreShelf/issues)
- Consulter la [documentation](README.md)

---

**Le projet est prêt !** Il suffit de l'ouvrir dans Android Studio et de cliquer sur Run. 🎉
