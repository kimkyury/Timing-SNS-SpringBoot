import { Route, Routes } from 'react-router-dom';

import Home from '../pages/Home/Home';
import Time from '../pages/Time/Time';
import Like from '../pages/Like/Like';
import Search from '../pages/Search/Search';
import Profile from '../pages/Profile/Profile';
import Create from '../pages/CreateFeed/CreateFeed';
import DetailComment from '../pages/DetailComment/DetailComment';
//test
import Test from '../components/SearchBar/SearchBar';
//Jeonghui
import Jeonghui from '../pages/Jeonghui/Jeonghui';
import ChooseObject from '../pages/Jeonghui/ChooseObject';
import DoChallenge from '../pages/Jeonghui/DoChallenge';

function Router() {
    return (
        <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/time" element={<Time />} />
            <Route path="/like" element={<Like />} />
            <Route path="/search" element={<Search />} />
            <Route path="/profile" element={<Profile />} />
            <Route path="/create" element={<Create />} />
            <Route path={`/detailcomment/:pk`} element={<DetailComment />} />

            {/* test */}
            <Route path="/test" element={<Test />} />
            {/* Jeonghui */}
            <Route path="/jeonghui" element={<Jeonghui />} />
            <Route path="/chooseObject" element={<ChooseObject />} />
            <Route path="/doChallenge" element={<DoChallenge />} />
        </Routes>
    );
}

export default Router;
