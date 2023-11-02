import { useEffect, useState } from 'react';
import styles from './Feed.module.css';
import { useNavigate, useLocation } from 'react-router-dom';
import FavoriteBorderOutlinedIcon from '@mui/icons-material/FavoriteBorderOutlined';
import FavoriteOutlinedIcon from '@mui/icons-material/FavoriteOutlined';
import SmsOutlinedIcon from '@mui/icons-material/SmsOutlined';
import ShareOutlinedIcon from '@mui/icons-material/ShareOutlined';
import dog from '../../assets/dog.jpg';

function Feed() {
    const navigate = useNavigate();
    const location = useLocation();
    const currentUrl = location.pathname;

    const [user, setUser] = useState(null);
    const [state, setState] = useState([]);
    const [newComment, setNewComment] = useState('');

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
            time: new Date() - 400,
            hash: ['#개', '#댕댕이', '#시바', '#산책'],
            content: '멍멍',
            comments: [
                { name: '김정희', comment: '뭐', profileimage: `${dog}`, time: new Date() - 100 },
                { name: '하성호', comment: 'ㅋㅋㅋ', profileimage: `${dog}`, time: new Date() - 10 },
                { name: '하성호', comment: 'ㅋㅋㅋ', profileimage: `${dog}`, time: new Date() - 10 },
                { name: '하성호', comment: 'ㅋㅋㅋ', profileimage: `${dog}`, time: new Date() - 10 },
                { name: '하성호', comment: 'ㅋㅋㅋ', profileimage: `${dog}`, time: new Date() - 10 },
                { name: '하성호', comment: 'ㅋㅋㅋ', profileimage: `${dog}`, time: new Date() - 10 },
                { name: '하성호', comment: 'ㅋㅋㅋ', profileimage: `${dog}`, time: new Date() - 10 },
                { name: '하성호', comment: 'ㅋㅋㅋ', profileimage: `${dog}`, time: new Date() - 10 },
                { name: '하성호', comment: 'ㅋㅋㅋ', profileimage: `${dog}`, time: new Date() - 10 },
                { name: '하성호', comment: 'ㅋㅋㅋ', profileimage: `${dog}`, time: new Date() - 10 },
                { name: '하성호', comment: 'ㅋㅋㅋ', profileimage: `${dog}`, time: new Date() - 10 },
                { name: '하성호', comment: 'ㅋㅋㅋ', profileimage: `${dog}`, time: new Date() - 10 },
                { name: '하성호', comment: 'ㅋㅋㅋ', profileimage: `${dog}`, time: new Date() - 10 },
                { name: '하성호', comment: 'ㅋㅋㅋ', profileimage: `${dog}`, time: new Date() - 10 },
                { name: '하성호', comment: 'ㅋㅋㅋ', profileimage: `${dog}`, time: new Date() - 10 },
                { name: '하성호', comment: 'ㅋㅋㅋ', profileimage: `${dog}`, time: new Date() - 10 },
            ],
        };
        setState(state);
    }, []);

    const gotoDetailComment = () => {
        if (currentUrl == '/') {
            navigate(`/detailcomment/${state.pk}`, { state });
        }
    };
    const gotoDetailFeed = () => {
        navigate(`/detailfeed/${state.pk}`, { state });
    };

    const formatK = (count) => {
        if (count >= 100000) {
            return (count / 1000000).toFixed(1) + 'm';
        } else if (count >= 1000) {
            return (count / 1000).toFixed(1) + 'k';
        } else {
            return count;
        }
    };

    const handleAddComment = () => {
        if (newComment.trim() !== '') {
            const updatedComments = [...state.comments];
            updatedComments.push({
                profileimage: user.profile_img,
                name: user.id,
                comment: newComment,
            });
            state.comment = updatedComments;
            setState(state);
            setNewComment(''); // 댓글 추가 후 새 댓글 상태 초기화
        }
    };

    const activeEnter = (e) => {
        if (e.key === 'Enter') {
            handleAddComment();
        }
    };

    // const handleDeleteComment = (commentIndex) => {
    //     const commentToDelete = comments[commentIndex];
    //     if (commentToDelete.name === user.id) {
    //         const updatedComments = [...comments];
    //         updatedComments.splice(commentIndex, 1);
    //         setComments(updatedComments);
    //     }
    // };

    return (
        <div className={styles.container}>
            {/* 게시글 주인 정보 */}
            <div className={styles.nameContainer}>
                <img src={state.profileimage} className={styles.profileimage} />
                <div className={styles.namebox}>
                    <div className={styles.name}>{state.name}</div>
                    <div className={styles.id}>{state.id}</div>
                </div>
            </div>

            {/* 게시글 이미지 */}
            <img src={state.image} className={styles.imageContainer} onClick={gotoDetailFeed} />

            {/* 게시글 좋아요, 댓글, 이어가기 정보 */}
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

            {/* 게시글 본문 */}
            <div className={styles.contentContainer}>
                <div className={styles.name}>{state.name}</div>
                <div className={styles.content}>{state.content}</div>
            </div>

            {/* 게시글 해시태그 */}
            <div className={styles.hashTagContainer}>
                {state.length != 0 &&
                    state.hash.map((v, i) => (
                        <div key={i} className={styles.hash}>
                            <div>{v}</div>
                        </div>
                    ))}
            </div>

            {/* 게시글 시간 정보 */}
            <div>{new Date() - state.time} 초 전</div>

            {/* 게시글 댓글 */}
            {currentUrl != '/' && state.length != 0 && (
                <div className={styles.floatBottom}>
                    {state.comments.map((v, i) => (
                        <div key={i} className={styles.commentContainer}>
                            <img src={v.profileimage} className={styles.commentImage} />
                            <div className={styles.commentbox}>
                                <div className={styles.commentInfo}>
                                    <div className={styles.name}>{v.name}</div>
                                    <div className={styles.time}>{new Date() - v.time}초</div>
                                </div>
                                <div className={styles.comment}>{v.comment}</div>
                            </div>
                            {/* {user.id == v.name ? <CloseOutlinedIcon onClick={() => handleDeleteComment(i)} /> : ''} */}
                        </div>
                    ))}
                </div>
            )}

            {user && (
                <div className={currentUrl == '/' ? styles.commentContainer : styles.commentContainerFix}>
                    <img src={user.profile_img} className={styles.commentImage} />
                    <input
                        type="text"
                        className={styles.commentInput}
                        placeholder="댓글 달기..."
                        value={newComment}
                        onChange={(e) => setNewComment(e.target.value)}
                        onClick={gotoDetailComment}
                        onKeyDown={(e) => activeEnter(e)}
                    />
                    {newComment.length != 0 && (
                        <button onClick={handleAddComment} className={styles.commentBtn}>
                            추가
                        </button>
                    )}
                </div>
            )}
        </div>
    );
}

export default Feed;
