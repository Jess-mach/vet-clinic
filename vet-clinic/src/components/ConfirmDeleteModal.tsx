import './ConfirmDeleteModal.css';

interface ConfirmDeleteModalProps {
  isOpen: boolean;
  title?: string;
  message: string;
  onConfirm: () => void;
  onCancel: () => void;
  isLoading?: boolean;
}

export function ConfirmDeleteModal({
  isOpen,
  title = 'Confirmar Exclusão',
  message,
  onConfirm,
  onCancel,
  isLoading = false,
}: ConfirmDeleteModalProps) {
  if (!isOpen) {
    return null;
  }

  return (
    <div className="confirm-delete-modal-overlay" onClick={onCancel}>
      <div className="confirm-delete-modal" onClick={(e) => e.stopPropagation()}>
        <div className="confirm-delete-modal-header">
          <h2>⚠️ {title}</h2>
          <button
            className="confirm-delete-modal-close"
            onClick={onCancel}
            disabled={isLoading}
          >
            ×
          </button>
        </div>

        <div className="confirm-delete-modal-content">
          <p className="confirm-delete-modal-message">{message}</p>
        </div>

        <div className="confirm-delete-modal-footer">
          <button
            className="btn btn-secondary"
            onClick={onCancel}
            disabled={isLoading}
          >
            Cancelar
          </button>
          <button
            className="btn btn-danger"
            onClick={onConfirm}
            disabled={isLoading}
          >
            {isLoading ? 'Excluindo...' : 'Confirmar Exclusão'}
          </button>
        </div>
      </div>
    </div>
  );
}

