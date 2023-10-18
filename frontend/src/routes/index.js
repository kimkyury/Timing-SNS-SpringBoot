import { Route, Routes } from 'react-router-dom';

import Home from '../pages/Home/Home';
import Login from '../pages/Login/Login';

function Router() {
    return (
        <>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/login" element={<Login />} />

                <Route path="*" element={<div>없는 페이지</div>} />
            </Routes>
        </>
    );
}

export default Router;
