# Persisting data in different data models and SGBD

---

### Design objective
Suporte a múltipos SGBDs por Configuração
---

### Quality Attribute Scenario

| **Element**          | **Statement**                                                                                                                                                       |
|----------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Stimulus**         | Existe a necessidade de suportar outros tipos de SGBD de forma a sustentar a necessidades de diferetentes clientes.                                                 |
| **Stimulus source**  | Product owner precisa que através de configuração seja possível adaptar a aplicação a diferentes SGBD                                                               |
| **Environment**      | Na atualidade a necessidade de usar SGBD para diferentes clientes requer a adoção de varios branches/projetos, e com isso aumentar a duplicação/repetição de código |
| **Artifact**         | Camada de persistencia de dados                                                                                                                                     |
| **Response**         | Deve ser desenvolvida uma solução baseada em configuração, que permita selecionar entre vários SGBD previamente configurados.                                       |
| **Response measure** | Deve ser possível alterar o modelo de dados e SGBDs pretendido através de um ficheiro de configuração, em 5 minutos.                                                |

---

### Constraints

- O requisito deve ser implementado com base no serviço REST já existente (LMS).
- O requisito deve ser implementado de forma a responder as necessidades do product owner de persistir dados em diferentes modelos de dados e SGBD:

    - **Modelo de dados relacional: por exemplo, H2, MySQL, SQL Server**

    - **Modelo de dados de documentos (MongoDB)**
---

### Concerns

- **Flexibilidade da Configuração**: O sistema deve permitir a seleção da persistência de dados em diferentes modelos de dados através de configuração sem necessisade de interromper o serviço.


- **Manutenção e Extensibilidade**: A implementação deve ser modular para que a adição de suporte a novos SGBDs seja simples sem dificultar a manutenção a longo prazo.


- **Configuração e Usabilidade**: A configuração através do ficheiro `applicationContext.xml` deve ser intuitiva e de fácil acesso, de forma que as alterações possam ser aplicadas de maneira rápida e sem erros.

---

### Technical Memo

- **Problem**: Persistência de dados em diferentes modelos de dados (por exemplo, relacional, documento) e SGBD (por exemplo, MySQL, Oracle, MongoDB)


- **Resumo da Solução**: Implementar uma solução configurável para que aplicação possa suportar diferentes tipos de modelos de dados e sistemas de gestão de banco de dados (SGBDs), como H2 (relacional) e MongoDB (documento).


- **Fatores**:
    - A capacidade de integração de diferentes SGBDs com estruturas de dados distintas, relacionais (H2) ou não relacionais (MongoDB)
    - Manter a aplicação modular e flexível para suportar a troca ou inclusão de novos SGBDs através do ficheiro de configuração.
    - A solução deve permitir alterar o Sistema de Gestao de Base de Dados sem afetar o funcionamento do sistema.


- **Solução**:
    - **Use an Intermediary**: Essa tática envolve a inserção de um intermediário, como um repositório ou adaptador, que lida com as especificidades dos SGBDs, simplificando a interação entre a aplicação e os diferentes sistemas de armazenamento.
    - **Abstract Common Services**: Para facilitar a integração com novos tipos de base de dados.
    - **Defer Binding**: Vai permitir selecionar o SGBD pretendido em tempo de execução através do ficheiro de configuracao.

- **Motivação**: Usar o código base para diferentes clientes que podem requerer de diferentes modelos de dados e diferentes SGBD


- **Alternativas**: Acoplar diretamente a lógica de persistência com implementações específicas de bases de dados, o que limitaria a flexibilidade e dificultaria futuras mudanças.


- **Questões pendentes**:
  - **O quão longo e difícil a “refazer”/adaptar a solução atual para adotar outros modelos de dados de persistência e SGBD?**
  - **Quais são as partes do sistema que vão requerer modificações?**
  - **Quais modelos de base de dados e SGBD vão ser necessárias? (H2, MongoDB)**

