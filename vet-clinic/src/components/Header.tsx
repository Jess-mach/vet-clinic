import { useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import './Header.css';

export function Header() {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();

  const toggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  const closeMenu = () => {
    setIsMenuOpen(false);
  };

  const handleNavClick = (e: React.MouseEvent<HTMLAnchorElement>, hash: string) => {
    e.preventDefault();
    closeMenu();
    
    if (location.pathname !== '/') {
      // Se não estiver na home, navega para home com hash
      navigate(`/${hash}`);
    } else {
      // Se já estiver na home, apenas faz scroll
      const element = document.querySelector(hash);
      if (element) {
        element.scrollIntoView({ behavior: 'smooth' });
      }
    }
  };

  return (
    <header className="header">
      <div className="header-container">
        <Link to="/" className="header-logo" onClick={closeMenu}>
          <h1>Vet Clínica Cecília</h1>
          <span className="header-subtitle">Clínica Veterinária</span>
        </Link>

        <nav className={`header-nav ${isMenuOpen ? 'header-nav-open' : ''}`}>
          <ul className="nav-list">
            <li><a href="#consultas" onClick={(e) => handleNavClick(e, '#consultas')}>Consultas</a></li>
            <li><a href="#exames" onClick={(e) => handleNavClick(e, '#exames')}>Exames</a></li>
            <li><a href="#cirurgias" onClick={(e) => handleNavClick(e, '#cirurgias')}>Cirurgias</a></li>
            <li><a href="#vacinas" onClick={(e) => handleNavClick(e, '#vacinas')}>Vacinação</a></li>
            <li><a href="#especialidades" onClick={(e) => handleNavClick(e, '#especialidades')}>Especialidades</a></li>
            <li><a href="#sobre" onClick={(e) => handleNavClick(e, '#sobre')}>Sobre</a></li>
            <li><a href="#contato" onClick={(e) => handleNavClick(e, '#contato')}>Contato</a></li>
          </ul>
        </nav>

        <div className="header-actions">
          <Link to="/cadastrar-animal" className="btn btn-secondary header-cta" onClick={closeMenu}>
            Cadastre seu pet
          </Link>
          <a href="#agendar" className="btn btn-primary header-cta" onClick={closeMenu}>
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

