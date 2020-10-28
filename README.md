# SouthSystems 0.1.0

## Sumário

<!-- TOC depthFrom:1 depthTo:2 orderedList:false withLinks:false anchorMode:gitlab.com -->
- 1 - Tecnologias
- 2 - Definição do projeto
- 3 - Execução
- 4 - Layout dos arquivos
<!-- /TOC -->

# 1 - Tecnologias
Projeto todo baseado em Spring com a utilização do Spring-Boot para configuração e inicialização do projeto.
Utilizado JUnit como ferramenta de testes unitário para validar a lógica envolvida.

# 2 - Definição do projeto
Você deve criar um sistema de análise de dados, onde o sistema deve importar lotes de arquivos, ler e analisar os dados e produzir um relatório

# 3 - Execução
A execução do aplicativo pode ser feita pelo comand `./gradlew run`, desta forma a rotina que monitora a pasta de input será executada conforme o intervalor definido na configuração do arquivo application.yaml.
Importante!!! Para que o aplicativo execute de forma correta, a variável $HOMEPATH deve estar definida e apontando para um diretório, onde deve existir uma pasta chamada data/in(pasta de input) e data/out. A pasta data/in terá os arquivos a serem processados e a pasta data/out terá um arquvio com o mesmo nome com o resultado.

# 4 - Layout dos arquivos
Para cada tipo de dados há um layout diferente.
Dados do vendedor
Os dados do vendedor têm o formato id 001 e a linha terá o seguinte formato: 001çCPFçNameçSalary

Dados do cliente
Os dados do cliente têm o formato id 002 e a linha terá o seguinte formato: 002çCNPJçNameçBusiness Area

Dados de vendas
Os dados de vendas têm o formato id 003. Dentro da linha de vendas, existe a lista de itens, que é envolto por colchetes []. A linha terá o seguinte formato: 003çSale IDç[Item ID-Item Quantity-Item Price]çSalesman name

Dados de Exemplo
O seguinte é um exemplo dos dados que o sistema deve ser capaz de ler.
001ç1234567891234çPedroç50000
001ç3245678865434çPauloç40000.99
002ç2345675434544345çJose da SilvaçRural
002ç2345675433444345çEduardo PereiraçRural
003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro
003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çPaulo