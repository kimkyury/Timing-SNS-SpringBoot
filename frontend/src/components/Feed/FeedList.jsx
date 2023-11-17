import { useNavigate } from 'react-router-dom';
import styles from './FeedList.module.css';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import axios from '../../server';
import { useState } from 'react';

function FeedList(data) {
    const [state, setState] = useState(data.state);
    const navigate = useNavigate();
    const accessToken = sessionStorage.getItem('accessToken');
    const goToFeed = (i) => {
        getDetailFeed(i);
    };
    const getDetailFeed = (i) => {
        axios
            .get(`/api/v1/feeds/${i}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            })
            .then((response) => {
                setState(response.data);
                navigate(`/detailfeed/${i}`, {
                    state: state.find((item) => item.id === i),
                });
            })
            .catch((error) => {
                console.error(error);
            });
    };
    return state ? (
        <div className={styles.myfeedcontainerName}>
            {/* 나의 피드 */}
            <div className={styles.image_container}>
                {Array.isArray(state) &&
                    state.map((v, i) => (
                        <div key={i} className={styles.imagebox} onClick={() => goToFeed(v.id)}>
                            <img src={v.thumbnailUrl} style={{ filter: v.isPublic ? 'blur(5px)' : '' }} />
                            {v.isPublic && <LockOutlinedIcon className={styles.lock} />}
                        </div>
                    ))}
            </div>
        </div>
    ) : (
        <></>
    );
}

export default FeedList;
