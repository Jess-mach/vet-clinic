import { useNavigate } from 'react-router-dom';
import './Hero.css';

export function Hero() {
  const navigate = useNavigate();

  return (
    <section className="hero">
      <div className="hero-container">
        <div className="hero-content">
          <h1 className="hero-title">Sys Cecília</h1>
          <h2 className="hero-subtitle">Compromisso com a saúde do seu pet</h2>
          <p className="hero-description">
            Consultas, exames e vacinas para cuidar do seu pet em todas as fases da vida.
          </p>
         
        </div>
        <div className="hero-image-placeholder">
          <img src="/hero-pet1.png" alt="Pet feliz na clínica veterinária" />
        </div>
      </div>
    </section>
  );
}

