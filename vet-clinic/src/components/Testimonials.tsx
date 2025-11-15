import './Testimonials.css';

interface Testimonial {
  id: number;
  petName: string;
  tutorName: string;
  comment: string;
}

const testimonials: Testimonial[] = [
  {
    id: 1,
    petName: 'Fridoca',
    tutorName: 'Yvina Andrade F.',
    comment: 'Fridoca foi muito bem atendida. Os médicos e alunos são muito atenciosos e dedicados. Recomendo 1000%!',
  },
  {
    id: 2,
    petName: '',
    tutorName: 'Gabriela Alves Gonçalves',
    comment: 'Ótimos profissionais e estagiários muito pró-ativos! Completinho e limpo!',
  },
  {
    id: 3,
    petName: '',
    tutorName: 'Aderizio Gonçalves',
    comment: 'Excelente serviço! Meu gato foi muito bem atendido. 😺🐈😻',
  },
  {
    id: 4,
    petName: '',
    tutorName: 'Gustavo Gumieiro',
    comment: 'Melhor atendimento da vida! Cuidaram muito bem do meu gato, foram super atenciosos. Estávamos preocupados, mas o atendimento deles acalmou a mim e a minha esposa.',
  },
  {
    id: 5,
    petName: '',
    tutorName: 'Andrea Gomes',
    comment: 'Excelente o atendimento, todos são muito atenciosos.',
  },
];

export function Testimonials() {
  return (
    <section id="depoimentos" className="section testimonials">
      <div className="container">
        <div className="section-title">
          <h2>Depoimentos</h2>
          <p>Confira o que estão falando sobre nós</p>
        </div>
        <div className="testimonials-grid">
          {testimonials.map((testimonial) => (
            <div key={testimonial.id} className="testimonial-card">
              <div className="testimonial-content">
                <p className="testimonial-comment">"{testimonial.comment}"</p>
              </div>
              <div className="testimonial-author">
                {testimonial.petName && (
                  <span className="testimonial-pet">{testimonial.petName}</span>
                )}
                <span className="testimonial-tutor">Tutor: {testimonial.tutorName}</span>
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}

