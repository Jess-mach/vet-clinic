import { useEffect } from 'react';
import './ErrorModal.css';

interface ErrorModalProps {
  isOpen: boolean;
  title?: string;
  message: string;
  onClose: () => void;
  details?: Record<string, string>;
}

const TOAST_DURATION_MS = 5000;

export function ErrorModal({
  isOpen,
  title = 'Erro',
  message,
  onClose,
  details,
}: ErrorModalProps) {
  useEffect(() => {
    if (!isOpen) {
      return;
    }

    const timeoutId = setTimeout(() => {
      onClose();
    }, TOAST_DURATION_MS);

    return () => {
      clearTimeout(timeoutId);
    };
  }, [isOpen, onClose, message, details]);

  if (!isOpen) {
    return null;
  }

  const errorMessages: string[] = [];

  if (message) {
    errorMessages.push(message);
  }

  if (details) {
    Object.entries(details).forEach(([field, error]) => {
      errorMessages.push(`${field}: ${error}`);
    });
  }

  if (errorMessages.length === 0) {
    return null;
  }

  return (
    <div className="error-toast-container">
      {errorMessages.map((text, index) => (
        <div key={index} className="error-toast">
          <div className="error-toast-header">
            <div className="error-toast-title-group">
              <span className="error-toast-icon">⚠️</span>
              <span className="error-toast-title">{title}</span>
            </div>
            <button
              type="button"
              className="error-toast-close"
              onClick={onClose}
              aria-label="Fechar alerta de erro"
            >
              ×
            </button>
          </div>
          <div className="error-toast-body">
            <p>{text}</p>
          </div>
        </div>
      ))}
    </div>
  );
}

