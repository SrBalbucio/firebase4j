# Exceções (Authentication API)

Os erros devolvidos pela **Identity Toolkit REST API** são mapeados em **`FirebaseAuth#processError`**. A mensagem da API (`error.message`) é normalizada para maiúsculas e comparada com valores fixos.

## Mapeamento principal

| Mensagem API (normalizada) | Tipo Java |
|----------------------------|-----------|
| `OPERATION_NOT_ALLOWED` | `OperationNotAllowedException` |
| `EMAIL_EXISTS` | `EmailExistsException` |
| `INVALID_ID_TOKEN` | `InvalidIdTokenException` |
| `USER_NOT_FOUND` | `UserNotFoundException` |
| `EMAIL_NOT_FOUND` | `UserNotFoundException` |
| `ADMIN_ONLY_OPERATION` | `BadConfigurationException` (mensagem fixa sobre método de login não configurado) |
| `PHONE_NUMBER_ALREADY_EXISTS` | `PhoneNumberExistsException` |
| `INVALID_PASSWORD` | `InvalidPasswordException` |
| `USER_DISABLED` | `UserDisabledException` |
| `TOO_MANY_ATTEMPTS_TRY_LATER` | `TooManyAttemptsTryLater` |
| *(qualquer outra)* | `RuntimeException` com a mensagem original |

Em vários casos, o segundo argumento `data` em `processError` é um **`JSONObject`** usado para preencher campos contextuais nas exceções (email, idToken, password, phoneNumber).

## Fluxo do utilizador já autenticado

Ao tentar substituir o utilizador actual sem **`overwrite`**, **`changeCurrentUser`** lança **`AlreadyLoggedException`** (não vem da REST API).

## Firestore

Erros HTTP do Firestore são tratados em **`Firestore#processError`**:

- Código **403** no JSON de erro → **`PermissionDeniedException`**
- Outros casos frequentemente → **`RuntimeException`** com código ou texto genérico

Consulte [Firestore](firestore.md).

## Pacote

Todas estas classes estão sob **`balbucio.org.firebase4j.exception`**.
