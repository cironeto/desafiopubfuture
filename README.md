# Desafio de Programação - PubFuture
## Detalhes do projeto

O objetivo do projeto é implementar uma solução que auxilie no controle das finanças pessoais. Para isso foi programado um sistema responsável por gerenciar as finanças pessoais.
Foi desenvolvido uma API REST com Spring Boot.

O sistema deverá conter os seguintes requisitos:

    • CRUD de Contas / Listar saldo total / transferir entre contas
    • CRUD de Receitas / Listar receita total / filtrar por datas
    • CRUD de Despesas / Listar despesa total / filtrar por datas

## Ferramentas ultilizadas
- Java 11
- Spring Boot 2.6.2
- Spring Data JPA
- JUnit 5
- H2
- Lombok

## Requisitos
Para execução deste projeto é necessário ter pré-instalado e configurado:
- JDK 11

## Executando o projeto
Abra o terminal em '.../desafiopubfuture' e execute o comando:

```sh
./mvnw clean
```
```sh
./mvnw install
```
Para executar os testes unitários:
```sh
./mvnw test
```
Para executar a aplicação:
```sh
./mvnw spring-boot:run
```

Ao executar, a aplicação estará disponível em **http://localhost:8080**

Assim que o projeto é executado, o banco de dados H2 é criado com alguns dados inseridos e pode ser acessado em **http://localhost:8080/h2-console/**

## REST API Endpoints
Para testar as requisições seguem abaixo os endpoints que devem
ser chamados.

Caso utilize o POSTMAN, importar o link da coleção de requisições:
https://www.getpostman.com/collections/acd30342d69d81a8ca07
- No Postman selecionar **arquivo/file** -> **importar/import** -> **Link**

###<u>Requisições</u>
###<u>Contas</u>
###Listar todas as contas:

`GET /accounts`

###Salvar nova conta:

`POST /accounts`

Corpo:
```sh
{
  "balance": 10000,
  "accountType": 1,
  "financialInstitution": "Banco Pub"
}
```

###Deletar uma conta:

`DELETE /accounts/{id}`

###Atualizar nova conta:

`PUT /accounts/{id}`

Corpo:
```sh
{
  "balance": 500,
  "accountType": 1,
  "financialInstitution": "Banco Pub"
}
```

###Visualizar saldo de todas as contas:

`GET /transaction/total-balance`

###Transferir saldo entre contas:

`PUT /transaction/transfer`

Corpo:
```sh
{
  "sourceAccountId": 1,
  "targetAccountId": 2,
  "amount": 50
}
```

###<u>Receitas</u>
###Listar todas as receitas:

`GET /incomes`

###Salvar nova receita:

`POST /incomes`

Corpo:
```sh
{
  "value": 1000,
  "receivingDate": "2022-01-10",
  "expectedReceivingDate": "2022-01-10",
  "description": "ref 01/2022",
  "incomeType": "2",
  "accountId": 2
}
```

###Deletar uma receita:

`DELETE /incomes/{id}`

###Atualizar nova receita:

`PUT /incomes/{id}`

Corpo:
```sh
{
  "value": 1000,
  "receivingDate": "2022-01-10",
  "expectedReceivingDate": "2022-01-10",
  "description": "ref 01/2022",
  "incomeType": "2",
  "accountId": 1
}
```






