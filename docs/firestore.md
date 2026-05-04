# Cloud Firestore (`Firestore`)

O acesso usa a API REST **v1** do Firestore (`FirestoreV1`). Base URL interna:  
`https://firestore.googleapis.com/v1/projects/{project_id}/databases/...`

## Criação

```java
// Base de dados por omissão "(default)"
Firestore db = Firestore.newInstance(options);

// Nome da base de dados explícito
Firestore db = Firestore.newInstance(options, "(default)");

// Com Auth para pedidos autenticados (recomendado com regras que exijam login)
Firestore db = Firestore.newInstance(options, "(default)", auth);
```

Também podem criar sem `auth` e ligar depois:

```java
Firestore db = Firestore.newInstance(options, "(default)", null).setAuth(auth);
```

Com **`auth` null** ou utilizador não autenticado, os pedidos vão **sem** cabeçalho `Authorization` (útil só se as regras permitirem leitura pública). Com utilizador autenticado, usa-se **`Bearer {idToken}`**.

## Operações implementadas

| Método | Notas |
|--------|--------|
| `getDocument(collection, id)` | Usa a base default configurada no construtor. |
| `getDocument(database, collection, id)` | GET no documento; preenche um **`DocumentSnapshot`**. |
| `updateDocument(collection, id, fields)` | PATCH com mapa de campos (formato simplificado enviado como JSON `fields`). |
| `updateDocument(database, collection, id, fields)` | Idem para outra base. |
| `updateDocument(DocumentSnapshot)` | Atualiza a partir do snapshot (delega nos campos internos). |
| `listDatabases()` | Na implementação atual devolve **lista vazia** (stub). |

## `DocumentSnapshot`

Classe em **`balbucio.org.firebase4j.model.DocumentSnapshot`**:

- Metadados: `collection`, `database`, `name`, `createTime`, `updateTime`, `path`.
- **`fields`**: mapa chave → valor já deserializado de tipos Firestore (string, int, bool, mapas, listas conforme implementação em `deserializeValue`).
- Acedores de conveniência: **`asString`**, **`asInt`**, **`asBool`**, **`asMap`**, **`asList`**.
- **`put`**, **`mergeValues`**, **`replaceValues`** para manipular em memória antes de gravar.

## Erros

- **`PermissionDeniedException`** quando a API devolve código **403** no payload de erro.
- Outros códigos podem produzir **`RuntimeException`** genérica com texto do erro (ver implementação de **`processError`** em `Firestore`).

## Regras de segurança

O comportamento depende das **Firestore Security Rules** e de o pedido ir **autenticado** ou não. Configure Auth e passe **`FirebaseAuth`** com utilizador logado quando as regras exigirem `request.auth`.

## Limitações

- Não está exposta aqui a lista completa da API Firestore (queries compostas, transações, batch, etc.); o foco atual é **get** e **patch** de documentos concretos.
- URLs e formato JSON seguem a REST API v1; alterações futuras do Google podem exigir atualização da biblioteca.

## Ligações

- [Autenticação](authentication.md)  
- [Configuração](configuration.md)
