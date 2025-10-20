# Instructions de compilation - LibreShelf

Ce document décrit comment compiler LibreShelf depuis les sources.

## Prérequis

### Logiciels requis
- **Android Studio** Hedgehog (2023.1.1) ou plus récent
- **JDK 17** (Java Development Kit)
- **Git** pour cloner le repository

### Configuration Android SDK
- **Min SDK**: API 26 (Android 8.0)
- **Target SDK**: API 35 (Android 15)
- **Compile SDK**: API 35

## Étape 1 : Cloner le repository

```bash
git clone https://github.com/juste-un-gars/LibreShelf.git
cd LibreShelf
```

## Étape 2 : Ouvrir le projet dans Android Studio

1. Lancez Android Studio
2. Sélectionnez "Open an Existing Project"
3. Naviguez vers le dossier LibreShelf cloné
4. Attendez que Gradle synchronise le projet

## Étape 3 : Configuration (optionnel)

### Clés de signature (pour release)
Pour créer une version de release signée, créez un fichier `keystore.properties` à la racine du projet :

```properties
storePassword=votre_mot_de_passe
keyPassword=votre_mot_de_passe
keyAlias=votre_alias
storeFile=chemin/vers/votre/keystore.jks
```

## Étape 4 : Compilation

### Depuis Android Studio

1. Sélectionnez `Build > Build Bundle(s) / APK(s) > Build APK(s)`
2. L'APK sera généré dans `app/build/outputs/apk/debug/`

### En ligne de commande

#### APK Debug
```bash
./gradlew assembleDebug
```

#### APK Release
```bash
./gradlew assembleRelease
```

#### Bundle Android (AAB)
```bash
./gradlew bundleRelease
```

## Étape 5 : Installation

### Depuis Android Studio
1. Connectez votre appareil Android ou lancez un émulateur
2. Cliquez sur `Run > Run 'app'`

### Depuis ADB
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Tests

### Lancer les tests unitaires
```bash
./gradlew test
```

### Lancer les tests d'instrumentation
```bash
./gradlew connectedAndroidTest
```

## Résolution de problèmes

### Problème : Gradle sync failed
**Solution** : Assurez-vous d'avoir la bonne version de JDK (17) et nettoyez le projet :
```bash
./gradlew clean
```

### Problème : SDK not found
**Solution** : Vérifiez que le SDK Android est correctement installé. Dans Android Studio :
- Allez dans `File > Project Structure > SDK Location`
- Assurez-vous que le chemin du SDK est correct

### Problème : Out of memory during build
**Solution** : Augmentez la mémoire allouée à Gradle dans `gradle.properties` :
```properties
org.gradle.jvmargs=-Xmx4096m
```

## Nettoyage

Pour nettoyer les fichiers de build :
```bash
./gradlew clean
```

## Contribution

Après avoir compilé avec succès, consultez [CONTRIBUTING.md](CONTRIBUTING.md) pour savoir comment contribuer au projet.

## Support

Si vous rencontrez des problèmes de compilation, ouvrez une issue sur GitHub avec :
- Votre version d'Android Studio
- Votre version de JDK
- Le message d'erreur complet
- Les étapes que vous avez suivies
