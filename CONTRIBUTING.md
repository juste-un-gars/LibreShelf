# Contribuer à LibreShelf

Merci de votre intérêt pour contribuer à LibreShelf ! Ce document fournit des directives pour contribuer au projet.

## 🤝 Comment contribuer

### Signaler des bugs

Si vous trouvez un bug, veuillez ouvrir une issue avec :
- Une description claire du problème
- Les étapes pour reproduire le bug
- Le comportement attendu vs le comportement actuel
- Votre version d'Android et de l'application
- Des captures d'écran si pertinent

### Proposer de nouvelles fonctionnalités

Pour proposer une nouvelle fonctionnalité :
1. Vérifiez qu'elle n'a pas déjà été proposée dans les issues
2. Ouvrez une issue avec le tag "enhancement"
3. Décrivez la fonctionnalité et son intérêt
4. Attendez les retours avant de commencer à coder

### Soumettre des modifications

1. **Fork** le repository
2. **Créez** une branche pour votre fonctionnalité
   ```bash
   git checkout -b feature/ma-nouvelle-fonctionnalite
   ```
3. **Codez** en suivant les conventions du projet
4. **Testez** vos modifications
5. **Commit** avec des messages clairs
   ```bash
   git commit -m "feat: ajout du support pour le format XYZ"
   ```
6. **Push** vers votre fork
   ```bash
   git push origin feature/ma-nouvelle-fonctionnalite
   ```
7. **Ouvrez** une Pull Request

## 📝 Conventions de code

### Kotlin
- Suivez les [conventions officielles Kotlin](https://kotlinlang.org/docs/coding-conventions.html)
- Utilisez des noms de variables et fonctions explicites
- Commentez le code complexe
- Limitez les fonctions à une responsabilité

### Jetpack Compose
- Utilisez des Composables réutilisables
- Préférez les `remember` et `derivedStateOf` pour les calculs
- Suivez les bonnes pratiques de state hoisting
- Utilisez `LaunchedEffect` et `DisposableEffect` appropriément

### Architecture
- Respectez l'architecture MVVM
- Les ViewModels ne doivent pas contenir de références au Context
- Utilisez les repositories pour l'accès aux données
- Les Use Cases sont recommandés pour la logique complexe

### Git
Format des messages de commit :
```
type(scope): description courte

[corps du message optionnel]

[footer optionnel]
```

Types :
- `feat`: Nouvelle fonctionnalité
- `fix`: Correction de bug
- `docs`: Documentation
- `style`: Formatage, point-virgules manquants, etc.
- `refactor`: Refactoring
- `test`: Ajout de tests
- `chore`: Maintenance

## 🧪 Tests

- Ajoutez des tests unitaires pour la logique métier
- Ajoutez des tests d'intégration pour les repositories
- Testez sur plusieurs versions d'Android si possible

## 📚 Documentation

- Documentez les nouvelles fonctionnalités dans le README
- Ajoutez des commentaires KDoc pour les fonctions publiques
- Mettez à jour CHANGELOG.md

## ✅ Checklist avant Pull Request

- [ ] Le code compile sans erreur
- [ ] Le code suit les conventions du projet
- [ ] Les tests passent
- [ ] La documentation est à jour
- [ ] Les messages de commit sont clairs
- [ ] Pas de conflits avec la branche main

## 🙋 Questions ?

N'hésitez pas à ouvrir une issue pour toute question !

Merci de contribuer à LibreShelf ! 🎉
