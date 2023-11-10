import { useState } from 'react';
import axios from 'axios';
import { useLocation, useNavigate } from 'react-router-dom';
import styles from './ChooseObject.module.css';

const BASE_URL = `http://k9e203.p.ssafy.io`;

function ChooseObject() {
    const [data, setDate] = useState(null);
    const [polygon, setPolygon] = useState(null);
    const location = useLocation();
    const navigate = useNavigate();
    const origin = location.state.origin;
    const object = location.state.object;
    const timeLaps = location.state.challenge;
    const [accessToken, setAccessToken] = useState(sessionStorage.getItem('accessToken'));

    const checkObject = (x, y) => {
        var formData = new FormData();
        formData.append('snapshot', origin);
        formData.append('x', y);
        formData.append('y', x);

        axios
            .post(`${BASE_URL}/api/v1/challenges/${timeLaps.id}/snapshots/objects/choose`, formData, {
                headers: {
                    accept: 'application/json;charset=UTF-8',
                    'Content-Type': `multipart/form-data; boundary=${formData._boundary}`,
                    Authorization: `Bearer ${accessToken}`,
                },
                responseType: 'blob',
            })
            .then((response) => {
                if (response.status == 400) {
                    setDate(null);
                    alert('객체를 제대로 선택해주세요');
                } else {
                    setDate(new Blob([response.data], { type: 'image/png' }));
                    setPolygon(response.headers.get('Polygon'));
                }
            })
            .catch((error) => {
                console.log(`Error: ${error}`);
            });
    };

    const eventListener = (event) => {
        const x = event.clientX - event.target.getBoundingClientRect().left; //x축
        const y = event.clientY - event.target.getBoundingClientRect().top; //y축
        checkObject(x, y);
    };

    const save = () => {
        var formData = new FormData();
        formData.append('object  ', origin);
        formData.append('polygon', polygon);

        axios
            .post(`${BASE_URL}/api/v1/challenges/${timeLaps.id}/objects`, formData, {
                headers: {
                    accept: '*/*',
                    'Content-Type': `multipart/form-data; boundary=${formData._boundary}`,
                    Authorization: `Bearer ${accessToken}`,
                },
            })
            .then(() => {
                navigate('/');
            })
            .catch((error) => {
                console.log(`Error: ${error}`);
            });
    };

    return (
        <div className={styles.container}>
            {data == null && (
                <div
                    className={styles.video}
                    onClick={(event) => {
                        eventListener(event, 'click');
                    }}
                >
                    <img
                        src={URL.createObjectURL(object)}
                        style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                    />
                </div>
            )}
            {data != null && (
                <>
                    <div className={styles.textContainer}>포커상하고 싶은 객체가 맞습니까?</div>
                    <img src={URL.createObjectURL(data)} />
                    <div className={styles.buttonContainer}>
                        <button className={styles.continue} onClick={save}>
                            확인
                        </button>
                        <button className={styles.retry} onClick={() => setDate(null)}>
                            다시 선택
                        </button>
                    </div>
                </>
            )}
        </div>
    );
}

export default ChooseObject;
