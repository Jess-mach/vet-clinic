import './ErrorModal.css';

interface ErrorModalProps {
  isOpen: boolean;
  title?: string;
  message: string;
  onClose: () => void;
  details?: Record<string, string>;
}

export function ErrorModal({
  isOpen,
  title = 'Erro',
  message,
  onClose,
  details,
}: ErrorModalProps) {
  if (!isOpen) {
    return null;
  }

  const hasDetails = details && Object.keys(details).length > 0;

  return (
    <div className="error-modal-overlay" onClick={onClose}>
      <div className="error-modal" onClick={(e) => e.stopPropagation()}>
        <div className="error-modal-header">
          <h2>⚠️ {title}</h2>
          <button className="error-modal-close" onClick={onClose}>
            ×
          </button>
        </div>

        <div className="error-modal-content">
          <p className="error-modal-message">{message}</p>

          {hasDetails && (
            <div className="error-modal-details">
              <h3>Erros de Validação:</h3>
              <ul>
                {Object.entries(details).map(([field, error]) => (
                  <li key={field}>
                    <strong>{field}:</strong> {error}
                  </li>
                ))}
              </ul>
            </div>
          )}
        </div>

        <div className="error-modal-footer">
          <button className="btn btn-primary" onClick={onClose}>
            Entendido
          </button>
        </div>
      </div>
    </div>
  );
}

