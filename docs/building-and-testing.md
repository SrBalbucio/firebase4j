# Compilar e testar

## Requisito de versão

Consulte o **`pom.xml`** raiz (`firebase4j-parent`) para **Java 8+** e encoding UTF-8.

## Compilar o reactor completo

Na raiz do repositório:

```bash
mvn clean verify
```

Isto compila **`firebase4j-core`** e **`firebase4j-server`**, corre testes unitários/integração configurados no Surefire e empacota os JARs.

## Testes sem credenciais Firebase

Os testes de integração esperam ficheiros JSON na **raiz do repositório** (ou no directório do módulo, conforme o teste):

- **`test-credentials.json`** — configuração Web do projeto Firebase (formato compatível com `FirebaseOptions.fromJsonFile`).
- **`service-account.json`** — apenas para o módulo **`firebase4j-server`** (`ServerAppCheckTest`).

Se não tiverem estes ficheiros (por exemplo em CI público), compilem sem executar testes:

```bash
mvn clean verify -DskipTests
```

## Executar só um módulo

```bash
mvn -pl firebase4j-core test
mvn -pl firebase4j-server test
```

## Instalar no repositório local Maven

```bash
mvn clean install
```

Os artefactos ficam disponíveis como `balbucio.org.firebase4j:firebase4j-core` e `balbucio.org.firebase4j:firebase4j-server` na versão definida no POM pai.

## Publicação

O POM pai inclui **`distributionManagement`** para o repositório HyperPowered (credenciais Maven no `settings.xml` do desenvolvedor). Detalhes operacionais ficam à cargo da equipa que publica.

## Ligações

- [Introdução](getting-started.md)
