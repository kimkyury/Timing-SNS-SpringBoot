import styles from './TimeLapse.module.css';
import { useNavigate } from 'react-router-dom';
import { CircularProgressbar, buildStyles, CircularProgressbarWithChildren } from 'react-circular-progressbar';
import 'react-circular-progressbar/dist/styles.css';
import { useEffect, useState } from 'react';
import ConfettiExplosion from 'react-confetti-explosion';
import axios from '../../server';
function TimeLapse() {
    const navigate = useNavigate();
    const [timeLaps, setTimeLaps] = useState([]);
    const [finished, setFinished] = useState(null);

    const [accessToken] = useState(sessionStorage.getItem('accessToken'));

    const takePhoto = (element) => {
        navigate(`/jeonghui`, { state: element });
    };

    const continueTimeLaps = () => {
        // 타입랩스 이어가기
        setFinished(null);
    };

    const discontinueTimeLaps = () => {
        // 타입랩스를 피드로 변환
        axios.post(`/api/v1/feeds`, { challengeId: finished });
        setFinished(null);
    };
    const largeProps = {
        force: 0.8,
        duration: 3000,
        particleCount: 300,
        width: 1600,
        colors: ['#041E43', '#1471BF', '#5BB4DC', '#FC027B', '#66D805'],
    };
    const getChallenge = () => {
        axios
            .get(`/api/v1/challenges`)
            .then((response) => {
                setTimeLaps(response.data.challenges);

                for (let i = 0; i < response.data.challenges.length; i++) {
                    if (response.data.challenges[i].countDays == 21) {
                        setFinished(response.data.challenges[i].id);
                        break;
                    }
                }
            })
            .catch((error) => {
                console.error(error);
            });
    };

    useEffect(() => {
        getChallenge();
    }, []);

    return (
        <div className={styles.container}>
            {timeLaps.length != 0 ? (
                <>
                    {timeLaps.map((element, index) => (
                        <div key={index} className={styles.timeLaps}>
                            <div className={styles.circularContainer} onClick={() => takePhoto(element)}>
                                <CircularProgressbarWithChildren
                                    value={element.countDays / element.maxDays}
                                    strokeWidth="10"
                                    styles={buildStyles({ pathColor: 'red', textColor: 'black' })}
                                >
                                    <img src={element.thumbnailUrl} className={styles.timeLapsImage} />
                                </CircularProgressbarWithChildren>
                            </div>
                        </div>
                    ))}
                </>
            ) : (
                <></>
            )}
            <div className={styles.timeLaps} onClick={() => navigate(`/create`)}>
                <div className={styles.timeLaps}>
                    <CircularProgressbar
                        value="100"
                        text="+"
                        strokeWidth="10"
                        styles={buildStyles({
                            pathColor: 'yellow',
                            textColor: 'black',
                            textSize: '50px',
                        })}
                    />
                </div>
            </div>

            {finished && (
                <div className={styles.finish}>
                    <ConfettiExplosion {...largeProps} />
                    <div className={styles.finishContainer}>
                        <div>타임랩스를 완료했습니다!!</div>
                        <div>이어하시면 21일이 추가됩니다.</div>
                        <div>이어하시겠습니까?</div>
                        <div className={styles.buttonContainer}>
                            <button className={styles.continueTimeLaps} onClick={continueTimeLaps}>
                                예
                            </button>
                            <button className={styles.discontinueTimeLaps} onClick={discontinueTimeLaps}>
                                아니요
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

export default TimeLapse;
