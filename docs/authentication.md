# Autenticação (`FirebaseAuth`)

A API pública é a classe abstracta **`FirebaseAuth`**. A implementação atual é **`AuthV1`**, obtida através da fábrica estática.

## Criação

```java
FirebaseAuth auth = FirebaseAuth.newInstance(options);
```

Na construção, o Auth tenta **restaurar** o utilizador a partir de [`FirebasePersistent`](persistence.md), se configurado.

## Utilizador atual

- **`getCurrentUser()`** — `User` em sessão ou `null`.
- **`isLogged()`** — indica se existe utilizador atual.
- **`changeCurrentUser(User user, boolean overwrite)`** — define o utilizador atual e persiste-o. Se já existir utilizador e `overwrite == false`, lança **`AlreadyLoggedException`**. Se **`options.isAdminSdk()`** for `true`, este método retorna sem persistir (comportamento especial).

## Operações suportadas

| Método | Descrição |
|--------|-----------|
| `signInAnonymously()` | Registo/login anónimo; devolve `User` com tokens. |
| `signUp(email, password)` | Cria conta email/password. |
| `signIn(email, password)` | Login email/password. |
| `delete(idToken)` | Remove a conta associada ao token. |
| `deleteUser(user)` | Atalho para `delete(user.getIdToken())`. |
| `sendEmailVerification(user)` | Envia email de verificação. |
| `updateDetails(user, UserDetails)` | Atualiza perfil (campos suportados pela API). |
| `getUserDetails(user)` | Preenche dados do utilizador a partir do servidor (recomendado após login). |
| `linkUserWithEmailAndPassword(user, email, password)` | Associa conta anónima a email/password. |
| `logout()` | Termina sessão local (e persistência conforme implementação). |

Cada método que altera o estado de login chama internamente a lógica de **`changeCurrentUser`** com `overwrite` adequado.

## Erros HTTP

Respostas não bem-sucedidas são convertidas em exceções através de **`processError`** (mensagens normalizadas em maiúsculas). Consulte a [tabela de exceções](exceptions.md).

Erros não mapeados surgem como **`RuntimeException`** com a mensagem devolvida pela API.

## Modelo `User`

Em **`balbucio.org.firebase4j.model.User`** encontram-se tokens (`idToken`, `refreshToken`), expirações e referência à instância `FirebaseAuth` para operações subsequentes. Convém chamar **`getUserDetails`** quando precisarem de metadados completos.

## Extensão futura

A classe prevê evolução da versão da API (`AuthV1`); novas versões podem expor-se via novas implementações e eventualmente nova fábrica.

## Ligações

- [Configuração](configuration.md)  
- [Firestore](firestore.md) (usa o ID token no cabeçalho `Authorization`)  
- [Persistência](persistence.md)
