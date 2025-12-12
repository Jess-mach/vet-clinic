import type { Consultation } from '../../types/consultation';

/**
 * Carrega os estilos de impressão no documento se ainda não estiverem carregados
 * Cria um link tag que aponta para o arquivo CSS
 */
function loadPrintStyles(): void {
  const existingLink = document.getElementById('animal-consultation-history-print-styles-link');
  if (existingLink) {
    return; // Estilos já estão carregados
  }

  // Criar link tag para carregar o CSS
  // O Vite processará o caminho corretamente durante o build
  const linkElement = document.createElement('link');
  linkElement.id = 'animal-consultation-history-print-styles-link';
  linkElement.rel = 'stylesheet';
  linkElement.type = 'text/css';
  // Usar um caminho relativo que será resolvido pelo bundler
  linkElement.href = '/src/components/animals/AnimalConsultationHistoryPrint.css';
  document.head.appendChild(linkElement);
}

export function printConsultations(consultations: Consultation[], onError?: (message: string) => void): void {
  if (consultations.length === 0) {
    if (onError) {
      onError('Não há consultas para imprimir.');
    }
    return;
  }

  // Criar elemento oculto para impressão na página atual
  const printContainer = document.createElement('div');
  printContainer.id = 'animal-consultation-history-print-container';
  printContainer.style.position = 'absolute';
  printContainer.style.left = '-9999px';
  printContainer.style.top = '0';
  printContainer.style.width = '210mm'; // Largura A4
  document.body.appendChild(printContainer);

  const formatDate = (dateString: string | null): string => {
    if (!dateString) return 'N/A';
    try {
      const date = new Date(dateString);
      return date.toLocaleDateString('pt-BR', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
      });
    } catch {
      return dateString;
    }
  };

  const formatStatus = (status: string) => {
    const statusMap: Record<string, string> = {
      COMPLETED: 'Concluída',
      SCHEDULED: 'Agendada',
      CANCELLED: 'Cancelada',
    };
    return statusMap[status] || status;
  };

  const currentDate = new Date().toLocaleDateString('pt-BR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });

  // Carregar estilos de impressão
  loadPrintStyles();

  // Criar conteúdo HTML
  printContainer.innerHTML = `
    <div class="page-header">
      <strong>SysCecilia - Sistema de Gestão Veterinária</strong>
    </div>
    
    <div class="page-footer">
      <span>Documento gerado automaticamente pelo sistema SysCecilia</span>
    </div>
    
    <div class="print-content">
      <div class="print-header">
        <h1>Lista de Consultas Veterinárias</h1>
        <div class="subtitle">SysCecilia - Sistema de Gestão Veterinária</div>
      </div>
      
      <div class="print-info">
        <strong>Data de Impressão:</strong> ${currentDate} | 
        <strong>Total de Consultas:</strong> ${consultations.length}
      </div>
      
      <table class="print-table">
        <thead>
          <tr>
            <th style="width: 5%;">ID</th>
            <th style="width: 15%;">Animal</th>
            <th style="width: 15%;">Proprietário</th>
            <th style="width: 12%;">Data</th>
            <th style="width: 15%;">Veterinário</th>
            <th style="width: 15%;">Motivo</th>
            <th style="width: 10%;">Status</th>
            <th style="width: 13%;">Espécie</th>
          </tr>
        </thead>
        <tbody>
          ${consultations.map((consultation) => `
            <tr>
              <td>${consultation.id}</td>
              <td>${consultation.animal.name}</td>
              <td>${consultation.animal.ownerName}</td>
              <td>${formatDate(consultation.consultationDate)}</td>
              <td>${consultation.veterinarianName}</td>
              <td>${consultation.reason || 'N/A'}</td>
              <td>
                <span class="status-badge status-${consultation.status.toLowerCase()}">
                  ${formatStatus(consultation.status)}
                </span>
              </td>
              <td>${consultation.animal.species}${consultation.animal.breed ? ' - ' + consultation.animal.breed : ''}</td>
            </tr>
            ${consultation.status === 'COMPLETED' ? `
            <tr class="detail-row">
              <td colspan="8" class="detail-cell" style="width: 100%; padding: 8px;">
                <table class="detail-table" style="width: 100%;">
                  <tr>
                    <td class="detail-label">Diagnóstico:</td>
                    <td class="detail-value">${consultation.diagnosis || 'N/A'}</td>
                  </tr>
                  <tr>
                    <td class="detail-label">Observações:</td>
                    <td class="detail-value">${consultation.observations || 'N/A'}</td>
                  </tr>
                  <tr>
                    <td class="detail-label">Receita:</td>
                    <td class="detail-value">${consultation.treatmentPrescribed || 'N/A'}</td>
                  </tr>
                </table>
              </td>
            </tr>
            ` : ''}
          `).join('')}
        </tbody>
      </table>
      
      <div class="print-footer">
        <p>Este documento contém informações confidenciais e é destinado apenas ao uso interno.</p>
      </div>
    </div>
  `;

  // Aguardar um momento para o conteúdo ser renderizado
  setTimeout(() => {
    window.print();
    
    // Remover o elemento após a impressão
    const handleAfterPrint = () => {
      if (printContainer && printContainer.parentNode) {
        printContainer.parentNode.removeChild(printContainer);
      }
      const styleToRemove = document.getElementById('animal-consultation-history-print-styles');
      if (styleToRemove) {
        styleToRemove.remove();
      }
      window.removeEventListener('afterprint', handleAfterPrint);
    };
    
    window.addEventListener('afterprint', handleAfterPrint);
    
    // Fallback: remover após um tempo caso o evento não seja disparado
    setTimeout(() => {
      if (printContainer && printContainer.parentNode) {
        printContainer.parentNode.removeChild(printContainer);
      }
      const styleToRemove = document.getElementById('animal-consultation-history-print-styles');
      if (styleToRemove) {
        styleToRemove.remove();
      }
    }, 1000);
  }, 250);
}

