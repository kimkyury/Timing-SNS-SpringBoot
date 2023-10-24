import styles from './TimeLapse.module.css';
import { useNavigate } from 'react-router-dom';
import { CircularProgressbar, buildStyles } from 'react-circular-progressbar';
import 'react-circular-progressbar/dist/styles.css';
import AddOutlinedIcon from '@mui/icons-material/AddOutlined';
function TimeLapse() {
    const state = {
        categoryList: [
            { percent: 10, name: 'test1' },
            { percent: 50, name: 'test2' },
            { percent: 70, name: 'test3' },
        ],
    };
    const navigate = useNavigate();
    function CreateFeed() {
        navigate(`/create`);
    }
    return (
        <div className={styles.container}>
            <div className={styles.timeContainerName}>진행중인 타입랩스</div>
            <div className={styles.timeContainer}>
                {Array.isArray(state.categoryList) &&
                    state.categoryList.map((v, i) => (
                        <div key={i} className={styles.TimeLapse}>
                            <CircularProgressbar
                                value={v.percent}
                                text={`${v.percent}%`}
                                strokeWidth="10"
                                styles={buildStyles({ pathColor: 'red', textColor: 'black' })}
                            />
                            <div>{v.name}</div>
                        </div>
                    ))}
                <div className={styles.TimeLapse} onClick={CreateFeed}>
                    <CircularProgressbar
                        value="40"
                        text="+"
                        strokeWidth="10"
                        styles={buildStyles({ pathColor: 'red', textColor: 'black', textSize: '50px' })}
                    />
                    <div>
                        <AddOutlinedIcon />
                    </div>
                </div>
            </div>
        </div>
    );
}

export default TimeLapse;
