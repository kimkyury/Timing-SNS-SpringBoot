import styles from './TimeLapse.module.css';
import { useNavigate } from 'react-router-dom';
import { CircularProgressbar, buildStyles, CircularProgressbarWithChildren } from 'react-circular-progressbar';
import 'react-circular-progressbar/dist/styles.css';
import dog from '../../assets/dog2.jpg';
function TimeLapse() {
    const state = {
        categoryList: [
            { percent: 10, name: 'test1', img: `${dog}` },
            { percent: 50, name: 'test2', img: `${dog}` },
            { percent: 70, name: 'test3', img: `${dog}` },
        ],
    };
    const navigate = useNavigate();
    function CreateFeed() {
        navigate(`/create`);
    }
    return (
        <div className={styles.container}>
            <div className={styles.timeContainer}>
                {Array.isArray(state.categoryList) &&
                    state.categoryList.map((v, i) => (
                        <div key={i} className={styles.TimeLapse}>
                            <CircularProgressbarWithChildren
                                value={v.percent}
                                strokeWidth="10"
                                styles={buildStyles({ pathColor: 'red', textColor: 'black' })}
                            >
                                <img
                                    src={v.img}
                                    style={{
                                        width: '48px',
                                        height: '48px',
                                        borderRadius: '30px',
                                        objectFit: 'cover',
                                    }}
                                />
                            </CircularProgressbarWithChildren>
                        </div>
                    ))}
                <div className={styles.TimeLapse} onClick={CreateFeed}>
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
