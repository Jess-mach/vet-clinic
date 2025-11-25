import './Differentials.css';

interface Differential {
  id: string;
  title: string;
  description: string;
  icon: string;
}

const differentials: Differential[] = [
  {
    id: 'equipe',
    title: 'Equipe multidisciplinar',
    description: 'Contamos com profissionais de diversas especialidades, para um cuidado completo.',
    icon: '👥',
  },
  {
    id: 'atendimento',
    title: 'Atendimento humanizado',
    description: 'Atuamos com profissionais de referência no mercado que cuidam da saúde integral dos nossos pacientes.',
    icon: '❤️',
  },
  {
    id: 'localizacao',
    title: 'Localização acessível',
    description: 'Estamos localizados na Centro, com estacionamento próprio, e facil acesso.',
    icon: '📍',
  },
  {
    id: 'cuidado',
    title: 'Cuidado em todas as fases',
    description: 'Acompanhamos a saúde do seu pet em todas as fases da vida.',
    icon: '🐾',
  },
];

export function Differentials() {
  return (
    <section id="sobre" className="section differentials">
      <div className="container">
        <div className="section-title">
          <h2>Nossos Diferenciais</h2>
          <p>O que nos torna únicos no cuidado com seu pet</p>
        </div>
        <div className="differentials-grid">
          {differentials.map((differential) => (
            <div key={differential.id} className="differential-card">
              <div className="differential-icon">{differential.icon}</div>
              <h3 className="differential-title">{differential.title}</h3>
              <p className="differential-description">{differential.description}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}

