# Implémentation EPUB personnalisée

## Pourquoi une implémentation personnalisée ?

La bibliothèque `epublib` n'était pas disponible de manière fiable sur les repositories Maven publics, causant des erreurs de build (`401 Unauthorized` depuis JitPack).

Plutôt que de dépendre d'une bibliothèque externe, LibreShelf implémente son propre lecteur EPUB en utilisant les outils intégrés d'Android.

## Format EPUB

EPUB est un format ouvert et standardisé :

- **Structure** : Un fichier ZIP contenant :
  - `META-INF/container.xml` : Pointe vers le fichier OPF
  - `*.opf` (Open Packaging Format) : Métadonnées et structure du livre
  - Fichiers XHTML/HTML : Contenu des chapitres
  - CSS, images, polices : Ressources

### Structure typique

```
livre.epub (ZIP)
├── META-INF/
│   └── container.xml       # Indique l'emplacement du fichier OPF
├── OEBPS/
│   ├── content.opf         # Métadonnées, manifest, spine
│   ├── toc.ncx            # Table des matières
│   ├── chapter1.xhtml     # Chapitres en XHTML
│   ├── chapter2.xhtml
│   └── images/
│       └── cover.jpg
└── mimetype                # application/epub+zip
```

## Notre implémentation

### Classes principales

#### `EpubReader`
Le lecteur principal qui :
1. Ouvre le fichier ZIP
2. Lit `META-INF/container.xml` pour trouver le fichier OPF
3. Parse le fichier OPF pour extraire :
   - Métadonnées (titre, auteur, éditeur, etc.)
   - Manifest (liste des fichiers)
   - Spine (ordre de lecture des chapitres)
4. Fournit l'accès au contenu de chaque chapitre

#### `EpubBook`
Data class contenant :
- Métadonnées du livre
- Liste des chapitres
- Chemin du fichier

#### `ChapterInfo`
Information sur un chapitre :
- Index
- Titre
- Chemin (href) dans le ZIP

### Technologies utilisées

- **`java.util.zip.ZipFile`** : Lecture de l'archive ZIP
- **`android.util.Xml`** : Parser XML Android (XmlPullParser)
- **Standard Kotlin/Java** : Aucune dépendance externe

### Exemple d'utilisation

```kotlin
val epubReader = EpubReader(context)

// Charger un livre EPUB
val book = epubReader.loadBook("/path/to/book.epub")

// Accéder aux métadonnées
println("Titre: ${book.title}")
println("Auteur: ${book.author}")

// Lire un chapitre
val chapterContent = epubReader.getChapterContent(book, chapterIndex = 0)

// Table des matières
val toc = epubReader.getTableOfContents(book)

// Ne pas oublier de fermer
epubReader.closeBook()
```

## Limitations actuelles

1. **Navigation basique** : Utilise l'ordre du spine, pas la table des matières NCX
2. **Pas de styles** : Le contenu HTML/CSS est retourné brut
3. **Pas de DRM** : Ne supporte pas les EPUB avec DRM
4. **EPUB 2 principalement** : Optimisé pour EPUB 2, compatibilité EPUB 3 limitée

## Améliorations futures

- [ ] Parser le fichier NCX pour une vraie table des matières
- [ ] Support des métadonnées EPUB 3 (Dublin Core étendu)
- [ ] Extraction et affichage des images embarquées
- [ ] Rendu HTML avec styles CSS
- [ ] Support des notes de bas de page
- [ ] Gestion des sections et sous-chapitres
- [ ] Support EPUB 3 complet (Media Overlays, etc.)

## Références

- [EPUB Specification](http://idpf.org/epub)
- [EPUB 3 Overview](https://www.w3.org/publishing/epub3/)
- [OPF Specification](http://idpf.org/epub/20/spec/OPF_2.0.1_draft.htm)

## Avantages de cette approche

✅ **Aucune dépendance externe** : Moins de risques de conflits ou d'indisponibilité
✅ **Léger** : Seulement le nécessaire pour lire les EPUB
✅ **Maintenable** : Code simple et compréhensible
✅ **Performant** : Utilise les parsers natifs Android
✅ **Contrôle total** : Possibilité d'ajouter des fonctionnalités spécifiques

## Note de développement

Cette implémentation couvre les besoins basiques de lecture EPUB. Pour des fonctionnalités avancées (annotations, surlignage, recherche dans le texte, etc.), des améliorations seront nécessaires mais la base est solide et extensible.
