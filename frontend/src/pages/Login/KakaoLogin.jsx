import { useEffect } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import Toast, { Success } from '../../components/Toast/Toast';

const KakaoLogin = () => {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    const accessToken = searchParams.get('access-token');

    useEffect(() => {
        if (accessToken) {
            Success('로그인 성공');
            sessionStorage.setItem('accessToken', accessToken);
            navigate('/');
        }

        return () => {};
    }, [accessToken, navigate]);

    return (
        <div>
            <Toast />
            <p>로그인</p>
        </div>
    );
};

export default KakaoLogin;
