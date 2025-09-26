# ARQSOFT-P1 — Library Management System (Projeto 1)

## Contexto

Este repositório corresponde ao Projeto 1 da unidade curricular de **Arquitetura de Software** (2024/2025), cujo propósito é evoluir uma aplicação REST de gestão de biblioteca desenvolvida previamente, superando limitações identificadas de extensibilidade, configurabilidade e fiabilidade.

## Problema Identificado

O sistema original, embora funcional, apresentava as seguintes limitações:
- **Baixa extensibilidade**: Dificuldade em adicionar novos tipos de persistência ou mecanismos de autenticação sem alterar código existente.
- **Configuração rígida**: Falta de flexibilidade para alterar comportamentos do sistema sem recompilação.
- **Problemas de fiabilidade**: Cobertura de testes insuficiente e ausência de mecanismos para garantir robustez em produção.

## Objetivos do Projeto

- Permitir alternância dinâmica de mecanismos de persistência (relacional, documental, diferentes SGBDs);
- Suportar múltiplos fornecedores de IAM (Google, Facebook, Azure), selecionáveis em runtime;
- Gerar identificadores de entidades (Empréstimos, Autores) em formatos variáveis conforme especificações de negócio;
- Prover recomendações de empréstimos baseadas em critérios configuráveis;
- Melhorar a qualidade de testes (unitários, mutacionais, integração e aceitação);
- Documentar decisões arquiteturais e alternativas, com foco em ASRs (Architecturally Significant Requirements).

## Abordagem, Arquitetura e Padrões

### Padrões Aplicados
- **Strategy**: Permite alternar algoritmos de persistência/IAM/recomendação em runtime sem alterar o código cliente.
- **Factory**: Facilita a criação de objetos (ex: geradores de ID) com base em configurações.
- **Adapter**: Integração transparente com APIs externas de IAM.
- **Dependency Injection (DI)**: Utilização do Spring Boot para promover baixo acoplamento e facilitar testes.
- **Configuration as Code**: Utilização de ficheiros de configuração e variáveis de ambiente para alterar comportamentos sem recompilação.

### Decisões Arquiteturais
- **Separação de camadas**: Domínio, aplicação, infraestrutura e apresentação bem definidos.
- **Portabilidade**: O sistema pode ser executado sobre diferentes SGBDs apenas alterando configurações.
- **Testabilidade**: Cobertura de testes elevada, com testes mutacionais para garantir robustez e integração contínua.

### Táticas de Qualidade
- **Configuração dinâmica** (modificabilidade)
- **Injeção de dependências** (testabilidade, manutenibilidade)
- **Fallback e logging centralizado** (fiabilidade)
- **Documentação automatizada da API** (usabilidade e manutenção)

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot** (REST API, DI)
- **Maven** (gestão de dependências)
- **MySQL, MongoDB** (persistência polimórfica)
- **JUnit, Mockito, PIT** (testes unitários, integração, mutação)
- **Swagger/OpenAPI** (documentação da API)


## Organização do Repositório

- `src/` — Código-fonte principal (controladores, serviços, repositórios, modelos de domínio)
- `Docs/` e `ARQSOFT-Docs/` — Documentação técnica, diagramas, relatórios de decisões
- `HELP.md` — Dicas de utilização e FAQ
- `pom.xml` — Configuração Maven
- `uploads-psoft-g1/` — Dados para testes ou submissão

## Racional

As opções arquiteturais foram fundamentadas em requisitos não-funcionais críticos (ASRs), priorizando a adaptabilidade, facilidade de manutenção e robustez. A modularidade e o uso extensivo de padrões tornaram o sistema mais flexível, preparado para futuras evoluções e integrações.


> **ISEP — Arquitetura de Software, 2024/2025**  
