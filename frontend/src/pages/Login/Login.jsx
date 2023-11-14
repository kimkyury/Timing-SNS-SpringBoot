import styles from './Login.module.css';
import kakao from '../../assets/kakao.png';
import logo from '../../assets/logo.png';
import React, { useEffect, useState } from 'react';
import axios from 'axios';

function Login() {
    const BASE_URL = `http://k9e203.p.ssafy.io`;
    const [accessToken, setAccessToken] = useState(null);
    const gotokakao = () => {
        window.location.href = `${BASE_URL}/oauth2/authorization/kakao`;
    };
    useEffect(() => {
        if (sessionStorage.getItem('accessToken')) {
            setAccessToken(sessionStorage.getItem('accessToken'));
        }

        return () => {};
    }, []);
    const handleLogout = () => {
        axios
            .post(
                `${BASE_URL}/api/v1/auth/logout`,
                {},
                {
                    headers: {
                        Authorization: `Bearer ${accessToken}`,
                    },
                    withCredentials: true,
                }
            )
            .then((response) => {
                console.log(response);
                sessionStorage.removeItem('accessToken');
                setAccessToken(null);
            })
            .catch((error) => {
                console.error(error);
            });
    };

    const handleReissue = () => {
        axios
            .post(
                `${BASE_URL}/api/v1/auth/reissue`,
                {},
                {
                    withCredentials: true,
                }
            )
            .then((response) => {
                console.log(response);
                sessionStorage.setItem('accessToken', response.data.accessToken);
                setAccessToken(response.data.accessToken);
            })
            .catch((error) => {
                console.error(error);
            });
    };
    return (
        <div className={styles.container}>
            <div className={styles.upppertitle}>21일 동안의 타임랩스로</div>
            <div className={styles.lowertitle}>당신만의 습관을 기르세요</div>

            <img src={kakao} className={styles.loginbtn} onClick={() => gotokakao()} />
            <div>
                <img src={logo} className={styles.logoimg} />
            </div>
            {/* <div onClick={handleLogout}>dsfsdf</div> */}
        </div>
    );
}

export default Login;
