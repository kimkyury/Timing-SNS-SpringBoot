import { useEffect, useRef } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import axios from '../../server';
import styles from './Jeonghui.module.css';
import PhotoCameraIcon from '@mui/icons-material/PhotoCamera';
import Webcam from 'react-webcam';
import { useState } from 'react';

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
    const [accessToken] = useState(sessionStorage.getItem('accessToken'));
    const [streamData, setStreamData] = useState(null);
    // const [videoData, setVideoData] = useState(null);

    useEffect(() => {
        setupWebcam();
        const containerInfo = document.querySelector('.' + styles.container);
        setWidth(containerInfo.getBoundingClientRect().width - 3);
        setHeight(containerInfo.getBoundingClientRect().height - 1);
        // setRatio(containerInfo.getBoundingClientRect().width / containerInfo.getBoundingClientRect().height);
        setRatio(containerInfo.getBoundingClientRect().height / containerInfo.getBoundingClientRect().width);

        if (timeLaps.countDays > 0) {
            // 여기서 ploy 가져오는 로직
            axios
                .get(`/api/v1/challenges/${timeLaps.id}/polygon`, {
                    headers: {
                        Authorization: `Bearer ${accessToken}`,
                    },
                })
                .then((response) => {
                    setPoly(response.data.polygon);
                })
                .catch((error) => {
                    console.error(error);
                });
        }

        return () => {
            closeWebcam();
        };
    }, []);

    const setupWebcam = async () => {
        try {
            const stream = await navigator.mediaDevices.getUserMedia({ video: true });
            videoRef.current.srcObject = stream;
            // 비디오 로딩이 완료된 후에 play()를 호출합니다.
            videoRef.current.onloadedmetadata = () => {
                videoRef.current.play();
            };
            setStreamData(videoRef.current.srcObject);
        } catch (error) {
            console.log(error);
        }
    };

    const closeWebcam = () => {
        const tracks = streamData.getTracks();

        tracks.forEach(function (track) {
            track.stop();
        });

        streamData(null);
        // videoRef.current.srcObject = null;
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

        photo.width = width;
        photo.height = height;

        const ctx = photo.getContext('2d');
        ctx.drawImage(video, 0, 0, photo.width, photo.height);

        // const photo = videoRef.current.getScreenshot();

        var dataUrl = photo.toDataURL('image/jpeg');
        var blob = dataURItoBlob(dataUrl);

        var formData = new FormData();
        formData.append('snapshot', blob);

        if (timeLaps.countDays <= 0) {
            console.log('객체 인식 실행');
            axios
                .post(`/api/v1/challenges/${timeLaps.id}/snapshots/objects/detection`, formData, {
                    headers: {
                        accept: '*/*',
                        'Content-Type': `multipart/form-data; boundary=${formData._boundary}`,
                        Authorization: `Bearer ${accessToken}`,
                    },
                    responseType: 'blob',
                })
                .then((response) => {
                    navigate('/chooseObject', { state: { origin: blob, object: response.data, challenge: timeLaps } });
                })
                .catch((error) => {
                    console.error(`Error: ${error}`);
                });
        } else {
            axios
                .post(`/api/v1/challenges/${timeLaps.id}/snapshots`, formData, {
                    headers: {
                        accept: '*/*',
                        'Content-Type': `multipart/form-data; boundary=${formData._boundary}`,
                        Authorization: `Bearer ${accessToken}`,
                    },
                })
                .then((response) => {
                    if (response.status == 400) {
                        alert('비슷한 객체를 찾지 못했습니다. 다시 사진을 찍어주세요');
                    } else {
                        navigate('/');
                    }
                })
                .catch((error) => {
                    console.error(`Error: ${error}`);
                });
        }
    };

    // const makeVideo = () => {
    //     console.log('비디오 만들자~~');
    //     axios
    //         .post(
    //             `/objectDetection/makeVideo`,
    //             {
    //                 object: 'https://kkukku-timing-21-s3.s3.ap-northeast-2.amazonaws.com/d21656a4-33dc-43b5-974d-dfb8a89443cb_blob',
    //                 snapshots:
    //                     'https://kkukku-timing-21-s3.s3.ap-northeast-2.amazonaws.com/3c659feb-d64d-4bc4-a613-681933703f24_blob, https://kkukku-timing-21-s3.s3.ap-northeast-2.amazonaws.com/ca4dcc8f-2c12-407a-ac67-71b435ca8881_blob, https://kkukku-timing-21-s3.s3.ap-northeast-2.amazonaws.com/87e6f259-4944-46dc-bbc5-39515baab8e0_blob, https://kkukku-timing-21-s3.s3.ap-northeast-2.amazonaws.com/3c659feb-d64d-4bc4-a613-681933703f24_blob, https://kkukku-timing-21-s3.s3.ap-northeast-2.amazonaws.com/ca4dcc8f-2c12-407a-ac67-71b435ca8881_blob, https://kkukku-timing-21-s3.s3.ap-northeast-2.amazonaws.com/87e6f259-4944-46dc-bbc5-39515baab8e0_blob, https://kkukku-timing-21-s3.s3.ap-northeast-2.amazonaws.com/3c659feb-d64d-4bc4-a613-681933703f24_blob, https://kkukku-timing-21-s3.s3.ap-northeast-2.amazonaws.com/ca4dcc8f-2c12-407a-ac67-71b435ca8881_blob, https://kkukku-timing-21-s3.s3.ap-northeast-2.amazonaws.com/87e6f259-4944-46dc-bbc5-39515baab8e0_blob, https://kkukku-timing-21-s3.s3.ap-northeast-2.amazonaws.com/3c659feb-d64d-4bc4-a613-681933703f24_blob, https://kkukku-timing-21-s3.s3.ap-northeast-2.amazonaws.com/ca4dcc8f-2c12-407a-ac67-71b435ca8881_blob, https://kkukku-timing-21-s3.s3.ap-northeast-2.amazonaws.com/87e6f259-4944-46dc-bbc5-39515baab8e0_blob, https://kkukku-timing-21-s3.s3.ap-northeast-2.amazonaws.com/3c659feb-d64d-4bc4-a613-681933703f24_blob, https://kkukku-timing-21-s3.s3.ap-northeast-2.amazonaws.com/ca4dcc8f-2c12-407a-ac67-71b435ca8881_blob, https://kkukku-timing-21-s3.s3.ap-northeast-2.amazonaws.com/87e6f259-4944-46dc-bbc5-39515baab8e0_blob',
    //             },
    //             {
    //                 headers: {
    //                     accept: '*/*',
    //                     Authorization: `Bearer ${accessToken}`,
    //                 },
    //                 responseType: 'blob',
    //             }
    //         )
    //         .then((response) => {
    //             console.log(response);
    //             const videoUrl = URL.createObjectURL(new Blob([response.data], { type: 'video/mp4' }));
    //             setVideoData(videoUrl);
    //         })
    //         .catch((error) => {
    //             console.log(`Error: ${error}`);
    //         });
    // };

    return (
        <>
            <div className={styles.container}>
                <video
                    ref={videoRef}
                    style={{ width: width, height: height, facingMode: 'environment', aspectRatio: ratio }}
                ></video>
                {/* <Webcam
                    height={height}
                    width={width}
                    videoConstraints={{ facingMode: 'environment', aspectRatio: ratio }}
                    screenshotFormat="image/jpeg"
                    ref={videoRef}
                /> */}
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
            {/* {videoData && (
                <div>
                    <video muted autoPlay loop>
                        <source src={videoData} type="video/mp4" />
                        Your browser does not support the video tag.
                    </video>
                </div>
            )} */}
        </>
    );
}

export default Jeonghui;
