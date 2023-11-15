import { Route, Routes } from 'react-router-dom';
import Home from '../pages/Home/Home';
import Time from '../pages/Time/Time';
import Like from '../pages/Like/Like';
import Search from '../pages/Search/Search';
import Profile from '../pages/Profile/Profile';
import Create from '../pages/CreateFeed/CreateFeed';
import DetailComment from '../pages/DetailComment/DetailComment';
import Tree from '../components/Tree/TreeGraph';
import Jeonghui from '../pages/Jeonghui/Jeonghui';
import ChooseObject from '../pages/Jeonghui/ChooseObject';
import DetailFeed from '../pages/DetailFeed/DetailFeed';
import Login from '../pages/Login/Login';
import UpdateReview from '../pages/UpdateReview/UpdateReview';
import KakaoLogin from '../pages/Login/KakaoLogin';
import PrivateRoute from './PrivateRoute';
import UpdateProfile from '../pages/UpdateProfile/UpdateProfile';
import PullToRefresh from '../components/PullToRefresh';
import Influence from '../pages/Influence/Influence';

function Router() {
    return (
        <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/login/oauth2/redirect/kakao" element={<KakaoLogin />} />
            <Route path={`/influence`} element={<Influence />} />
            <Route element={<PrivateRoute />}>
                <Route path="/" element={<Home />} />
                <Route path="/time" element={<Time />} />
                <Route path="/like" element={<Like />} />
                <Route path="/search" element={<Search />} />
                <Route path="/profile" element={<Profile />} />
                <Route path={`/profile/?email=:email`} element={<Profile />} />
                <Route path="/create" element={<Create />} />
                <Route path="/tree" element={<Tree />} />
                <Route path={`/detailcomment/:pk`} element={<DetailComment />} />
                <Route path={`/detailfeed/:pk`} element={<DetailFeed />} />
                <Route path={`/updatereview/:pk`} element={<UpdateReview />} />
                <Route path={`/updateProfile`} element={<UpdateProfile />} />

                {/* test */}
                <Route path="/test" element={<PullToRefresh />} />
                {/* Jeonghui */}
                <Route path="/jeonghui" element={<Jeonghui />} />
                <Route path="/chooseObject" element={<ChooseObject />} />
                {/* <Route path="/doChallenge" element={<DoChallenge />} /> */}
            </Route>
        </Routes>
    );
}

export default Router;
