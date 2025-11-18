import { useEffect } from 'react';
import { BrowserRouter, Routes, Route, useLocation } from 'react-router-dom';
import { Header } from './components/Header';
import { Hero } from './components/Hero';
import { Services } from './components/Services';
import { Specialties } from './components/Specialties';
import { Differentials } from './components/Differentials';
import { Testimonials } from './components/Testimonials';
import { Footer } from './components/Footer';
import { CreateAnimal } from './components/CreateAnimal';
import { PetsPage } from './components/PetsPage';
import { ConsultationsPage } from './components/ConsultationsPage';
import { CreateConsultation } from './components/CreateConsultation';
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
