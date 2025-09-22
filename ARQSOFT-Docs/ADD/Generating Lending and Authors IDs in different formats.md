# Generating Lending and Authors ID in Different Formats According to Varying Specifications

---

### Design objective
Suporte à geração de IDs em formatos customizáveis por configuração.

---

### Quality Attribute Scenario

| **Element**          | **Statement**                                                                                                                                                                                                                                        |
|----------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Stimulus**         | Necessidade de gerar IDs de Empréstimos e Autores em formatos específicos de acordo com especificações definida pelo product owner.                                                                                                                  |
| **Stimulus source**  | Product owner define diferentes regras de formatação de IDs para diferentes clientes ou sistemas.                                                                                                                                                    |
| **Environment**      | Product owner reconhece a necessidade de usar algoritmos diferentes para gerar IDs de empréstimos e IDs de autores para diferentes clientes, o que atualmente requer múltiplos branches/projetos, aumentando a possibilidade de repetição de código. |
| **Artifact**         | O serviço de criação de IDs.                                                                                                                                                                                                                         |
| **Response**         | O sistema deve gerar IDs de Empréstimos e Autores no formato adequado para cada cliente, conforme o ficheiro de configuração.                                                                                                                        |
| **Response measure** | Deve ser possível alterar o modelo de geração de IDs através do ficheiro de configuração, em 5 minutos.                                                                                                                                              |
---

### Constraints

- O requisito deve ser implementado com base no serviço REST já existente (LMS).
- A implementação deve permitir que o formato de geração de IDs possa ser alterado via configuração sem a necessidade de recompilar ou reiniciar o sistema.
- O sistema deve suportar múltiplos algoritmos de geração de IDs, por exemplo:
  - 24 hexadecimal characters
  - 20 carateres alfanuméricos como hash de id de negócio da entidade de negócio

---

### Concerns

- **Flexibilidade da Configuração**: O sistema deve possibilitar a geração de IDs em diferentes formatos definidos pelo ficheiro de configuração.

- **Manutenção e Extensibilidade**: A implementação deve ser modular e extensível para que novos formatos de geração de IDs possam ser adicionados com facilidade.

- **Configuração e Usabilidade**: A configuração para a geração de IDs deve ser gerida através do ficheiro `library.properties` e deve ser de fácil compreensão para que mudanças possam ser aplicadas de forma eficiente e sem erros.

---

### Technical Memo

- **Problem**: Necessidade de geração de IDs de Empréstimos e Autores em diferentes formatos, adaptáveis conforme especificações dos clientes.

- **Resumo da Solução**: Implementar uma solução configurável para que o sistema possa gerar IDs de Empréstimos e Autores em diferentes formatos, como **UUID** ou **alfanuméricos**, com base em regras definidas pelo ficheiro de configuração.

- **Fatores**:
    - Suporte à flexibilidade de definir diferentes formatos de geração de IDs por configuração.
    - Modularidade para facilitar a inclusão de novos formatos de IDs sem alterações profundas no código existente.
    - Possibilidade de personalizar a lógica de geração de IDs para atender a diferentes necessidades de clientes.

- **Solução**:
    - **Abstract Common Services**: Facilitar a adição de novas estratégias de geração de IDs por meio de classes abstratas e interfaces.
    - **Defer Binding**: Permitir a escolha do algoritmo de geração de IDs em tempo de execução por meio de um ficheiro de configuração.
    - **Encapsular**: Criacao de um modulo independente para facilitar a troca de formatodos de geracao de ids
    - **Restringir Dependencias**: Manter a logica de geracao de ids independente do resto do sistema

- **Motivação**: Atender às exigências de clientes com diferentes especificações para formatos de IDs e reduzir a necessidade de ramificações separadas de código.

- **Alternativas**: Codificar a lógica de geração de IDs de forma fixa, utilizando apenas o necessario, o que reduziria a flexibilidade e aumentaria a complexidade de manutenção, adaptação e diminuiria a escalabilidade do sistema.

- **Questões pendentes**:
    - **Quais partes do sistema precisarão ser modificadas para acomodar a nova solução?**
    - **Quais formatos de geração de IDs serão necessários inicialmente (UUID, alfanuméricos, etc.)?**
    - **Qual o impacto de performance de diferentes métodos de geração de IDs?**
