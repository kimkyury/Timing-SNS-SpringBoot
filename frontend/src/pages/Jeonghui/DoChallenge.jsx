import { useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import styles from './Jeonghui.module.css';

const BASE_HTTP_URL = 'http://localhost:8001';

const challengeId = 'kjh';

function DoChallenge() {
    const photoRef = useRef(null);
    const videoRef = useRef(null);
    const navigate = useNavigate();

    useEffect(() => {
        setupWebcam();
    }, []);

    const setupWebcam = async () => {
        try {
            const stream = await navigator.mediaDevices.getUserMedia({ video: true });
            videoRef.current.srcObject = stream;
            // 비디오 로딩이 완료된 후에 play()를 호출합니다.
            videoRef.current.onloadedmetadata = () => {
                videoRef.current.play();
            };
        } catch (error) {
            console.log(error);
        }
    };

    const closeWebcam = () => {
        const stream = videoRef.current.srcObject;
        const tracks = stream.getTracks();

        tracks.forEach(function (track) {
            track.stop();
        });

        videoRef.current.srcObject = null;
    };

    const dataURItoBlob = (dataURI) => {
        var byteString = atob(dataURI.split(',')[1]);
        var ab = new ArrayBuffer(byteString.length);
        var ia = new Uint8Array(ab);
        for (var i = 0; i < byteString.length; i++) {
            ia[i] = byteString.charCodeAt(i);
        }

        var bb = new Blob([ab], { type: 'image/jpeg' });
        return bb;
    };

    const capture = () => {
        console.log('capture');
        const photo = photoRef.current;
        const video = videoRef.current;

        photo.width = 400;
        photo.height = 300;

        const ctx = photo.getContext('2d');
        ctx.drawImage(video, 0, 0, photo.width, photo.height);

        var dataUrl = photo.toDataURL('image/jpeg');
        var blob = dataURItoBlob(dataUrl);

        var formData = new FormData();
        formData.append('img', blob);
        formData.append('challengeId', challengeId);

        axios
            .post(`${BASE_HTTP_URL}/objectDetaction/similarity`, formData, {
                headers: {
                    accept: '*/*',
                    'Content-Type': `multipart/form-data; boundary=${formData._boundary}`,
                },
            })
            .then((response) => {
                console.log(response);
                if (response.status == 204) {
                    alert('비슷한 객체를 찾지 못했습니다. 다시 사진을 찍어주세요');
                } else {
                    closeWebcam();
                    navigate('/');
                }
            })
            .catch((error) => {
                console.error(`Error: ${error}`);
            });
    };

    return (
        <div className={styles.container}>
            <div className={styles.video}>
                <video ref={videoRef} style={{ width: '100%', height: '100%', objectFit: 'cover' }}></video>
            </div>
            <button onClick={capture}>사진 찍기</button>
            <div>
                <canvas ref={photoRef} style={{ display: 'none' }}></canvas>
            </div>
        </div>
    );
}

export default DoChallenge;
