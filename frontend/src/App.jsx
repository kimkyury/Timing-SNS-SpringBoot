import Router from "../src/routes/index";
import Footer from "../src/components/Footer/Footer";
import Header from "../src/components/Header/Header";
import { useNavigate, useLocation } from "react-router-dom";

import "./App.css";

function App() {
  const location = useLocation();
  const currentUrl = location.pathname;
  return (
    <div>
      <Header />

      <Router />
      {currentUrl == "/login" ? <></> : <Footer />}
    </div>
  );
}

export default App;
