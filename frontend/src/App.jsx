
import Router from "../src/routes/index";
import Footer from "../src/components/Footer/Footer";
import Header from "../src/components/Header/Header";
import "./App.css";

function App() {

  return (
      <div>
        <Header/>
        <Router />
        <Footer/>
      </div>
  )
}

export default App
