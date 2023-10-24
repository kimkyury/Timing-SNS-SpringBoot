import styles from './MyFeed.module.css';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';

import dog from '../../assets/dog.jpg';
function MyFeed() {
    const state = {
        content: [
            { image: `${dog}`, isPublic: false },
            { image: `${dog}`, isPublic: true },
            { image: `${dog}`, isPublic: false },
            { image: `${dog}`, isPublic: true },
            { image: `${dog}`, isPublic: false },
            { image: `${dog}`, isPublic: true },
            { image: `${dog}`, isPublic: false },
            { image: `${dog}`, isPublic: true },
            { image: `${dog}`, isPublic: false },
            { image: `${dog}`, isPublic: true },
        ],
    };

    return (
        <div className={styles.image_container}>
            {Array.isArray(state.content) &&
                state.content.map((v, i) => (
                    <div key={i} className={styles.imagebox}>
                        <img src={v.image} style={{ filter: v.isPublic ? 'blur(5px)' : '' }} />
                        {v.isPublic && <LockOutlinedIcon className={styles.lock} />}
                    </div>
                ))}
        </div>
    );
}

export default MyFeed;
