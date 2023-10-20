import { Route, Routes } from "react-router-dom";

import Home from "../pages/Home/Home";
import Time from "../pages/Time/Time";
import Like from "../pages/Like/Like";
import Search from "../pages/Search/Search";
import Profile from "../pages/Profile/Profile";
import Create from "../pages/CreateFeed/CreateFeed"

//test
import Test from "../components/Feed/MainFeed"

function Router() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/time" element={<Time />} />
      <Route path="/like" element={<Like />} />
      <Route path="/search" element={<Search />} />
      <Route path="/profile" element={<Profile />} />
      <Route path="/create" element={<Create />} />

      {/* test */}
      <Route path="/test" element={<Test />} />

    </Routes>
  );
}

export default Router;
