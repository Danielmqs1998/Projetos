import { Routes } from 'react-router';
import { Route } from 'react-router';
import Auth from './components/Auth';
import Home from './components/Home';
import './main.css';

function App() {
  return (
    <Routes>
      <Route path="/" element={<Auth />} />
      <Route path="/home" element={<Home />} />
    </Routes>
  );
}

export default App;
