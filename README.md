# PostViewer

**Aluno:** Leo Marques Sabino de Freitas  
**RA:** 3045366  
**Disciplina:** Programação para Dispositivos Móveis (PRDM)  
**Instituição:** IFSP — Campus São Carlos  

---

## Descrição

PostViewer é um aplicativo Android que consome a API pública [JSONPlaceholder](https://jsonplaceholder.typicode.com) para exibir posts e seus comentários, e permite que o usuário adicione comentários próprios salvos localmente no dispositivo.

### Requisitos implementados

- Listagem de posts buscados da API (tela principal)
- Tela de detalhe de um post exibindo os comentários da API
- Adição de comentários locais por tela, persistidos no dispositivo
- Comentários locais e da API exibidos juntos na tela de detalhe
- Navegação entre telas com botão de voltar na barra do topo

---

## Como executar localmente

### Pré-requisitos

- Android Studio Meerkat ou superior
- JDK 11
- Dispositivo físico ou emulador com Android 8.0+ (API 26+)
- Conexão com a internet (para buscar posts e comentários da API)

### Passos

1. Clone ou copie o projeto para uma pasta local
2. Abra o Android Studio e selecione **Open** → pasta do projeto
3. Aguarde o Gradle sincronizar as dependências
4. Clique em **Run → Run 'app'** ou pressione `Shift + F10`

Não é necessário configurar nenhuma chave de API ou variável de ambiente.

---

## Tecnologias e bibliotecas

| Tecnologia | Versão | Uso |
|---|---|---|
| Kotlin | 2.3.21 | Linguagem principal |
| Android Gradle Plugin | 9.0.1 | Build do projeto |
| Jetpack Compose | BOM 2026.05.00 | Interface declarativa |
| Navigation Compose | 2.9.8 | Navegação entre telas |
| Room | 2.7.1 | Banco de dados local |
| Retrofit | 2.11.0 | Requisições HTTP |
| Gson Converter | 2.11.0 | Desserialização do JSON da API |
| Coroutines | 1.10.2 | Operações assíncronas |
| Lifecycle ViewModel Compose | 2.10.0 | ViewModel integrado ao Compose |
| KSP | 2.3.9 | Geração de código para o Room |

---

## Decisões de design

### Jetpack Compose em vez de XML
O Compose permite descrever a interface diretamente em Kotlin, sem alternar entre arquivos de layout e código. A reatividade embutida (via `StateFlow` + `collectAsState`) simplifica a atualização da UI quando o estado muda.

### ViewModel + StateFlow
Cada tela tem seu próprio ViewModel que expõe um `StateFlow<UiState>`. A tela só lê o estado — nunca acessa a API ou o banco diretamente. Isso mantém a lógica de negócio separada da UI e facilita a localização de bugs.

### AndroidViewModel para acesso ao banco
`PostDetailViewModel` estende `AndroidViewModel` (em vez de `ViewModel`) para receber o `Application` e abrir o banco Room sem depender de um repositório ou injeção de dependência extra, mantendo a implementação simples conforme o escopo do projeto.

### Room para comentários locais
Os comentários adicionados pelo usuário são armazenados com Room. A query retorna um `Flow<List<LocalComment>>`, que é combinado com os comentários da API via `combine()`. Quando o usuário insere um novo comentário, a tela atualiza automaticamente sem nenhuma chamada manual de refresh.

### Navigation Compose centralizado
As rotas são definidas em uma `sealed class Screen` e o grafo de navegação fica em `AppNavGraph`. Isso evita strings de rota espalhadas pelo código e centraliza toda a lógica de navegação em um único lugar.

### Scaffold com TopAppBar na MainActivity
O `Scaffold` com a `TopAppBar` fica na `MainActivity`, compartilhado entre todas as telas. O botão de voltar aparece automaticamente quando há uma tela anterior na pilha de navegação, sem precisar configurar nada individualmente em cada tela.
