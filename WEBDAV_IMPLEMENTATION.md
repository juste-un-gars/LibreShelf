# Implémentation WebDAV personnalisée

## Pourquoi une implémentation personnalisée ?

La bibliothèque `sardine-android` n'était pas disponible sur les repositories Maven standards (uniquement sur JitPack), causant des problèmes de build.

LibreShelf implémente son propre client WebDAV en utilisant **OkHttp** (déjà inclus dans le projet).

## Protocole WebDAV

WebDAV (Web Distributed Authoring and Versioning) est une extension du protocole HTTP qui permet :
- La gestion de fichiers sur un serveur distant
- Lecture, écriture, copie, déplacement de fichiers
- Gestion de métadonnées

### Méthodes WebDAV utilisées

| Méthode | Description | Usage dans LibreShelf |
|---------|-------------|----------------------|
| **PROPFIND** | Récupère les propriétés d'une ressource | Lister les fichiers d'un dossier |
| **GET** | Télécharge un fichier | Télécharger un livre |
| **OPTIONS** | Vérifie les capacités du serveur | Tester la connexion |

## Notre implémentation

### Structure

```kotlin
class WebDavClient {
    // Créer un client OkHttp avec authentification
    private fun getClient(source: NetworkSource): OkHttpClient

    // Construire l'URL complète
    private fun getUrl(source: NetworkSource, path: String): String

    // Lister les fichiers (PROPFIND)
    fun listFiles(source: NetworkSource, path: String): List<NetworkFile>

    // Parser la réponse XML de PROPFIND
    private fun parseWebDavResponse(xml: String, baseUrl: String): List<NetworkFile>

    // Télécharger un fichier (GET)
    fun downloadFile(source: NetworkSource, remotePath: String, localFile: File): Boolean

    // Tester la connexion (OPTIONS)
    fun testConnection(source: NetworkSource): Boolean
}
```

### Requête PROPFIND

Pour lister les fichiers d'un répertoire, WebDAV utilise PROPFIND :

```xml
<?xml version="1.0" encoding="utf-8" ?>
<D:propfind xmlns:D="DAV:">
    <D:prop>
        <D:displayname/>
        <D:getcontentlength/>
        <D:getlastmodified/>
        <D:resourcetype/>
    </D:prop>
</D:propfind>
```

**Propriétés récupérées** :
- `displayname` : Nom du fichier
- `getcontentlength` : Taille du fichier
- `getlastmodified` : Date de modification
- `resourcetype` : Type (fichier ou dossier)

### Exemple de réponse

```xml
<?xml version="1.0" encoding="utf-8"?>
<D:multistatus xmlns:D="DAV:">
    <D:response>
        <D:href>/remote.php/dav/files/user/Books/</D:href>
        <D:propstat>
            <D:prop>
                <D:displayname>Books</D:displayname>
                <D:resourcetype>
                    <D:collection/>
                </D:resourcetype>
            </D:prop>
            <D:status>HTTP/1.1 200 OK</D:status>
        </D:propstat>
    </D:response>
    <D:response>
        <D:href>/remote.php/dav/files/user/Books/book.epub</D:href>
        <D:propstat>
            <D:prop>
                <D:displayname>book.epub</D:displayname>
                <D:getcontentlength>1234567</D:getcontentlength>
                <D:getlastmodified>Mon, 20 Oct 2025 14:00:00 GMT</D:getlastmodified>
            </D:prop>
            <D:status>HTTP/1.1 200 OK</D:status>
        </D:propstat>
    </D:response>
</D:multistatus>
```

## Authentification

WebDAV utilise l'authentification HTTP Basic :

```kotlin
val credentials = Credentials.basic(username, password)
request.header("Authorization", credentials)
```

## Serveurs supportés

Cette implémentation est compatible avec :

- ✅ **Nextcloud** (serveur de fichiers cloud open source)
- ✅ **Owncloud** (alternative à Nextcloud)
- ✅ **Apache mod_dav** (module WebDAV pour Apache)
- ✅ **nginx avec ngx_http_dav_module**
- ✅ **Serveurs WebDAV standards**

### Configuration Nextcloud

Pour Nextcloud, l'URL typique est :
```
https://votre-serveur.com/remote.php/dav/files/votre-username/
```

## Limitations actuelles

1. **Authentification basique uniquement** : Pas de support OAuth ou authentification avancée
2. **Pas de cache** : Chaque requête interroge le serveur
3. **Dates simplifiées** : Pas de parsing RFC 1123 complet
4. **Pas d'upload** : Seulement téléchargement (GET) pour le moment

## Améliorations futures

- [ ] Support PUT/POST pour l'upload de fichiers
- [ ] Support MKCOL pour créer des dossiers
- [ ] Support MOVE/COPY pour déplacer/copier
- [ ] Parse complet des dates RFC 1123
- [ ] Support des propriétés personnalisées
- [ ] Cache des listings de dossiers
- [ ] Support de l'authentification Digest
- [ ] Support des chunked transfers pour gros fichiers

## Avantages de cette approche

✅ **Aucune dépendance externe** : Utilise seulement OkHttp (déjà dans le projet)
✅ **Léger** : ~240 lignes de code
✅ **Maintenable** : Code simple et compréhensible
✅ **Compatible** : Fonctionne avec tous les serveurs WebDAV standards
✅ **Extensible** : Facile d'ajouter de nouvelles méthodes WebDAV

## Exemple d'utilisation

```kotlin
val webDavClient = WebDavClient()

val source = NetworkSource(
    type = NetworkType.WEBDAV,
    host = "cloud.example.com",
    port = 443,
    path = "remote.php/dav/files/username",
    username = "user",
    password = "pass"
)

// Lister les fichiers
val files = webDavClient.listFiles(source, "/Books")

// Télécharger un fichier
val success = webDavClient.downloadFile(
    source,
    "/Books/mybook.epub",
    File("/local/path/mybook.epub")
)

// Tester la connexion
val isConnected = webDavClient.testConnection(source)
```

## Références

- [RFC 4918 - WebDAV Specification](https://tools.ietf.org/html/rfc4918)
- [OkHttp Documentation](https://square.github.io/okhttp/)
- [Nextcloud WebDAV API](https://docs.nextcloud.com/server/latest/developer_manual/client_apis/WebDAV/)

## Tests recommandés

Pour tester votre implémentation WebDAV :

1. **Nextcloud local** : Utilisez Docker
   ```bash
   docker run -d -p 8080:80 nextcloud
   ```

2. **Serveur WebDAV de test** : `webdav://test.webdav.org/`

3. **nginx avec WebDAV** :
   ```nginx
   location /webdav {
       dav_methods PUT DELETE MKCOL COPY MOVE;
       dav_access user:rw group:rw all:r;
   }
   ```
