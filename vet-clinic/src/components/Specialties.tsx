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
    veterinarian: 'Luna Lovegood',
  },
  {
    id: 'cardiologia',
    title: 'Cardiologia',
    image: '/cardiologista.png',
    veterinarian: 'Dr. Minerva McGonagall',
  },
  {
    id: 'neurologia',
    title: 'Neurologia',
    image: '/Neurologista.png',
    veterinarian: 'Dra. Albus Dumbledore',
  },
  {
    id: 'ortopedia',
    title: 'Ortopedia',
    image: '/Ortopedista.png',
    veterinarian: 'Dr. Remus Lupin',
  },
  {
    id: 'oftalmologia',
    title: 'Oftalmologia',
    image: '/oftamologista.png',
    veterinarian: 'Dra. Hermione Granger',
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
