**Api de Pautas de votação**
----
  Este é um sistema Desenvolvido para criar pautas de votação e permitir o usuário votar, criar pautas e listar. Além disso o sistema está integrado com o <strong>Kafka<strong> e <strong>WebSockets<strong> para expor os resultados para outras aplicações ao encerrar cada turno de votação. Um exemplo de frontEnd que recebe essas notifações foi desenvolvido em <strong>React <strong> e pode ser acessado clicando <a href="https://github.com/vitorpaixaoa/voting-agenda-app">aqui</a>. 



### EXECUÇÃO

O banco de dados ```MySQL 8``` e o serviço de mensageria ```Kafka``` estão configurados para rodar em um container docker, que foi definido no arquivo  ```docker-compose``` então para subir o container basta ter a docker instalada e rodar o comando:

```docker-compose up -d```

Para rodar o programa, basta instalar as dependências ```Maven``` e inicia-lo como uma aplicação SPRING.
Para testar o programa, basta rodar um ```mvn test``` e os testes unitários são realizados utilizando um banco em memória h2

### URL

  O serviço, como previamente definido roda na porta :8080 sendo assim ficando com o url base: 
  http://localhost:8080

  Para acessar à documentação da api e teste via Swagger basta acessar ao link:
  http://localhost:8080/swagger-ui/index.html#/

### Formato

O formato esperado de uma Pauta de votação (Voting Agenda) é o seguinte:

```javascript
  {
    "name": "String",
    "description": "String",
    "dateStart": "yyyy-MM-dd HH:mm",
    "dateEnd": "yyyy-MM-dd HH:mm"
  }
```
Durante a criação e alteração, os campos name, description e price são obrigatórios. Em relação às datas caso não informadas será definido a data atual como inicial e um minuto a mais para a final.

  

### Endpoints
A rota padrão da atual versão  (v1) da api é http://localhost:8080/api/v1.0/voting-agenda
  
São disponibilizados os seguintes endpoints:


| Verbo HTTP  |  Resource path    |          descrição           |
|-------------|:-----------------:|------------------------------:|
| POST        |  /        |   Criação de uma pauta       |
| GET        |  /        |   Listar todas as pautas       |
| GET        |  /{id}   |   Buscar pauta específica  |
| GET         |  /cpf/{cpf}   |   Verificar se o cpf é válido para votar   |
| POST         |  /{id}/vote   |   Votar em uma pauta  |
| GET         |  /{id}/votes   |  Buscar resultado da votação  |

  ### Socket
  A aplicação disponibiliza um <strong>Socket<strong> para consumir as messages de Resultado das votações
  http://localhost:8080/vote-result/
  
  as notifações ficam no tópico:
  '/topic/group'

