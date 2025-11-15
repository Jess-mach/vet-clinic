import './Services.css';

interface Service {
  id: string;
  title: string;
  description: string;
  icon: string;
}

const services: Service[] = [
  {
    id: 'consultas',
    title: 'Consultas',
    description: 'Atendimento médico completo para seu pet',
    icon: '🩺',
  },
  {
    id: 'exames',
    title: 'Exames',
    description: 'Exames de imagem, laboratoriais e cardiológicos',
    icon: '🔬',
  },
  {
    id: 'cirurgias',
    title: 'Cirurgias',
    description: 'Procedimentos cirúrgicos com segurança',
    icon: '⚕️',
  },
  {
    id: 'vacinas',
    title: 'Vacinas',
    description: 'Vacinação completa para proteger seu pet',
    icon: '💉',
  },
];

export function Services() {
  return (
    <section id="servicos" className="section services">
      <div className="container">
        <div className="section-title">
          <h2>Nossos Serviços</h2>
          <p>Oferecemos cuidados completos para a saúde do seu pet</p>
        </div>
        <div className="services-grid">
          {services.map((service) => (
            <div key={service.id} id={service.id} className="service-card">
              <div className="service-icon">{service.icon}</div>
              <h3 className="service-title">{service.title}</h3>
              <p className="service-description">{service.description}</p>
              <a href={`#${service.id}`} className="service-link">
                Saiba mais →
              </a>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}

