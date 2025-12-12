import { useEffect } from 'react';
import { BrowserRouter, Routes, Route, useLocation } from 'react-router-dom';
import { Header } from './components/shared/Header';
import { Footer } from './components/shared/Footer';
import { Hero } from './components/landing/Hero';
import { Services } from './components/landing/Services';
import { Specialties } from './components/landing/Specialties';
import { Differentials } from './components/landing/Differentials';
import { Testimonials } from './components/landing/Testimonials';
import { CreateAnimal } from './components/animals/CreateAnimal';
import { PetsPage } from './components/animals/PetsPage';
import { ConsultationsPage } from './components/consultations/ConsultationsPage';
import { CreateConsultation } from './components/consultations/CreateConsultation';
import './App.css';

function ScrollToHash() {
  const location = useLocation();

  useEffect(() => {
    if (location.hash) {
      const element = document.querySelector(location.hash);
      if (element) {
        // Pequeno delay para garantir que o componente foi renderizado
        setTimeout(() => {
          element.scrollIntoView({ behavior: 'smooth' });
        }, 100);
      }
    } else {
      // Se não houver hash, scroll para o topo
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  }, [location]);

  return null;
}

function HomePage() {
  return (
    <>
      <Hero />
      <Services />
      <Specialties />
      <Differentials />
      <Testimonials />
    </>
  );
}

function AppContent() {
  return (
    <>
      <ScrollToHash />
      <div className="app">
        <Header />
        <main className="app-main">
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/cadastrar-animal" element={<CreateAnimal />} />
            <Route path="/pets" element={<PetsPage />} />
            <Route path="/consultas" element={<ConsultationsPage />} />
            <Route path="/cadastrar-consulta" element={<CreateConsultation />} />
            <Route path="/consultas/:id/editar" element={<CreateConsultation />} />
          </Routes>
        </main>
        <Footer />
      </div>
    </>
  );
}

function App() {
  return (
    <BrowserRouter>
      <AppContent />
    </BrowserRouter>
  );
}

export default App;
