# Introdução e requisitos

## O que é o Firebase4J

Biblioteca Java que expõe operações de **Firebase Authentication** e **Cloud Firestore** através das APIs REST públicas documentadas pelo Google. Útil para:

- Aplicações **desktop** (Swing, JavaFX, SWT, etc.)
- Serviços ou ferramentas na JVM que não usam Android

Não substitui o Admin SDK nem os SDKs nativos; o modelo é **HTTP + JSON**, com tipos próprios (`User`, `DocumentSnapshot`, etc.).

## Requisitos

- **Java 8** ou superior (conforme `maven.compiler` do projeto).
- **Maven 3.6+** para construir o reactor multi-módulo.
- Projeto Firebase criado na [Consola Firebase](https://console.firebase.google.com/) com **Authentication** e, se necessário, **Firestore** ativos.

## Dependências Maven

### Apenas cliente (desktop)

Inclui Auth, Firestore, persistência e modelos.

```xml
<dependency>
  <groupId>balbucio.org.firebase4j</groupId>
  <artifactId>firebase4j-core</artifactId>
  <version>0.1.0</version>
</dependency>
```

Substitua `0.1.0` pela versão publicada no vosso repositório Maven.

### Cliente + funcionalidades de servidor (App Check)

O artefacto `firebase4j-server` traz **App Check** (troca de token com conta de serviço) e depende transitivamente de `firebase4j-core`.

```xml
<dependency>
  <groupId>balbucio.org.firebase4j</groupId>
  <artifactId>firebase4j-server</artifactId>
  <version>0.1.0</version>
</dependency>
```

Não é necessário declarar `firebase4j-core` à parte, exceto se quiserem fixar versões explicitamente com `<dependencyManagement>`.

## Primeiros passos (fluxo mínimo)

1. Descarregar ou exportar o JSON de configuração da **app Web** do Firebase (os mesmos campos que aparecem na snippet JavaScript).
2. Carregar opções com [`FirebaseOptions`](configuration.md) (ficheiro, classpath ou `JSONObject`).
3. Criar `FirebaseAuth auth = FirebaseAuth.newInstance(options)` e fazer login (`signIn`, `signUp` ou `signInAnonymously`).
4. Opcional: configurar [persistência](persistence.md) para restaurar o utilizador na próxima execução.
5. Para Firestore: `Firestore.newInstance(options, "(default)", auth)` e usar `getDocument` / `updateDocument` conforme [Firestore](firestore.md).

## Estrutura de pacotes (núcleo)

| Pacote | Descrição |
|--------|-----------|
| `balbucio.org.firebase4j` | `FirebaseOptions`, `FirebaseAuth`, `Firestore` |
| `balbucio.org.firebase4j.impl.auth` | Implementação atual da Auth (`AuthV1`) |
| `balbucio.org.firebase4j.impl.firestore` | Implementação atual do Firestore (`FirestoreV1`) |
| `balbucio.org.firebase4j.model` | `User`, `UserDetails`, `DocumentSnapshot`, … |
| `balbucio.org.firebase4j.persistent` | Implementações de `FirebasePersistent` |
| `balbucio.org.firebase4j.exception` | Exceções específicas da API |

Módulo servidor: `balbucio.org.firebase4j.server` e `balbucio.org.firebase4j.server.model` — ver [server-app-check.md](server-app-check.md).

## Próximo passo

[Configuração com `FirebaseOptions`](configuration.md)
