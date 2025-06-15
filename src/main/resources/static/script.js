let checkInterval;
let currentMessageIndex = 0;
const UPDATE_INTERVAL = 5000;
const LOADING_MESSAGES = [
    "Inicializando busca...",
    "Procurando URLs...",
    "Analisando conteúdo...",
    "Processando resultados...",
    "Quase lá..."
];

function startCrawl() {
    const keyword = document.getElementById('keyword').value;
    if (!keyword || keyword.length < 4 || keyword.length > 32) {
        showError('A palavra-chave deve ter entre 4 e 32 caracteres');
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
    .then(response => {
        if (!response.ok) {
            throw new Error('Erro na requisição');
        }
        return response.json();
    })
    .then(data => {
        if (data.id) {
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
    currentMessageIndex = 0;
    const resultsDiv = document.getElementById('results');
    resultsDiv.style.display = 'none';
    resultsDiv.innerHTML = '';
    resultsDiv.classList.remove('visible');
    document.getElementById('progressBar').style.width = '0%';
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
    .then(response => {
        if (!response.ok) {
            throw new Error('Erro ao buscar resultados');
        }
        return response.json();
    })
    .then(data => {
        if (data.status === 'done') {
            handleDoneStatus(data);
        } else if (data.status === 'active') {
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
    document.getElementById('progressBar').style.width = '100%';
    showFinalResults(data);
}

function updateProgress(data) {
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

    // Garante que o container de resultados esteja visível
    resultsDiv.style.display = 'block';
    resultsDiv.classList.add('visible');
}

function showError(message) {
    document.getElementById('loading').style.display = 'none';
    document.getElementById('searchButton').disabled = false;
    const resultsDiv = document.getElementById('results');
    resultsDiv.style.display = 'block';
    resultsDiv.classList.add('visible');
    resultsDiv.innerHTML = `<div class="error">${message}</div>`;
}