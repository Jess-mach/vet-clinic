import './Footer.css';

export function Footer() {
  return (
    <footer id="contato" className="footer">
      <div className="container">
        <div className="footer-content">
          <div className="footer-section">
            <h3 className="footer-title">Nosso Vet</h3>
            <p className="footer-subtitle">Clínica Veterinária</p>
            <p className="footer-description">
              Compromisso com a saúde do seu pet. Cuidado em cada fase da vida.
            </p>
          </div>

          <div className="footer-section">
            <h4 className="footer-heading">Serviços</h4>
            <ul className="footer-links">
              <li><a href="#consultas">Agendamento</a></li>
              <li><a href="#consultas">Consultas</a></li>
              <li><a href="#cirurgias">Cirurgias</a></li>
              <li><a href="#exames">Exames</a></li>
              <li><a href="#vacinas">Vacinação</a></li>
            </ul>
          </div>

          <div className="footer-section">
            <h4 className="footer-heading">Sobre</h4>
            <ul className="footer-links">
              <li><a href="#sobre">Sobre o Nosso Vet</a></li>
              <li><a href="#especialidades">Especialidades</a></li>
              <li><a href="#contato">Fale conosco</a></li>
              <li><a href="#trabalhe">Trabalhe Conosco</a></li>
            </ul>
          </div>

          <div className="footer-section">
            <h4 className="footer-heading">Contato</h4>
            <div className="footer-contact">
              <p className="contact-item">
                <strong>WhatsApp:</strong>{' '}
                <a href="https://wa.me/551130033381" target="_blank" rel="noopener noreferrer">
                  (11) 3003-3381
                </a>
              </p>
              <p className="contact-item">
                <strong>Atendimento:</strong><br />
                De segunda a sexta-feira das 9h às 21h,<br />
                e aos sábados das 9h30 às 16h.
              </p>
              <p className="contact-item">
                <strong>Atendimento por telefone e WhatsApp:</strong><br />
                De segunda a sexta-feira das 9h às 19h,<br />
                e aos sábados das 8h às 13h.
              </p>
            </div>
          </div>
        </div>

        <div className="footer-social">
          <p className="footer-social-title">Acompanhe nossas redes sociais</p>
          <div className="social-links">
            <a href="#" aria-label="Instagram" className="social-link">Instagram</a>
            <a href="#" aria-label="Facebook" className="social-link">Facebook</a>
            <a href="#" aria-label="YouTube" className="social-link">YouTube</a>
            <a href="#" aria-label="LinkedIn" className="social-link">LinkedIn</a>
          </div>
        </div>

        <div className="footer-bottom">
          <div className="footer-legal">
            <p className="footer-responsible">
              <strong>Responsável técnico:</strong> Dra. Juliana Weckx Peña Muñoz - CRMV-SP 51.569
            </p>
            <p className="footer-copyright">
              Nosso Vet Clínica Veterinária - Compromisso com a saúde do seu pet | 2025 © Todos direitos reservados
            </p>
          </div>
        </div>
      </div>
    </footer>
  );
}

