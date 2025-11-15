import { Header } from './components/Header';
import { Hero } from './components/Hero';
import { Services } from './components/Services';
import { Specialties } from './components/Specialties';
import { Differentials } from './components/Differentials';
import { Testimonials } from './components/Testimonials';
import { Footer } from './components/Footer';
import './App.css';

function App() {
  return (
    <div className="app">
      <Header />
      <main className="app-main">
        <Hero />
        <Services />
        <Specialties />
        <Differentials />
        <Testimonials />
      </main>
      <Footer />
    </div>
  );
}

export default App;
