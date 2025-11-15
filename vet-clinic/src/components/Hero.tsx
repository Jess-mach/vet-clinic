import './Hero.css';

export function Hero() {
  return (
    <section className="hero">
      <div className="hero-container">
        <div className="hero-content">
          <h1 className="hero-title">Vet Clínica Cecília</h1>
          <h2 className="hero-subtitle">Compromisso com a saúde do seu pet</h2>
          <p className="hero-description">
            Consultas, exames e vacinas para cuidar do seu pet em todas as fases da vida.
          </p>
          <div className="hero-actions">
            <a href="#agendar" className="btn btn-primary btn-large">
              Agendar consulta
            </a>
          </div>
        </div>
      </div>
    </section>
  );
}

