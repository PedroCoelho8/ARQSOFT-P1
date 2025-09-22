# Adopting Different IAM Providers

---

### Design Objective
Implementar suporte para diferentes provedores de gestão de identidade e acesso (IAM) para autenticação e autorização.

---

### Quality Attribute Scenario

| **Element**          | **Statement**                                                                                                                                                |
|----------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Stimulus**         | Existe a necessidade de adotar diferentes provedores de IAM em tempo de execução, conforme especificações do product owner.                                  |
| **Stimulus source**  | Uma nova branch será criada sempre que um novo cliente pretenda utilizar um novo provedor de IAM.                                                            |
| **Environment**      | O sistema deve funcionar com múltiplos provedores de IAM, permitindo que os utilizadores escolham como se autenticar através de configuração.                |
| **Artifact**         | Sistema de autenticação.                                                                                                                                     |
| **Response**         | Deve ser desenvolvida uma solução modular que suporte a adição e troca de provedores de IAM sem interrupção do serviço, através de ficheiro de configuração. |
| **Response measure** | A integração de novos provedores de IAM deve ser feita em até 10 minutos.                                                                                    |

---

### Constraints

- O requisito deve ser implementado de forma a garantir compatibilidade com os provedores IAM Google e Facebook, e estar preparado para acrescenter novos provedores no futuro.
- A solução deve permitir que o sistema selecione o provedor de IAM com base em configurações definidas em um arquivo de configurações.

---

### Concerns

- **Flexibilidade da Integração**: O sistema deve permitir a adição de novos provedores de IAM sem necessidade de interrupção do serviço.

- **Segurança**: As integrações devem seguir as melhores práticas de segurança para proteção de dados dos utilizadores.

- **Escalabilidade**: O sistema deve suportar um aumento no número de utilizadores e provedores de IAM, garantindo performance adequada.

- **Manutenção e Extensibilidade**: A implementação deve ser modular para facilitar a manutenção e a inclusão de novos provedores de IAM no futuro.

- **Configuração e Usabilidade**: A configuração através de um arquivo de configuração deve ser intuitiva e acessível, permitindo ajustes rápidos nas integrações.

---

### Technical Memo

- **Problem**: Integrar diferentes provedores de IAM para autenticação e autorização de utilizadores.

- **Resumo da Solução**: Implementar uma solução modular que permita a integração dinâmica de múltiplos provedores de IAM, garantindo segurança e flexibilidade nas autenticações.

- **Fatores**:
    - O sistema deve ser capaz de trabalhar com provedores de IAM populares.
    - A configuração do provedor de IAM deve ser simples e rápida.
    - A solução deve permitir alterar o provedor de IAM em tempo de execução sem interrupções.

- **Solução**:
    - **Encapsulate**: Criar interfaces para os provedores de IAM, permitindo que a implementação interna mude sem interferir com outros módulos.
    - **Use an Intermediary**: Implementar um componente que atue como um intermediário entre o sistema e os provedores de IAM.
    - **Abstract Common Services**: Criar serviços comuns que simplifiquem a integração de novos provedores de IAM.
    - **Defer Binding**: Permitir que o provedor de IAM seja selecionado em tempo de execução, conforme as preferências do utilizador.

- **Motivação**: Assegurar a flexibilidade e segurança na autenticação dos utilizadores, atendendo a diferentes necessidades e preferências.

- **Alternativas**: Avaliar provedores de IAM alternativos, considerando custos, funcionalidades e requisitos de segurança.

- **Questões Pendentes**: Definir critérios para a escolha de provedores de IAM e garantir conformidade com regulamentos de proteção de dados.

---
