import { useState } from 'react';
import axios from 'axios';
import { useLocation, useNavigate } from 'react-router-dom';
import styles from './ChooseObject.module.css';

const BASE_HTTP_URL = 'http://localhost:8001';

const email = 'spor1998@naver.com';

function ChooseObject() {
    const [data, setDate] = useState(null);
    const location = useLocation();
    const navigate = useNavigate();
    const origin = location.state.origin;
    const object = location.state.object;
    const timeLaps = location.state.challenge;

    const checkObject = (x, y) => {
        var formData = new FormData();
        formData.append('img', origin);
        formData.append('x', y);
        formData.append('y', x);
        formData.append('challengeId', timeLaps.id + email);

        axios
            .post(`${BASE_HTTP_URL}/objectDetaction/chooseObject`, formData, {
                headers: {
                    accept: '*/*',
                    'Content-Type': `multipart/form-data; boundary=${formData._boundary}`,
                },
                // responseType: 'blob',
            })
            .then((response) => {
                console.log(response);
                console.log(response.data);
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
        const x = event.clientX - event.target.getBoundingClientRect().left; //x축
        const y = event.clientY - event.target.getBoundingClientRect().top; //y축
        // console.log('position', x, y);
        checkObject(x, y);
    };

    const save = () => {
        var formData = new FormData();
        formData.append('img', data);
        formData.append('challengeId', timeLaps.id + email);

        axios
            .post(`${BASE_HTTP_URL}/objectDetaction/objectSave`, formData, {
                headers: {
                    accept: '*/*',
                    'Content-Type': `multipart/form-data; boundary=${formData._boundary}`,
                },
            })
            .then(() => {
                navigate('/');
            })
            .catch((error) => {
                console.error(`Error: ${error}`);
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
                        // onClick={(event) => {
                        //     eventListener(event, 'click');
                        // }}
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
