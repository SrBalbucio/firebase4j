# Módulo servidor e App Check (`firebase4j-server`)

O artefacto **`firebase4j-server`** contém código que só deve correr em **ambientes confiáveis** (servidor, backend, ferramenta administrativa interna). Usa uma **chave JSON de conta de serviço** Google para:

1. Obter um **access token** OAuth2 com scope Firebase.
2. Assinar um **JWT** próprio para o serviço App Check.
3. Chamar **`exchangeCustomToken`** na API Firebase App Check e obter um **`AppCheckToken`**.

O cliente desktop típico usa apenas **`firebase4j-core`**; adicionem **`firebase4j-server`** quando precisarem de emitir tokens App Check no servidor.

## Dependência Maven

```xml
<dependency>
  <groupId>balbucio.org.firebase4j</groupId>
  <artifactId>firebase4j-server</artifactId>
  <version>0.1.0</version>
</dependency>
```

Transitivemente inclui **`firebase4j-core`**.

## Pacotes

| Pacote | Conteúdo |
|--------|-----------|
| `balbucio.org.firebase4j.server` | `FirebaseServerOptions`, `FirebaseServerAppCheck` |
| `balbucio.org.firebase4j.server.impl` | `ServerAppCheckV1` |
| `balbucio.org.firebase4j.server.model` | `AppCheckToken` (`token`, `ttl`) |

## `FirebaseServerOptions`

Combina:

- Dados do **cliente Web** já presentes em **`FirebaseOptions`** (`projectId`, `appId`, `Gson`).
- Uma **conta de serviço** (JSON Google) para credenciais e chave RSA.

### Fabricação

```java
FirebaseOptions client = FirebaseOptions.fromJsonFile(new File("firebase-web-config.json"));

FirebaseServerOptions serverOpts = FirebaseServerOptions.fromClientAndServiceAccount(
    client,
    new File("service-account.json")
);
```

Sobrecargas aceitam também **`InputStream`** ou **`JSONObject`** para o JSON da conta de serviço.

### Campos expostos (getters)

- **`gson`**, **`projectId`**, **`appId`**
- **`serviceAccountJson`** — objecto `JSONObject` da chave
- **`serviceAccountCredentials`** — `GoogleCredentials` com scope Firebase
- **`privateKey`** — `RSAPrivateKey` derivada da chave PEM no JSON

## `FirebaseServerAppCheck`

```java
FirebaseServerAppCheck appCheck = FirebaseServerAppCheck.newInstance(serverOpts);

// JWT interno usado no fluxo de troca (cache com TTL ~1 h)
String jwt = appCheck.getJWTToken();

// Troca por token App Check (limitedUse conforme API Google)
Optional<AppCheckToken> token = appCheck.createToken(false);
```

- **`getJWTToken()`** — gera ou reutiliza o JWT assinado enquanto válido.
- **`createToken(boolean limitedUse)`** — POST à API Google; em caso de erro HTTP devolve **`Optional.empty()`**.

## Segurança

- O ficheiro **service-account.json** concede privilégios elevados; **nunca** o incluam em builds de aplicação desktop distribuída aos utilizadores finais.
- Rotacionem chaves e restrinjam IAM na Google Cloud conforme as boas práticas do projeto.

## Ligações

- [Introdução](getting-started.md)  
- [Migração](migration.md) (API antiga `withServiceAccount` em `FirebaseOptions`)
