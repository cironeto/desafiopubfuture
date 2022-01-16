![GitHub Workflow Status](https://img.shields.io/github/workflow/status/cironeto/desafiopubfuture/maven-ci-cd?style=flat-square)
![GitHub last commit](https://img.shields.io/github/last-commit/cironeto/desafiopubfuture?style=flat-square)
![GitHub top language](https://img.shields.io/github/languages/top/cironeto/desafiopubfuture?style=flat-square)
# Desafio de Programação - PubFuture

## Sobre
Esse projeto foi desenvolvido para o Desafio de Programação
Pubfuture.
O objetivo é implementar uma solução que 
auxilie no controle das finanças pessoais. 
Para isso foi desenvolvido uma API REST com Spring Boot
responsável por esse gerenciamento financeiro.


O sistema contém as seguintes funcionalidades:

    • CRUD de Contas / Listar saldo total / transferir saldo entre contas
    • CRUD de Receitas / Listar receita total / filtrar por tipo e datas
    • CRUD de Despesas / Listar despesa total / filtrar por tipo e datas

## Ferramentas ultilizadas
- Java 11
- Spring Boot 2.6.2
- Spring Data JPA
- H2 Database
- JUnit 5


## Requisitos
Para execução deste projeto é necessário ter instalado:
- JDK 11

## Execução
Execute via IDE ou abra o terminal em '.../desafiopubfuture' e execute o comando:


```sh
./mvnw clean install
```
Em seguida,execute a aplicação:

```sh
./mvnw spring-boot:run
```


Ao executar, a aplicação estará disponível em **http://localhost:8080**

Assim que o projeto é executado, o banco de dados H2 é criado com alguns dados inseridos e pode ser acessado em **http://localhost:8080/h2-console/**:
```sh
H2
username: pub
password: pub
```


## REST API Endpoints
As requisições devem ser realizadas nos endpoints listados abaixo:

Caso utilize o POSTMAN, importar o link da coleção de requisições:
https://www.getpostman.com/collections/acd30342d69d81a8ca07
- No Postman selecionar **arquivo/file** -> **importar/import** -> **Link**

### Requisições:
### - CONTAS
### Listar todas as contas:

`GET /accounts`

### Salvar nova conta:

`POST /accounts`

Corpo:
```sh
{
  "balance": 10000,
  "accountType": 1,
  "financialInstitution": "Banco Pub"
}
```

### Deletar uma conta:

`DELETE /accounts/{id}`

### Atualizar nova conta:

`PUT /accounts/{id}`

Corpo:
```sh
{
  "balance": 500,
  "accountType": 1,
  "financialInstitution": "Banco Pub"
}
```

### Visualizar saldo de todas as contas:

`GET /transaction/total-balance`

### Transferir saldo entre contas:

`PUT /transaction/transfer`

Corpo:
```sh
{
  "sourceAccountId": 1,
  "targetAccountId": 2,
  "amount": 50
}
```

### - RECEITAS
### Listar todas as receitas:

`GET /incomes`

### Salvar nova receita:

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

### Deletar uma receita:

`DELETE /incomes/{id}`

### Atualizar nova receita:

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
### Mostrar total de receitas:

`GET /incomes/total`

### Fitrar receitas por Tipo 
(SALARY, GIFT, BONUS, OTHERS):

`GET /incomes/{TIPO}`

### Fitrar receitas por data de recebimento:
(yyyy-MM-dd):

`GET /incomes/receiving-date?from={yyyy-MM-dd}&to={yyyy-MM-dd}`

### Fitrar receitas por data esperada do recebimento:
(yyyy-MM-dd):

`GET /incomes/expected-date?from={yyyy-MM-dd}&to={yyyy-MM-dd}`


### - DESPESAS
### Listar todas as despesas:

`GET /expenses`

### Salvar nova despesas:

`POST /expenses`

Corpo:
```sh
{
  "value": 250,
  "paymentDate": "2022-01-15",
  "dueDate": "2022-01-13",
  "expenseType": "CLOTHING",
  "accountId": 2
}
```

### Deletar uma despesas:

`DELETE /expenses/{id}`

### Atualizar nova despesas:

`PUT /expenses/{id}`

Corpo:
```sh
{
  "value": 150,
  "paymentDate": "2022-01-15",
  "dueDate": "2022-01-13",
  "expenseType": "CLOTHING",
  "accountId": 2
}
```
### Mostrar total de despesas:

`GET /expenses/total`

### Fitrar despesas por Tipo
(FOOD, EDUCATION, LEISURE, HOUSEHOLD, CLOTHING, HEALTH, TRANSPORT, OTHERS):

`GET /expenses/{TIPO}`

### Fitrar despesas por data de pagamento:
(yyyy-MM-dd):

`GET /expenses/payment-date?from={yyyy-MM-dd}&to={yyyy-MM-dd}`

### Fitrar despesas por data de vencimento:
(yyyy-MM-dd):

`GET /expenses/due-date?from={yyyy-MM-dd}&to={yyyy-MM-dd}`

<br><br>
Desenvolvido por Ciro Neto
<div> 
<a href="https://api.whatsapp.com/send?phone=5519992582741" target="_blank"><img src="https://img.shields.io/badge/WhatsApp-25D366?style=for-the-badge&logo=whatsapp&logoColor=white" target="_blank"></a> 
<a href="https://www.linkedin.com/in/cironeto/" target="_blank"><img src="https://img.shields.io/badge/-LinkedIn-%230077B5?style=for-the-badge&logo=linkedin&logoColor=white" target="_blank"></a> 
<a href = "mailto:ciro.neto16@gmail.com"><img src="https://img.shields.io/badge/-Gmail-%23333?style=for-the-badge&logo=gmail&logoColor=white" target="_blank"></a>
</div>



