import './Specialties.css';

interface Specialty {
  id: string;
  title: string;
  image: string;
}

const specialties: Specialty[] = [
  {
    id: 'clinico-geral',
    title: 'Clínico Geral',
    image: 'https://via.placeholder.com/400x300?text=Clinico+Geral',
  },
  {
    id: 'cardiologia',
    title: 'Cardiologia',
    image: 'https://via.placeholder.com/400x300?text=Cardiologia',
  },
  {
    id: 'neurologia',
    title: 'Neurologia',
    image: 'https://via.placeholder.com/400x300?text=Neurologia',
  },
  {
    id: 'ortopedia',
    title: 'Ortopedia',
    image: 'https://via.placeholder.com/400x300?text=Ortopedia',
  },
  {
    id: 'oftalmologia',
    title: 'Oftalmologia',
    image: 'https://via.placeholder.com/400x300?text=Oftalmologia',
  },
];

export function Specialties() {
  return (
    <section id="especialidades" className="section specialties">
      <div className="container">
        <div className="section-title">
          <h2>Nossas Especialidades</h2>
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
                <a href={`#${specialty.id}`} className="btn btn-gradient specialty-btn">
                  Agendar
                </a>
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}

