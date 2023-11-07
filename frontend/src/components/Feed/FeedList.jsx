import styles from './FeedList.module.css';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';

function FeedList(data) {
    const state = data;
    return (
        <div className={styles.myfeedcontainerName}>
            {/* 나의 피드 */}
            <div className={styles.image_container}>
                {Array.isArray(state.state) &&
                    state.state.map((v, i) => (
                        <div key={i} className={styles.imagebox}>
                            <img src={v.image} style={{ filter: v.isPublic ? 'blur(5px)' : '' }} />
                            {v.isPublic && <LockOutlinedIcon className={styles.lock} />}
                        </div>
                    ))}
            </div>
        </div>
    );
}

export default FeedList;