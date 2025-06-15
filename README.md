# Web Crawler Application

## Descrição
Esta aplicação é um web crawler que busca URLs em páginas web com base em uma palavra-chave fornecida. A aplicação agora permite que o usuário especifique tanto a URL base quanto a palavra-chave para a busca.

## Funcionalidades
- Interface web amigável
- Busca personalizada em qualquer domínio web
- Rastreamento recursivo de links
- Feedback em tempo real do progresso da busca
- Exibição dos resultados encontrados

## Como Executar
docker build . -t axreng/backend

#### Executando o container
docker run -p 4567:4567 --rm axreng/backend

### Acessando a Aplicação
1. Abra seu navegador e acesse `http://localhost:4567`
2. Na interface, você encontrará dois campos:
   - URL Base: Digite a URL completa do site onde deseja realizar a busca (ex: http://example.com/)
   - Palavra-chave: Digite o termo que deseja procurar (4-32 caracteres)
3. Clique em "Iniciar Busca" para começar o processo

## API Endpoints

### POST /crawl
Inicia uma nova busca.

Request body:
json { "baseUrl": "[http://example.com/](http://example.com/)", "keyword": "search-term" }

Response:
json { "id": "unique-crawl-id" }

### GET /crawl/:id
Recupera o status e resultados de uma busca.

Response:
json { "id": "unique-crawl-id", "status": "active|done", "urls": "http://example.com/page1.html","http://example.com/page2.html" }

## Restrições
- A palavra-chave deve ter entre 4 e 32 caracteres
- A URL base deve ser uma URL válida e acessível
- A aplicação realiza buscas apenas em páginas HTML

## Tecnologias Utilizadas
- Java 17
- Spark Framework
- Docker
- HTML/CSS/JavaScript

## Notas de Segurança
- A aplicação valida todas as entradas do usuário
- Implementa limites de conexão e timeout
- Respeita as políticas de robots.txt dos sites

## Suporte
Para questões e suporte, por favor abra uma issue no repositório do projeto.


