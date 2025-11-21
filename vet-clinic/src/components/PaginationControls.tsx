import React from 'react';
import './PaginationControls.css';

interface PaginationControlsProps {
  currentPage: number;
  totalPages: number;
  totalElements: number;
  onFirstPage?: () => void;
  onPreviousPage?: () => void;
  onNextPage?: () => void;
  onLastPage?: () => void;
  disabled?: boolean;
  showFirstLastButtons?: boolean;
}

export const PaginationControls: React.FC<PaginationControlsProps> = ({
  currentPage,
  totalPages,
  totalElements,
  onFirstPage,
  onPreviousPage,
  onNextPage,
  onLastPage,
  disabled = false,
  showFirstLastButtons = true,
}) => {
  return (
    <div className="pagination-controls-wrapper">
      <div className="pagination-info">
        <span>
          Página {currentPage + 1} de {totalPages}
        </span>
        <span className="pagination-separator">•</span>
        <span>Total: {totalElements} registros</span>
      </div>

      <div className="pagination-controls">
        {showFirstLastButtons && (
          <button
            className="pagination-btn"
            onClick={onFirstPage}
            disabled={currentPage === 0 || disabled}
            title="Primeira página"
          >
            ⏮
          </button>
        )}

        <button
          className="pagination-btn"
          onClick={onPreviousPage}
          disabled={currentPage === 0 || disabled}
          title="Página anterior"
        >
          ← Anterior
        </button>

        <span className="pagination-page-info">
          {currentPage + 1} / {totalPages}
        </span>

        <button
          className="pagination-btn"
          onClick={onNextPage}
          disabled={currentPage >= totalPages - 1 || disabled}
          title="Próxima página"
        >
          Próxima →
        </button>

        {showFirstLastButtons && (
          <button
            className="pagination-btn"
            onClick={onLastPage}
            disabled={currentPage >= totalPages - 1 || disabled}
            title="Última página"
          >
            ⏭
          </button>
        )}
      </div>
    </div>
  );
};

