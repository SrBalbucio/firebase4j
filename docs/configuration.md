# Configuração (`FirebaseOptions`)

Toda a configuração “de cliente” do projeto Firebase está concentrada em **`FirebaseOptions`**. Não inclui chaves de conta de serviço; para isso use o módulo [`firebase4j-server`](server-app-check.md) e `FirebaseServerOptions`.

## Origem dos dados

Os valores coincidem com o objeto de configuração Web que a consola Firebase mostra para uma app registada (apiKey, authDomain, projectId, appId, etc.).

### Campos principais

| Campo | Origem típica no JSON da consola |
|-------|-----------------------------------|
| `apiKey` | `apiKey` |
| `authDomain` | `authDomain` |
| `databaseURL` | `databaseURL` ou `databaseUrl` (opcional) |
| `projectId` | `projectId` ou `id` |
| `storageBucket` | `storageBucket` |
| `messagingSenderId` | `messagingSenderId` |
| `appId` | `appId` |
| `measurementId` | `measurementId` |
| `emailTest` | opcional; valor por omissão interno se omitido |

O construtor interno também recebe uma instância **`Gson`** (serialização usada em vários fluxos).

## Formas de carregar

### Ficheiro JSON no disco

```java
FirebaseOptions options = FirebaseOptions.fromJsonFile(new File("firebase-web-config.json"));
```

### Classpath (recurso no JAR)

```java
FirebaseOptions options = FirebaseOptions.fromClasspath("/firebase-web-config.json");
```

### `InputStream`

```java
try (InputStream in = Files.newInputStream(path)) {
    FirebaseOptions options = FirebaseOptions.fromInputStream(in);
}
```

### `JSONObject` programático

```java
JSONObject json = new JSONObject();
// preencher chaves como na consola Firebase
FirebaseOptions options = FirebaseOptions.fromJSON(json);
```

## Gson

Por omissão, `fromJSON` usa um `GsonBuilder` com serialização de mapas complexos e `nulls` serializados. Para cenários avançados podem construir `FirebaseOptions` manualmente com um `Gson` próprio via construtor completo (ver código-fonte).

## Persistência de sessão

O campo **`persistent`** (tipo [`FirebasePersistent`](persistence.md)) determina onde o utilizador atual é guardado. Por omissão usa **`EmptyPersistent`** (nada é gravado em disco).

```java
options.setPersistent(FirebasePersistent.fromFile(new File("session.dat")));
// ou MVStore:
options.setPersistent(FirebasePersistent.fromMVStore(new File("session.mv.db")));
```

## Modo `adminSdk`

**`adminSdk`** (boolean): quando `true`, `FirebaseAuth#changeCurrentUser` não persiste nem valida o “já existe utilizador” da mesma forma (atalho para cenários especiais). Na maioria das apps desktop deve permanecer `false`.

## PATCH no `HttpURLConnection`

Na inicialização estática da classe, o código tenta registar o método HTTP **`PATCH`** no `HttpURLConnection` do JDK (necessário para atualizações Firestore em algumas VMs). Em JDKs recentes o reflexo sobre o campo `modifiers` pode falhar silenciosamente; nesse caso o comportamento depende da versão do Java — em caso de falhas estranhas em `PATCH`, validar a versão do JDK e relatório de erros no repositório.

## Segurança

- O JSON Web da app contém **apiKey** exposta no cliente — isto é esperado no modelo Firebase Web; a segurança vem das **regras** Firestore e das políticas Auth na consola.
- **Nunca** embutir chaves de **conta de serviço** na mesma configuração que vai para a app desktop; use apenas no servidor com `firebase4j-server`.

## Ligações

- [Autenticação](authentication.md)  
- [Firestore](firestore.md)  
- [Persistência](persistence.md)
