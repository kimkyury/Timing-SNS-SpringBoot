import { useEffect, useState } from 'react';
import styles from './MainFeed.module.css';
import { useNavigate, useLocation } from 'react-router-dom';
import FavoriteBorderOutlinedIcon from '@mui/icons-material/FavoriteBorderOutlined';
import FavoriteOutlinedIcon from '@mui/icons-material/FavoriteOutlined';
import SmsOutlinedIcon from '@mui/icons-material/SmsOutlined';
import ShareOutlinedIcon from '@mui/icons-material/ShareOutlined';
import dog from '../../assets/dog.jpg';
import { style, width } from '@mui/system';

function MainFeed() {
    const navigate = useNavigate();
    const location = useLocation();

    const [user, setUser] = useState(null);
    const [state, setState] = useState([]);

    useEffect(() => {
        const user = { id: '김정희', profile_img: `${dog}` };
        setUser(user);
        const state = {
            pk: 1,
            profileimage: `${dog}`,
            name: '하성호',
            id: '@헬린이',
            image: `${dog}`,
            isLiked: false,
            likes: 1234567,
            comment: 765432,
            share: 1000,
            hash: ['#개', '#댕댕이', '#시바', '#산책'],
            content: '멍멍',
            comments: [
                { name: '시바', comment: '뭐', profileimage: `${dog}` },
                { name: '씨바', comment: 'ㅋㅋㅋ', profileimage: `${dog}` },
                { name: '치바', comment: 'ㅎㅎㅎㅎ', profileimage: `${dog}` },
                { name: '치바', comment: 'ㅎㅎㅎㅎ', profileimage: `${dog}` },
                { name: '치바', comment: 'ㅎㅎㅎㅎ', profileimage: `${dog}` },
                { name: '치바', comment: 'ㅎㅎㅎㅎ', profileimage: `${dog}` },
                { name: '치바', comment: 'ㅎㅎㅎㅎ', profileimage: `${dog}` },
                { name: '치바', comment: 'ㅎㅎㅎㅎ', profileimage: `${dog}` },
                { name: '치바', comment: 'ㅎㅎㅎㅎ', profileimage: `${dog}` },
                { name: '치바', comment: 'ㅎㅎㅎㅎ', profileimage: `${dog}` },
                { name: '치바', comment: 'ㅎㅎㅎㅎ', profileimage: `${dog}` },
            ],
        };
        setState(state);
    }, []);

    function gotoDetailFeed() {
        navigate(`/detailfeed/${state.pk}`, { state });
    }

    function formatK(count) {
        if (count >= 100000) {
            return (count / 1000000).toFixed(1) + 'm';
        } else if (count >= 1000) {
            return (count / 1000).toFixed(1) + 'k';
        } else {
            return count;
        }
    }

    return (
        <div className={styles.container}>
            <div className={styles.nameContainer}>
                <img src={state.profileimage} className={styles.profileimage} />
                <div className={styles.namebox}>
                    <div className={styles.name}>{state.name}</div>
                    <div className={styles.id}>{state.id}</div>
                </div>
            </div>

            <img src={state.image} className={styles.imageContainer} onClick={gotoDetailFeed} />

            <div className={styles.tagContainer}>
                <div className={styles.tagitem}>
                    <div>
                        {state.isLiked ? (
                            <FavoriteOutlinedIcon style={{ width: '4vw', height: '4vw' }} />
                        ) : (
                            <FavoriteBorderOutlinedIcon style={{ width: '4vw', height: '4vw' }} />
                        )}
                    </div>
                    <div>{formatK(state.likes)}</div>
                </div>
                <div className={styles.tagitem}>
                    <div>
                        <SmsOutlinedIcon style={{ width: '4vw', height: '4vw' }} />
                    </div>
                    <div>{formatK(state.comment)}</div>
                </div>
                <div className={styles.tagitem}>
                    <div>
                        <ShareOutlinedIcon style={{ width: '4vw', height: '4vw' }} />
                    </div>
                    <div>{formatK(state.share)}</div>
                </div>
            </div>

            <div className={styles.commentContainer}>
                <div className={styles.commentname}>{state.name}</div>
                <div className={styles.commentcontent}>{state.content}</div>
            </div>
            <div className={styles.hashTagContainer}>
                {state.length != 0 &&
                    state.hash.map((v, i) => (
                        <div key={i} className={styles.hash}>
                            <div>{v}</div>
                        </div>
                    ))}
            </div>
        </div>
    );
}

export default MainFeed;
