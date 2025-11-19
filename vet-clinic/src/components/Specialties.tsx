import { Link } from 'react-router-dom';
import './Specialties.css';

interface Specialty {
  id: string;
  title: string;
  image: string;
  veterinarian: string;
}

const specialties: Specialty[] = [
  {
    id: 'clinico-geral',
    title: 'Clínico Geral',
    image: '/Clinico-geral.png',
    veterinarian: '1',
  },
  {
    id: 'cardiologia',
    title: 'Cardiologia',
    image: '/cardiologista.png',
    veterinarian: '3',
  },
  {
    id: 'neurologia',
    title: 'Neurologia',
    image: '/Neurologista.png',
    veterinarian: '5',
  },
  {
    id: 'ortopedia',
    title: 'Ortopedia',
    image: '/Ortopedista.png',
    veterinarian: '4',
  },
  {
    id: 'oftalmologia',
    title: 'Oftalmologia',
    image: '/oftamologista.png',
      veterinarian: '2',
    },
];

export function Specialties() {
  return (
    <section id="especialidades" className="section specialties">
      <div className="container">
        <div className="section-title">
          <h2>Especialidades</h2>
          <p>Contamos com profissionais especializados em diversas áreas</p>
        </div>
        <div className="specialties-grid">
          {specialties.map((specialty) => (
            <div key={specialty.id} className="specialty-card">
              <div
                className="specialty-image"
                style={{ backgroundImage: `url(${specialty.image})` }}
              >
                <div className="specialty-overlay"></div>
              </div>
              <div className="specialty-content">
                <h3 className="specialty-title">{specialty.title}</h3>
                <Link 
                  to={`/cadastrar-consulta?veterinarian=${encodeURIComponent(specialty.veterinarian)}`}
                  className="btn btn-gradient specialty-btn"
                >
                  Agendar
                </Link>
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
