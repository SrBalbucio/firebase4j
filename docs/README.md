# Documentação Firebase4J

SDK não oficial em Java puro para usar **Firebase Authentication** e **Cloud Firestore** via [REST APIs](https://firebase.google.com/docs/reference/rest), pensado para aplicações desktop ou qualquer JVM sem o SDK Android/iOS.

**Firebase** é marca registrada do Google LLC.

## Índice

| Documento | Conteúdo |
|-----------|----------|
| [Arquitectura](architecture.md) | Diagrama e papéis dos módulos |
| [Introdução e requisitos](getting-started.md) | Java, Maven, artefatos `firebase4j-core` e `firebase4j-server` |
| [Configuração](configuration.md) | `FirebaseOptions`, ficheiros JSON do projeto Firebase |
| [Autenticação](authentication.md) | `FirebaseAuth`, utilizador atual, fluxos comuns |
| [Firestore](firestore.md) | Leitura e atualização de documentos, `DocumentSnapshot` |
| [Persistência de sessão](persistence.md) | Guardar o utilizador entre execuções (`FilePersistent`, `MVStorePersistent`) |
| [Módulo servidor e App Check](server-app-check.md) | `firebase4j-server`, conta de serviço, tokens App Check |
| [Exceções](exceptions.md) | Erros mapeados da API Identity Toolkit |
| [Compilar e testar](building-and-testing.md) | Maven, credenciais de teste, `skipTests` |
| [Migração da versão monolítica](migration.md) | De `firebase4j` 0.0.x para módulos 0.1.x |

## Repositório e artefactos

- Código e issues: repositório GitHub do projeto (ver README na raiz).
- Maven (HyperPowered): ver badge no [README principal](../README.md).

## Limitações conhecidas

- O SDK chama HTTP diretamente (via Jsoup); não é o mesmo pipeline dos SDKs oficiais móveis.
- `FirestoreV1#listDatabases` devolve lista vazia (não implementado).
- Regras de segurança do Firestore e quotas aplicam-se como na consola Firebase.
- App Check no cliente desktop pode exigir configuração adicional no projeto Firebase; o módulo `firebase4j-server` cobre apenas **troca de token no servidor** com chave de conta de serviço.

## Mais informação

- [README](../README.md) na raiz do repositório  
- [Wiki do projeto](https://github.com/SrBalbucio/firebase4j/wiki) (quando aplicável)  
- [Atribuição legal / marca Firebase](https://github.com/SrBalbucio/firebase4j/wiki/Legal)
