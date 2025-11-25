import type { Consultation } from '../../types/consultation';

/**
 * Carrega os estilos de impressão no documento se ainda não estiverem carregados
 */
function loadPrintStyles(): void {
  const existingLink = document.getElementById('consultation-recipe-print-styles-link');
  if (existingLink) {
    return; // Estilos já estão carregados
  }

  const linkElement = document.createElement('link');
  linkElement.id = 'consultation-recipe-print-styles-link';
  linkElement.rel = 'stylesheet';
  linkElement.type = 'text/css';
  linkElement.href = '/src/components/consultations/ConsultationRecipePrint.css';
  document.head.appendChild(linkElement);
}

export function printConsultationRecipe(consultation: Consultation, onError?: (message: string) => void): void {
  if (!consultation) {
    if (onError) {
      onError('Consulta não encontrada para impressão da receita.');
    }
    return;
  }

  if (!consultation.treatmentPrescribed && !consultation.diagnosis) {
    if (onError) {
      onError('Não há informações de tratamento ou diagnóstico para gerar a receita.');
    }
    return;
  }

  // Criar elemento oculto para impressão na página atual
  const printContainer = document.createElement('div');
  printContainer.id = 'consultation-recipe-print-container';
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
      });
    } catch {
      return dateString;
    }
  };

  const currentDate = new Date().toLocaleDateString('pt-BR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  });

  // Carregar estilos de impressão
  loadPrintStyles();

  // Criar conteúdo HTML
  printContainer.innerHTML = `
    <div class="page-header">
      <strong>SysCecilia - Sistema de Gestão Veterinária</strong>
    </div>
    
    <div class="print-content">
      <div class="recipe-header">
        <div class="recipe-logo">
          <h1>RECEITUÁRIO VETERINÁRIO</h1>
        </div>
      </div>
      
      <div class="recipe-patient-info">
        <div class="info-row">
          <div class="info-item">
            <strong>Animal:</strong> ${consultation.animal.name}
          </div>
          <div class="info-item">
            <strong>Espécie:</strong> ${consultation.animal.species}
          </div>
        </div>
        <div class="info-row">
          <div class="info-item">
            <strong>Raça:</strong> ${consultation.animal.breed || 'N/A'}
          </div>
          <div class="info-item">
            <strong>Proprietário:</strong> ${consultation.animal.ownerName}
          </div>
        </div>
        <div class="info-row">
          <div class="info-item">
            <strong>Data da Consulta:</strong> ${formatDateLong(consultation.consultationDate)}
          </div>
          <div class="info-item">
            <strong>Consulta #:</strong> ${consultation.id}
          </div>
        </div>
      </div>

      ${consultation.treatmentPrescribed ? `
      <div class="recipe-section">
        <h3 class="recipe-section-title">TRATAMENTO PRESCRITO</h3>
        <div class="recipe-section-content">
          <p class="treatment-text">${consultation.treatmentPrescribed}</p>
        </div>
      </div>
      ` : ''}

      ${(consultation.observations || consultation.nextAppointmentDate) ? `
      <div class="recipe-section">
        <h3 class="recipe-section-title">OBSERVAÇÕES</h3>
          ${consultation.observations ? `<p>${consultation.observations}</p>` : ''}
          ${consultation.nextAppointmentDate ? `
            ${consultation.observations ? '<br/>' : ''}
            <p>Retorno agendado para: <strong>${formatDateLong(consultation.nextAppointmentDate)}</strong></p>
          ` : ''}
      </div>
      ` : ''}



        <div class="signature-line">
          <p class="signature-space"><br/>Assinatura: _________________________________________</p>
          <p>Veterinário: ${consultation.veterinarianName}</p>
          <p class="signature-crmv">CRMV: _________________________</p>
        </div>
        <div class="signature-date">
          <p>Data: ${formatDate(consultation.consultationDate)}</p>
        </div>


      <div class="recipe-footer">
        <p class="recipe-warning">
          <strong>⚠️ ATENÇÃO:</strong> Este receituário é válido apenas para o animal e data especificados acima.
          Siga rigorosamente as instruções do veterinário. Em caso de dúvidas, entre em contato com a clínica.
        </p>
        <p class="recipe-validity">
          Este documento foi gerado automaticamente pelo sistema SysCecilia em ${currentDate} e possui validade legal.
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
      const styleToRemove = document.getElementById('consultation-recipe-print-styles-link');
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
      const styleToRemove = document.getElementById('consultation-recipe-print-styles-link');
      if (styleToRemove) {
        styleToRemove.remove();
      }
    }, 1000);
  }, 250);
}

