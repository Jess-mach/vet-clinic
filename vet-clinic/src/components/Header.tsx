import { useState } from 'react';
import './Header.css';

export function Header() {
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  const toggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  const closeMenu = () => {
    setIsMenuOpen(false);
  };

  return (
    <header className="header">
      <div className="header-container">
        <div className="header-logo">
          <h1>Nosso Vet</h1>
          <span className="header-subtitle">Clínica Veterinária</span>
        </div>

        <nav className={`header-nav ${isMenuOpen ? 'header-nav-open' : ''}`}>
          <ul className="nav-list">
            <li><a href="#consultas" onClick={closeMenu}>Consultas</a></li>
            <li><a href="#exames" onClick={closeMenu}>Exames</a></li>
            <li><a href="#cirurgias" onClick={closeMenu}>Cirurgias</a></li>
            <li><a href="#vacinas" onClick={closeMenu}>Vacinação</a></li>
            <li><a href="#especialidades" onClick={closeMenu}>Especialidades</a></li>
            <li><a href="#sobre" onClick={closeMenu}>Sobre</a></li>
            <li><a href="#contato" onClick={closeMenu}>Contato</a></li>
          </ul>
        </nav>

        <div className="header-actions">
          <a href="#agendar" className="btn btn-primary header-cta">
            Agendar agora
          </a>
          <button
            className="header-menu-toggle"
            onClick={toggleMenu}
            aria-label="Toggle menu"
            aria-expanded={isMenuOpen}
          >
            <span className={`menu-icon ${isMenuOpen ? 'menu-icon-open' : ''}`}>
              <span></span>
              <span></span>
              <span></span>
            </span>
          </button>
        </div>
      </div>
    </header>
  );
}

