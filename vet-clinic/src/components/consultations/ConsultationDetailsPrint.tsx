import type { Consultation } from '../../types/consultation';

/**
 * Carrega os estilos de impressão no documento se ainda não estiverem carregados
 */
function loadPrintStyles(): void {
  const existingStyle = document.getElementById('consultation-details-print-styles');
  if (existingStyle) {
    return; // Estilos já estão carregados
  }

  const styleElement = document.createElement('style');
  styleElement.id = 'consultation-details-print-styles';
  styleElement.textContent = `
    @page {
      size: A4;
      margin: 2cm 2cm 2cm 2cm;
    }

    #consultation-details-print-container {
      font-family: 'Times New Roman', serif;
      font-size: 11pt;
      line-height: 1.4;
      color: #000;
      background: white;
      padding: 0;
    }

    #consultation-details-print-container .page-header {
      position: fixed;
      top: 0;
      left: 0;
      right: 0;
      height: 0.8cm;
      background-color: white;
      border-bottom: 1px solid #000;
      text-align: center;
      padding: 3px;
      font-size: 8pt;
      color: #000;
      z-index: 1000;
    }

    #consultation-details-print-container .page-footer {
      position: fixed;
      bottom: 0;
      left: 0;
      right: 0;
      height: 0.8cm;
      background-color: white;
      border-top: 1px solid #000;
      text-align: center;
      padding: 3px;
      font-size: 7pt;
      color: #000;
      z-index: 1000;
    }

    #consultation-details-print-container .print-content {
      margin-top: 0.8cm;
      margin-bottom: 0.8cm;
    }

    #consultation-details-print-container .print-header {
      text-align: center;
      margin-bottom: 15px;
      padding-bottom: 8px;
      border-bottom: 1px solid #000;
    }

    #consultation-details-print-container .print-header h1 {
      font-size: 14pt;
      color: #000;
      margin: 0 0 3px 0;
      font-weight: bold;
    }

    #consultation-details-print-container .print-header .subtitle {
      font-size: 9pt;
      color: #000;
      margin-bottom: 3px;
    }

    #consultation-details-print-container .print-header .consultation-id {
      font-size: 9pt;
      color: #000;
      font-weight: normal;
    }

    #consultation-details-print-container .details-section {
      margin-bottom: 12px;
      page-break-inside: avoid;
    }

    #consultation-details-print-container .details-section .section-title {
      font-size: 11pt;
      color: #000;
      margin-bottom: 8px;
      padding-bottom: 2px;
      border-bottom: 1px solid #000;
      font-weight: bold;
    }

    #consultation-details-print-container .details-grid {
      display: block;
    }

    #consultation-details-print-container .detail-row {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      margin-bottom: 5px;
      min-height: 16px;
    }

    #consultation-details-print-container .detail-row.full-width {
      display: block;
      margin-bottom: 10px;
    }

    #consultation-details-print-container .detail-label {
      font-weight: normal;
      color: #000;
      font-size: 10pt;
      margin-right: 8px;
      flex-shrink: 0;
      min-width: 120px;
    }

    #consultation-details-print-container .detail-value {
      color: #000;
      font-size: 10pt;
      flex: 1;
      text-align: left;
      border-bottom: 1px solid #ccc;
      padding-bottom: 1px;
      min-height: 12px;
    }

    #consultation-details-print-container .detail-row.full-width .detail-label {
      display: block;
      margin-bottom: 3px;
      margin-right: 0;
      min-width: auto;
      border-bottom: none;
      padding-bottom: 0;
    }

    #consultation-details-print-container .detail-row.full-width .detail-value {
      border-bottom: none;
      padding-bottom: 0;
    }

    #consultation-details-print-container .detail-text {
      color: #000;
      font-size: 10pt;
      line-height: 1.5;
      margin: 3px 0 0 0;
      padding: 2px 0;
      white-space: pre-wrap;
      word-wrap: break-word;
      text-align: left;
      border-bottom: 1px solid #ccc;
      min-height: 30px;
    }

    #consultation-details-print-container .status-badge {
      font-size: 10pt;
      font-weight: normal;
      display: inline;
      color: #000;
      border-bottom: 1px solid #ccc;
      padding-bottom: 1px;
    }

    #consultation-details-print-container .status-completed {
      color: #000;
    }

    #consultation-details-print-container .status-scheduled {
      color: #000;
    }

    #consultation-details-print-container .status-cancelled {
      color: #000;
    }

    #consultation-details-print-container .print-footer {
      margin-top: 20px;
      padding-top: 10px;
      border-top: 1px solid #000;
      text-align: left;
      font-size: 9pt;
      color: #000;
    }

    #consultation-details-print-container .print-footer .signature-section {
      margin-top: 15px;
      text-align: left;
      padding: 0;
      background-color: transparent;
      border: none;
    }

    #consultation-details-print-container .print-footer .signature-line {
      margin-top: 30px;
      padding-top: 3px;
    }

    @media print {
      body > *:not(#consultation-details-print-container) {
        display: none !important;
      }
      
      #consultation-details-print-container {
        position: relative !important;
        left: 0 !important;
        top: 0 !important;
        width: 100% !important;
      }
      
      #consultation-details-print-container .page-header,
      #consultation-details-print-container .page-footer {
        position: fixed;
      }
      
      #consultation-details-print-container .details-section {
        page-break-inside: avoid;
      }
    }
  `;
  document.head.appendChild(styleElement);
}

export function printConsultationDetails(consultation: Consultation, onError?: (message: string) => void): void {
  if (!consultation) {
    if (onError) {
      onError('Consulta não encontrada para impressão.');
    }
    return;
  }

  // Criar elemento oculto para impressão na página atual
  const printContainer = document.createElement('div');
  printContainer.id = 'consultation-details-print-container';
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

  const formatDateLong = (dateString: string | null): string => {
    if (!dateString) return 'N/A';
    try {
      const date = new Date(dateString);
      return date.toLocaleDateString('pt-BR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
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
      <span>Documento gerado automaticamente pelo sistema SysCecilia em ${currentDate}</span>
    </div>
    
    <div class="print-content">
      <div class="print-header">
        <h1>FICHA MÉDICA</h1>
        <div class="subtitle">SysCecilia - Sistema de Gestão Veterinária</div>
        <div class="consultation-id">Consulta #${consultation.id}</div>
      </div>
      
      <div class="details-section">
        <h2 class="section-title">Informações do Animal</h2>
        <div class="details-grid">
          <div class="detail-row">
            <span class="detail-label">Nome do Animal:</span>
            <span class="detail-value">${consultation.animal.name}</span>
          </div>
          <div class="detail-row">
            <span class="detail-label">Espécie:</span>
            <span class="detail-value">${consultation.animal.species}</span>
          </div>
          <div class="detail-row">
            <span class="detail-label">Raça:</span>
            <span class="detail-value">${consultation.animal.breed || 'N/A'}</span>
          </div>
          <div class="detail-row">
            <span class="detail-label">Proprietário:</span>
            <span class="detail-value">${consultation.animal.ownerName}</span>
          </div>
        </div>
      </div>

      <div class="details-section">
        <h2 class="section-title">Dados da Consulta</h2>
        <div class="details-grid">
          <div class="detail-row">
            <span class="detail-label">Data da Consulta:</span>
            <span class="detail-value">${formatDateLong(consultation.consultationDate)}</span>
          </div>
          <div class="detail-row">
            <span class="detail-label">Veterinário:</span>
            <span class="detail-value">${consultation.veterinarianName}</span>
          </div>
          <div class="detail-row">
            <span class="detail-label">Motivo:</span>
            <span class="detail-value">${consultation.reason}</span>
          </div>
          <div class="detail-row">
            <span class="detail-label">Status:</span>
            <span class="detail-value status-badge status-${consultation.status.toLowerCase()}">${formatStatus(consultation.status)}</span>
          </div>
        </div>
      </div>

      <div class="details-section">
        <h2 class="section-title">Descrição</h2>
        <div class="details-grid">
          <div class="detail-row full-width">
            <p class="detail-text">${consultation.description || 'Não informado'}</p>
          </div>
        </div>
      </div>

      <div class="details-section">
        <h2 class="section-title">Diagnóstico e Tratamento</h2>
        <div class="details-grid">
          <div class="detail-row full-width">
            <span class="detail-label">Diagnóstico:</span>
            <p class="detail-text">${consultation.diagnosis || 'Não informado'}</p>
          </div>
          <div class="detail-row full-width">
            <span class="detail-label">Tratamento:</span>
            <p class="detail-text">${consultation.treatmentPrescribed || 'Não informado'}</p>
          </div>
          <div class="detail-row full-width">
            <span class="detail-label">Observações:</span>
            <p class="detail-text">${consultation.observations || 'Sem observações'}</p>
          </div>
        </div>
      </div>

      ${consultation.nextAppointmentDate ? `
      <div class="details-section">
        <h2 class="section-title">Próxima Consulta</h2>
        <div class="details-grid">
          <div class="detail-row">
            <span class="detail-label">Data:</span>
            <span class="detail-value">${formatDateLong(consultation.nextAppointmentDate)}</span>
          </div>
        </div>
      </div>
      ` : ''}

      <div class="details-section">
        <h2 class="section-title">Informações de Auditoria</h2>
        <div class="details-grid">
          <div class="detail-row">
            <span class="detail-label">Criada em:</span>
            <span class="detail-value">${formatDate(consultation.createdAt)}</span>
          </div>
          <div class="detail-row">
            <span class="detail-label">Atualizada em:</span>
            <span class="detail-value">${formatDate(consultation.updatedAt)}</span>
          </div>
        </div>
      </div>

      <div class="print-footer">
        <p class="signature-section">
          <strong>Veterinário Responsável:</strong> ${consultation.veterinarianName}<br>
          <span class="signature-line">Assinatura: _________________________</span>
        </p>
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
      const styleToRemove = document.getElementById('consultation-details-print-styles');
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
      const styleToRemove = document.getElementById('consultation-details-print-styles');
      if (styleToRemove) {
        styleToRemove.remove();
      }
    }, 1000);
  }, 250);
}
