# Migração: artefacto único `firebase4j` → módulos 0.1.x

Versões antigas publicavam um único JAR **`firebase4j`** (por exemplo `0.0.3.ALPHA-01`). A partir da reorganização multi-módulo:

| Antes | Depois |
|-------|--------|
| `artifactId`: `firebase4j` | `firebase4j-core` (cliente desktop) |
| Mesmo JAR com Auth + App Check servidor | `firebase4j-server` opcional (App Check com conta de serviço) |
| Versão exemplo `0.0.x` | Versão exemplo **`0.1.0`** (consultar POM actual) |

## Passos para consumidores Maven

1. Substituir a dependência:

   ```xml
   <!-- Antigo -->
   <artifactId>firebase4j</artifactId>

   <!-- Novo (desktop) -->
   <artifactId>firebase4j-core</artifactId>
   ```

2. Actualizar o número de versão para a linha **0.1.x** ou superior publicada.

3. Se usavam **App Check no servidor** com `FirebaseOptions#withServiceAccount`:
   - Adicionar dependência **`firebase4j-server`**.
   - Remover chamadas a `withServiceAccount` em **`FirebaseOptions`** (já não existem).
   - Criar **`FirebaseServerOptions.fromClientAndServiceAccount(FirebaseOptions, File|InputStream|JSONObject)`**.
   - Usar **`FirebaseServerAppCheck.newInstance(FirebaseServerOptions)`** em pacotes **`balbucio.org.firebase4j.server`**.
   - Actualizar imports de **`AppCheckToken`** para **`balbucio.org.firebase4j.server.model.AppCheckToken`**.

## Compatibilidade de código cliente

- **`FirebaseAuth.newInstance(FirebaseOptions)`**, **`Firestore.newInstance(...)`**, persistência e modelos principais mantêm-se no núcleo com os mesmos pacotes **`balbucio.org.firebase4j`**.

## Ligações

- [Introdução](getting-started.md)  
- [App Check servidor](server-app-check.md)
