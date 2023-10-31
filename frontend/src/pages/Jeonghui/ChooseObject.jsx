import { useState } from 'react';
import axios from 'axios';
import { useLocation, useNavigate } from 'react-router-dom';
import styles from './Jeonghui.module.css';

const BASE_HTTP_URL = 'http://localhost:8001';

const challengeId = 'kjh';

function ChooseObject() {
    const [data, setDate] = useState(null);
    const location = useLocation();
    const navigate = useNavigate();
    const origin = location.state.origin;
    const object = location.state.object;

    const checkObject = (x, y) => {
        var formData = new FormData();
        formData.append('img', origin);
        formData.append('x', x);
        formData.append('y', y);

        axios
            .post(`${BASE_HTTP_URL}/objectDetaction/chooseObject`, formData, {
                headers: {
                    accept: '*/*',
                    'Content-Type': `multipart/form-data; boundary=${formData._boundary}`,
                },
                responseType: 'blob',
            })
            .then((response) => {
                console.log(response);
                if (response.status == 204) {
                    setDate(null);
                    alert('객체를 제대로 선택해주세요');
                } else {
                    setDate(response.data);
                }
            })
            .catch((error) => {
                console.log(`Error: ${error}`);
            });
    };

    const eventListener = (event) => {
        const x = event.clientX - event.target.offsetLeft; //x축
        const y = event.clientY - event.target.offsetTop; //y축
        console.log(x, y);
        console.log('client', event.clientX, event.clientY);
        console.log('target', event.target.offsetTop, event.target.offsetLeft);
        checkObject(x, y);
    };

    const save = () => {
        var formData = new FormData();
        formData.append('img', data);
        formData.append('challengeId', challengeId);

        axios
            .post(`${BASE_HTTP_URL}/objectDetaction/objectSave`, formData, {
                headers: {
                    accept: '*/*',
                    'Content-Type': `multipart/form-data; boundary=${formData._boundary}`,
                },
            })
            .then((response) => {
                console.log(response);
                navigate('/doChallenge');
            })
            .catch((error) => {
                console.error(`Error: ${error}`);
            });
    };

    return (
        <div className={styles.container}>
            <div className={styles.video}>
                <img
                    src={URL.createObjectURL(object)}
                    style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                    onClick={(event) => {
                        eventListener(event, 'click');
                    }}
                />
            </div>
            {data != null && (
                <>
                    <img src={URL.createObjectURL(data)} />
                    <button onClick={save}>확인</button>
                </>
            )}
        </div>
    );
}

export default ChooseObject;
