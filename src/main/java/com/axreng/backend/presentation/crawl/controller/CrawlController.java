package com.axreng.backend.presentation.crawl.controller;

import com.axreng.backend.application.crawl.dto.request.CrawlIdDTO;
import com.axreng.backend.application.crawl.dto.request.CrawlKeywordDTO;
import com.axreng.backend.application.crawl.dto.response.GetCrawlDTO;
import com.axreng.backend.application.crawl.dto.response.StartCrawlDTO;
import com.axreng.backend.domain.crawl.exception.CrawlNotExistsException;
import com.axreng.backend.domain.crawl.usecase.GetCrawlUseCase;
import com.axreng.backend.domain.crawl.usecase.StartCrawlUseCase;
import com.axreng.backend.domain.util.GsonSingleton;
import com.axreng.backend.application.response.ErrorResponse;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import static spark.Spark.get;
import static spark.Spark.post;

public class CrawlController {
    private StartCrawlUseCase startCrawlUseCase;
    private GetCrawlUseCase getCrawlUseCase;
    private final Gson gson = GsonSingleton.getInstance();

    public CrawlController(
            StartCrawlUseCase startCrawlUseCase,
            GetCrawlUseCase getCrawlUseCase) {
        this.startCrawlUseCase = startCrawlUseCase;
        this.getCrawlUseCase = getCrawlUseCase;
    }

    public void setupEndpoints() {
        get("/", this::handleHome);
        
        post("/crawl", this::handlePostCrawl);
        get("/crawl/:id", this::handleGetCrawl);
    }

    private String handleHome(Request req, Response res) {
        res.type("text/html");
        return renderHtml();
    }

    public String handlePostCrawl(Request req, Response res) {
        try {
            res.type("application/json");
            CrawlKeywordDTO crawlKeywordDTO = gson.fromJson(req.body(), CrawlKeywordDTO.class);
            StartCrawlDTO startCrawlDTO = startCrawlUseCase.execute(crawlKeywordDTO);
            res.status(200);
            return gson.toJson(startCrawlDTO);
        } catch (IllegalArgumentException e) {
            res.status(400);
            return gson.toJson(new ErrorResponse(400, e.getMessage()));
        } catch (Exception e) {
            return gson.toJson(new ErrorResponse(500, e.getMessage()));
        }
    }

    public String handleGetCrawl(Request req, Response res) {
        try {
            res.type("application/json");
            String id = req.params(":id");
            CrawlIdDTO crawlIdDTO = new CrawlIdDTO(id);
            GetCrawlDTO getCrawlDTO = getCrawlUseCase.execute(crawlIdDTO);
            return gson.toJson(getCrawlDTO);
        } catch (CrawlNotExistsException e) {
            res.status(404);
            return gson.toJson(new ErrorResponse(404, e.getMessage()));
        } catch (Exception e) {
            return gson.toJson(new ErrorResponse(500, e.getMessage()));
        }
    }

    private String renderHtml() {
        return """
<!DOCTYPE html>
<html>
<head>
    <title>Web Crawler</title>
    <meta charset="UTF-8">
    <style>
        /* ... estilos anteriores ... */
        
        /* Novos estilos */
        .loading-container {
            text-align: center;
            margin: 20px 0;
            padding: 20px;
            display: none;
        }
        
        .spinner {
            width: 50px;
            height: 50px;
            border: 5px solid #f3f3f3;
            border-top: 5px solid #1a73e8;
            border-radius: 50%;
            animation: spin 1s linear infinite;
            margin: 0 auto 15px auto;
        }
        
        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
        
        .status-message {
            color: #5f6368;
            font-size: 16px;
            margin-top: 10px;
        }
        
        .results-container {
            opacity: 0;
            transition: opacity 0.5s ease-in-out;
        }
        
        .results-container.visible {
            opacity: 1;
        }
        
        .progress-bar {
            width: 100%;
            height: 4px;
            background-color: #e0e0e0;
            margin-top: 10px;
            border-radius: 2px;
            overflow: hidden;
        }
        
        .progress-bar-fill {
            height: 100%;
            background-color: #1a73e8;
            width: 0%;
            transition: width 0.5s ease-in-out;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Web Crawler</h1>
        
        <div class="form-group">
            <label for="keyword">Palavra-chave para busca:</label>
            <input type="text" 
                   id="keyword" 
                   name="keyword" 
                   placeholder="Digite a palavra-chave..."
                   required>
        </div>
        
        <button id="searchButton" onclick="startCrawl()">Iniciar Busca</button>
        
        <div class="loading-container" id="loading">
            <div class="spinner"></div>
            <div class="status-message">Inicializando busca...</div>
            <div class="progress-bar">
                <div class="progress-bar-fill" id="progressBar"></div>
            </div>
        </div>
        
        <div id="results" class="results-container"></div>
    </div>

    <script>
        let checkInterval;
        let resultsBuffer = [];
        let lastUpdateTime = 0;
        const UPDATE_INTERVAL = 5000; // 5 segundos
        const LOADING_MESSAGES = [
            "Inicializando busca...",
            "Procurando URLs...",
            "Analisando conteúdo...",
            "Processando resultados...",
            "Quase lá..."
        ];
        let currentMessageIndex = 0;

        function startCrawl() {
            const keyword = document.getElementById('keyword').value;
            if (!keyword) {
                alert('Por favor, insira uma palavra-chave');
                return;
            }

            resetSearch();
            showLoading();

            fetch('/crawl', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ keyword: keyword })
            })
            .then(response => response.json())
            .then(data => {
                if (data.id) {
                    // Aguarda 2 segundos antes de começar o polling
                    setTimeout(() => startPolling(data.id), 2000);
                } else {
                    showError('Erro ao iniciar busca');
                }
            })
            .catch(error => {
                showError('Erro ao conectar com o servidor');
            });
        }

        function resetSearch() {
            if (checkInterval) clearInterval(checkInterval);
            resultsBuffer = [];
            lastUpdateTime = 0;
            currentMessageIndex = 0;
            document.getElementById('results').innerHTML = '';
            document.getElementById('results').classList.remove('visible');
        }

        function showLoading() {
            document.getElementById('loading').style.display = 'block';
            document.getElementById('searchButton').disabled = true;
            updateLoadingMessage();
        }

        function updateLoadingMessage() {
            const statusElement = document.querySelector('.status-message');
            statusElement.textContent = LOADING_MESSAGES[currentMessageIndex];
            currentMessageIndex = (currentMessageIndex + 1) % LOADING_MESSAGES.length;
        }

        function startPolling(crawlId) {
            checkInterval = setInterval(() => {
                checkResults(crawlId);
                updateLoadingMessage();
            }, UPDATE_INTERVAL);
        }

        function checkResults(crawlId) {
            fetch(`/crawl/${crawlId}`)
                .then(response => response.json())
                .then(data => {
                    if (data.status === 'done') {
                        handleDoneStatus(data);
                    } else {
                        updateProgress(data);
                    }
                })
                .catch(error => {
                    showError('Erro ao buscar resultados');
                });
        }

        function handleDoneStatus(data) {
            clearInterval(checkInterval);
            document.getElementById('loading').style.display = 'none';
            document.getElementById('searchButton').disabled = false;
            showFinalResults(data);
        }

        function updateProgress(data) {
            // Atualiza a barra de progresso (simulação)
            const progressBar = document.getElementById('progressBar');
            const currentWidth = parseInt(progressBar.style.width) || 0;
            const newWidth = Math.min(currentWidth + 10, 90);
            progressBar.style.width = newWidth + '%';
        }

        function showFinalResults(data) {
            const resultsDiv = document.getElementById('results');
            
            if (data.urls && data.urls.length > 0) {
                let html = `<h3>URLs encontradas (${data.urls.length}):</h3>`;
                html += '<ul class="url-list">';
                data.urls.forEach(url => {
                    html += `<li><a href="${url}" target="_blank">${url}</a></li>`;
                });
                html += '</ul>';
                resultsDiv.innerHTML = html;
            } else {
                resultsDiv.innerHTML = '<p>Nenhuma URL encontrada.</p>';
            }

            // Anima a exibição dos resultados
            setTimeout(() => {
                resultsDiv.classList.add('visible');
            }, 100);
        }

        function showError(message) {
            document.getElementById('loading').style.display = 'none';
            document.getElementById('searchButton').disabled = false;
            const resultsDiv = document.getElementById('results');
            resultsDiv.innerHTML = `<div class="error">${message}</div>`;
            resultsDiv.classList.add('visible');
        }
    </script>
</body>
</html>
""";
    }
}