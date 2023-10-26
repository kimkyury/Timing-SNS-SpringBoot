import styles from './MainFeed.module.css';
import FavoriteBorderOutlinedIcon from '@mui/icons-material/FavoriteBorderOutlined';
import FavoriteOutlinedIcon from '@mui/icons-material/FavoriteOutlined';
import SmsOutlinedIcon from '@mui/icons-material/SmsOutlined';
import ShareOutlinedIcon from '@mui/icons-material/ShareOutlined';
import { useNavigate } from 'react-router-dom';
import Etc from '../Etc/Etc';
import dog from '../../assets/dog.jpg';
function Feed() {
    const navigate = useNavigate();
    const state = {
        pk: 1,
        profileimage: `${dog}`,
        name: 'kim',
        id: '@abcd',
        image: `${dog}`,
        isLiked: true,
        likes: 1234567,
        comment: 765432,
        share: 1000,
        hash: ['#개', '#댕댕이', '#시바', '#산책'],
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
    const maxVisibleComments = 2;

    const visibleComments = state.comments.slice(0, maxVisibleComments);
    const hiddenCommentsCount = state.comments.length - maxVisibleComments;

    const renderedComments = visibleComments.map((comment) => {
        return `${comment.name}: ${comment.comment}`;
    });

    if (hiddenCommentsCount > 0) {
        renderedComments.push(`+ ${hiddenCommentsCount} 댓글 더 보기...`);
    }
    function gotoDetailFeed() {
        navigate(`/create`);
    }

    function gotoDetailComment() {
        navigate(`/detailcomment/${state.pk}`, { state });
    }
    function formatK(count) {
        if (count >= 100000) {
            return (count / 1000000).toFixed(1) + 'm';
        } else if (count >= 1000) {
            return (count / 1000).toFixed(1) + 'k';
        } else {
            return count.toString();
        }
    }

    return (
        <div className={styles.container}>
            <div className={styles.nameContainer}>
                <div>
                    <img src={state.profileimage} className={styles.profileimage} />
                </div>
                <div className={styles.namebox}>
                    <div className={styles.name}>{state.name}</div>
                    <div className={styles.id}>{state.id}</div>
                </div>
                <div className={styles.etc}>
                    <Etc />
                </div>
            </div>
            <div className={styles.imageContainer}>
                <img src={state.image} className={styles.imageContainer} />
            </div>
            <div className={styles.tagContainer}>
                <div className={styles.tagitem}>
                    <div>{state.isLiked ? <FavoriteOutlinedIcon /> : <FavoriteBorderOutlinedIcon />}</div>
                    <div>{formatK(state.likes)}</div>
                </div>
                <div className={styles.tagitem}>
                    <div>
                        <SmsOutlinedIcon />
                    </div>
                    <div>{formatK(state.comment)}</div>
                </div>
                <div className={styles.tagitem}>
                    <div>
                        <ShareOutlinedIcon />
                    </div>
                    <div>{formatK(state.share)}</div>
                </div>
            </div>
            <div className={styles.hashTagContainer}>
                {state.hash.map((v, i) => (
                    <div key={i} className={styles.hash}>
                        <div>{v}</div>
                    </div>
                ))}
            </div>
            <div className={styles.commentContainer}>
                {visibleComments.map((v, i) => (
                    <div key={i} className={styles.commentbox}>
                        <div className={styles.commentname}>{v.name} : </div>
                        <div className={styles.commentcontent}>{v.comment}</div>
                    </div>
                ))}
                {hiddenCommentsCount > 0 && (
                    <div className={styles.seemore}>
                        <div className={styles.commentcontent} onClick={gotoDetailComment}>
                            + {hiddenCommentsCount} 댓글 더 보기...
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}

export default Feed;
