import styles from './TimeLapse.module.css';
import { useNavigate } from 'react-router-dom';
import { CircularProgressbar, buildStyles, CircularProgressbarWithChildren } from 'react-circular-progressbar';
import 'react-circular-progressbar/dist/styles.css';
import dog from '../../assets/dog2.jpg';
import { useEffect, useState } from 'react';

function TimeLapse() {
    const navigate = useNavigate();
    const [timeLaps, setTimeLaps] = useState([]);

    useEffect(() => {
        // 여기서 timeLaps 가져올꺼임
        const state = [
            { percent: 10, name: 'test1', img: `${dog}` },
            { percent: 50, name: 'test2', img: `${dog}` },
            { percent: 70, name: 'test3', img: `${dog}` },
            { percent: 70, name: 'test3', img: `${dog}` },
            { percent: 70, name: 'test3', img: `${dog}` },
            { percent: 70, name: 'test3', img: `${dog}` },
            { percent: 70, name: 'test3', img: `${dog}` },
            { percent: 70, name: 'test3', img: `${dog}` },
            { percent: 70, name: 'test3', img: `${dog}` },
            { percent: 70, name: 'test3', img: `${dog}` },
            { percent: 70, name: 'test3', img: `${dog}` },
            { percent: 70, name: 'test3', img: `${dog}` },
        ];

        setTimeLaps(state);
    }, []);

    return (
        <div className={styles.container}>
            {timeLaps.map((element, index) => (
                <div key={index} className={styles.timeLaps}>
                    <div className={styles.circularContainer}>
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
        </div>
    );
}

export default TimeLapse;
