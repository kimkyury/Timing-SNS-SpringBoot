import { Route, Routes } from "react-router-dom";

import Home from "../pages/Home/Home";
import Time from "../pages/Time/Time";

function Router() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/time" element={<Time />} />
    </Routes>
  );
}

export default Router;
