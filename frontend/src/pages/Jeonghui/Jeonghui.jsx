import { useEffect, useRef } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import axios from 'axios';
import styles from './Jeonghui.module.css';
import PhotoCameraIcon from '@mui/icons-material/PhotoCamera';
import Webcam from 'react-webcam';
import { useState } from 'react';

const BASE_HTTP_URL = 'http://localhost:8001';

const email = 'spor1998@naver.com';

function Jeonghui() {
    const photoRef = useRef(null);
    const videoRef = useRef(null);
    const location = useLocation();
    const timeLaps = location.state;
    const navigate = useNavigate();
    const [width, setWidth] = useState(100);
    const [height, setHeight] = useState(100);
    const [ratio, setRatio] = useState(1);
    const [poly, setPoly] = useState(null);

    useEffect(() => {
        // setupWebcam();

        const containerInfo = document.querySelectorAll('div')[4];
        setWidth(containerInfo.getBoundingClientRect().width);
        setHeight(containerInfo.getBoundingClientRect().height - 1);
        setRatio(containerInfo.getBoundingClientRect().width / containerInfo.getBoundingClientRect().height);

        if (timeLaps.percent != 0) {
            // 여기서 ploy 가져오는 로직
            axios
                .get(`${BASE_HTTP_URL}/objectDetaction/getPoly/${timeLaps.id + email}`, {
                    headers: {
                        accept: '*/*',
                    },
                })
                .then((response) => {
                    console.log(response);
                    // setPoly(JSON.parse(response.data));
                    setPoly(response.data);
                })
                .catch((error) => {
                    console.error(`Error: ${error}`);
                });
        }
    }, []);

    // const setupWebcam = async () => {
    //     try {
    //         const stream = await navigator.mediaDevices.getUserMedia({
    //             video: {
    //                 // width: 1024,
    //                 // height: 600, // 해상도 설정
    //                 facingMode: 'environment', // 셀카카메라 설정. 후면카메라는 "environment"
    //             },
    //         });
    //         videoRef.current.srcObject = stream;
    //         // 비디오 로딩이 완료된 후에 play()를 호출합니다.
    //         videoRef.current.onloadedmetadata = () => {
    //             videoRef.current.play();
    //         };
    //     } catch (error) {
    //         console.log(error);
    //     }
    // };

    // const closeWebcam = () => {
    //     const stream = videoRef.current.srcObject;
    //     const tracks = stream.getTracks();

    //     tracks.forEach(function (track) {
    //         track.stop();
    //     });

    //     videoRef.current.srcObject = null;
    // };

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
        // const photo = photoRef.current;
        // const video = videoRef.current;

        // photo.width = 400;
        // photo.height = 300;

        // const ctx = photo.getContext('2d');
        // ctx.drawImage(video, 0, 0, photo.width, photo.height);

        const photo = videoRef.current.getScreenshot();

        // var dataUrl = photo.toDataURL('image/jpeg');
        // console.log(photo);
        // console.log(dataUrl);
        var blob = dataURItoBlob(photo);

        var formData = new FormData();
        formData.append('img', blob);

        if (timeLaps.percent == 0) {
            axios
                .post(`${BASE_HTTP_URL}/objectDetaction`, formData, {
                    headers: {
                        accept: '*/*',
                        'Content-Type': `multipart/form-data; boundary=${formData._boundary}`,
                    },
                    responseType: 'blob',
                })
                .then((response) => {
                    // closeWebcam();
                    navigate('/chooseObject', { state: { origin: blob, object: response.data, challenge: timeLaps } });
                })
                .catch((error) => {
                    console.error(`Error: ${error}`);
                });
        } else {
            formData.append('challengeId', timeLaps.id + email);

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
                        // closeWebcam();
                        navigate('/');
                    }
                })
                .catch((error) => {
                    console.error(`Error: ${error}`);
                });
        }
    };

    return (
        <div className={styles.container}>
            <Webcam
                height={height}
                width={width}
                videoConstraints={{ facingMode: 'environment', aspectRatio: ratio }}
                screenshotFormat="image/jpeg"
                ref={videoRef}
            />
            {/* <div className={styles.video}>
                <video ref={videoRef} style={{ width: '100vw', height: '100%', objectFit: 'cover' }}></video>
            </div> */}
            <div className={styles.camera} onClick={capture}>
                <PhotoCameraIcon style={{ width: '13vw', height: '13vw' }} />
            </div>
            <div>
                <canvas ref={photoRef} style={{ display: 'none' }}></canvas>
            </div>
            {poly && (
                <div className={styles.polyContainer}>
                    <svg height={height} width={width}>
                        <polyline points={poly} style={{ fill: 'none', stroke: 'yellow', strokeWidth: 4 }} />
                    </svg>
                </div>
            )}
        </div>
    );
}

export default Jeonghui;
