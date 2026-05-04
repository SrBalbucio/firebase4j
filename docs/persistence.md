# Persistência de sessão

Por omissão, **`FirebaseOptions`** usa **`EmptyPersistent`**: o estado do login **não** é guardado entre execuções da aplicação.

Para restaurar automaticamente o **`User`** ao arrancar o **`FirebaseAuth`**, configure uma implementação de **`FirebasePersistent`**.

## Interface `FirebasePersistent`

Pacote: `balbucio.org.firebase4j.persistent`.

| Método | Função |
|--------|--------|
| `getCurrentUser(FirebaseAuth auth)` | Devolver o utilizador persistido ou `null`. |
| `saveCurrentUser(User user)` | Guardar o utilizador atual (chamado quando o login muda). |
| `removeCurrentUser()` | Limpar só o utilizador atual. |
| `clear()` | Apagar todos os dados persistidos pela implementação. |

O Auth chama **`saveCurrentUser`** quando o utilizador muda (exceto no modo `adminSdk` — ver abaixo).

## Implementações incluídas

### `EmptyPersistent`

Implementação nula; não grava nada.

### `FilePersistent`

Persistência em **ficheiro JSON** simples; o factory usa um executor em cache.

```java
FirebasePersistent p = FirebasePersistent.fromFile(new File("firebase-session.dat"));
options.setPersistent(p);
```

Para controlo fino do executor, instanciem **`new FilePersistent(file, executor)`** directamente.

### `MVStorePersistent`

Usa **H2 MVStore** (ficheiro `.mv.db` ou path configurado).

```java
FirebasePersistent p = FirebasePersistent.fromMVStore(new File("firebase.mv.db"));
options.setPersistent(p);
```

Útil quando preferem um formato compacto e embutido sem estrutura de ficheiro texto.

## Integração com `FirebaseAuth`

Ao criar **`FirebaseAuth.newInstance(options)`**, o construtor faz:

```text
currentUser = options.getPersistent().getCurrentUser(this)
```

Portanto, desde que tenham persistido um **`User`** válido (tokens ainda utilizáveis ou com capacidade de refresh conforme a vossa lógica), o utilizador reaparece na próxima execução.

**Nota:** tokens expiram; podem precisar de fluxo de **refresh** conforme política da app (não detalhado aqui se não estiver na API exposta).

## Modo `adminSdk`

Se **`FirebaseOptions#setAdminSdk(true)`**:

- **`changeCurrentUser`** não grava na persistência e não aplica a lógica de “já existe utilizador” da mesma forma.

Use apenas se souberem o impacto (normalmente integrações especiais, não a app desktop média).

## Boas práticas

- Isolar o ficheiro de sessão num directório de dados da aplicação do utilizador.
- Em apps partilhadas, considerar permissões do SO sobre o ficheiro.
- Nunca guardar **conta de serviço** nestes mecanismos — apenas o modelo **`User`** do cliente.

## Ligações

- [Configuração](configuration.md)  
- [Autenticação](authentication.md)
