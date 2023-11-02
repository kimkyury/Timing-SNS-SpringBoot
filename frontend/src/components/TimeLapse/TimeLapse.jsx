import styles from './TimeLapse.module.css';
import { useNavigate } from 'react-router-dom';
import { CircularProgressbar, buildStyles, CircularProgressbarWithChildren } from 'react-circular-progressbar';
import 'react-circular-progressbar/dist/styles.css';
import dog from '../../assets/dog2.jpg';
import { useEffect, useState } from 'react';

function TimeLapse() {
    const navigate = useNavigate();
    const [timeLaps, setTimeLaps] = useState([]);
    const [isFinished, setIsFinished] = useState(false);

    useEffect(() => {
        // 여기서 axios로 timeLaps 가져올꺼임
        const state = [
            { percent: 10, name: 'test1', img: `${dog}` },
            { percent: 50, name: 'test2', img: `${dog}` },
            { percent: 70, name: 'test3', img: `${dog}` },
            { percent: 70, name: 'test3', img: `${dog}` },
            { percent: 70, name: 'test3', img: `${dog}` },
        ];

        setTimeLaps(state);
    }, []);

    const finishTimeLaps = () => {
        setIsFinished(!isFinished);
    };

    const continueTimeLaps = () => {
        // 타입랩스 이어가기 위한 로직 추가
        setIsFinished(false);
    };

    const discontinueTimeLaps = () => {
        // 타입랩스를 피드로 변환하는 로직 추가
        setIsFinished(false);
    };

    return (
        <div className={styles.container}>
            {timeLaps.map((element, index) => (
                <div key={index} className={styles.timeLaps}>
                    <div className={styles.circularContainer} onClick={finishTimeLaps}>
                        <CircularProgressbarWithChildren
                            value={element.percent}
                            strokeWidth="10"
                            styles={buildStyles({ pathColor: 'red', textColor: 'black' })}
                        >
                            <img src={element.img} className={styles.timeLapsImage} />
                        </CircularProgressbarWithChildren>
                    </div>
                </div>
            ))}
            <div className={styles.timeLaps} onClick={() => navigate(`/create`)}>
                <div className={styles.timeLaps}>
                    <CircularProgressbar
                        value="40"
                        text="+"
                        strokeWidth="10"
                        styles={buildStyles({ pathColor: 'red', textColor: 'black', textSize: '50px' })}
                    />
                </div>
            </div>

            {isFinished && (
                <div className={styles.finish}>
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
