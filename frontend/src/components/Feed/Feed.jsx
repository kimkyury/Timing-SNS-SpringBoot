import { useEffect, useState } from 'react';
import styles from './Feed.module.css';
import { useNavigate, useLocation } from 'react-router-dom';
import FavoriteBorderOutlinedIcon from '@mui/icons-material/FavoriteBorderOutlined';
import FavoriteOutlinedIcon from '@mui/icons-material/FavoriteOutlined';
import SmsOutlinedIcon from '@mui/icons-material/SmsOutlined';
import ShareOutlinedIcon from '@mui/icons-material/ShareOutlined';
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import axios from '../../server';
import _ from 'lodash';

function Feed(data) {
    const [page, setPage] = useState(1);
    const navigate = useNavigate();
    const location = useLocation();
    const currentUrl = location.pathname;
    const [state, setState] = useState(data.data);
    const [user, setUser] = useState(null);
    const [comment, setComments] = useState([]);
    const [newComment, setNewComment] = useState('');
    const accessToken = sessionStorage.getItem('accessToken');

    const getDetailFeed = () => {
        axios
            .get(`/api/v1/feeds/${data.data.id}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            })
            .then((response) => {
                setState(response.data);
            })
            .catch((error) => {
                console.error(error);
            });
    };
    const Like = () => {
        if (user.email != state.writer.email) {
            axios
                .post(
                    `/api/v1/feeds/${state.id}/likes`,
                    {},
                    {
                        headers: {
                            Authorization: `Bearer ${accessToken}`,
                        },
                    }
                )
                .then(() => {
                    getDetailFeed();
                })
                .catch((error) => {
                    console.error(error);
                });
        }
    };
    const Dislike = () => {
        if (state && state.id) {
            // state 및 state.id가 존재하는지 확인
            axios
                .delete(`/api/v1/feeds/${state.id}/likes`, {
                    headers: {
                        Authorization: `Bearer ${accessToken}`,
                    },
                })
                .then(() => {
                    getDetailFeed();
                })
                .catch((error) => {
                    console.error(error);
                });
        } else {
            console.log('state.id가 유효하지 않습니다.');
        }
    };

    useEffect(() => {
        axios
            .get(`/api/v1/members`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            })
            .then((response) => {
                setUser(response.data);
            })
            .catch((error) => {
                console.error(error);
            });
        // if (currentUrl != "/") {
        //   getComment();
        // }
    }, [accessToken]);
    useEffect(() => {
        if (currentUrl != '/') {
            axios
                .get(`/api/v1/feeds/${state.id}/comments?page=${page}`, {
                    headers: {
                        Authorization: `Bearer ${accessToken}`,
                    },
                })
                .then((response) => {
                    setComments((prevData) => [...prevData, ...response.data]);
                })
                .catch((error) => {
                    console.error(error);
                });
        }
    }, [page]);
    const gotoDetailComment = () => {
        navigate(`/detailcomment/${state.id}`, { state });
    };
    const gotoDetailFeed = () => {
        navigate(`/detailfeed/${state.id}`, { state });
    };

    const gotoEdit = () => {
        navigate(`/updatereview/${state.id}`, { state });
    };
    const gotowriterprofile = () => {
        axios
            .get(`/api/v1/feeds?email=${state.writer.email}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            })
            .then(() => {
                navigate(`/profile/?email=${state.writer.email}`);
            })
            .catch((error) => {
                console.error(error);
            });
    };
    const formatK = (count) => {
        if (count >= 100000) {
            return (count / 1000000).toFixed(1) + '백만';
        } else if (count >= 1000) {
            return (count / 1000).toFixed(1) + '천';
        } else {
            return count;
        }
    };
    const formatT = (date) => {
        const currentTime = new Date(); // 현재 시간 (로컬 시간대)
        const postTime = new Date(date); // 입력된 시간 (로컬 시간대)

        const localTimezoneOffset = currentTime.getTimezoneOffset(); // 로컬 시간대의 오프셋
        const koreaTimezoneOffset = 9 * 60; // 한국 시간대의 오프셋

        const differenceInMinutes = (currentTime - postTime) / 60000 - koreaTimezoneOffset; // 시간 차이(분)

        const correctedPostTime = new Date(postTime.getTime() + (localTimezoneOffset - koreaTimezoneOffset) * 60000); // 시간을 한국 시간대로 보정
        if (differenceInMinutes < 1) {
            return '방금 전';
        } else if (differenceInMinutes < 60) {
            return Math.floor(differenceInMinutes) + '분 전';
        } else if (differenceInMinutes < 60 * 24) {
            return Math.floor(differenceInMinutes / 60) + '시간 전';
        } else {
            const options = {
                year: 'numeric',
                month: 'long',
                day: 'numeric',
                hour: 'numeric',
                minute: 'numeric',
                hour12: false,
                timeZone: 'Asia/Seoul',
            };
            return correctedPostTime.toLocaleString('ko-KR', options);
        }
    };
    const formatEmail = (t) => {
        const s = t.indexOf('@');
        return t.substring(0, s);
    };
    const handleAddComment = () => {
        if (newComment.trim() !== '') {
            axios
                .post(
                    `/api/v1/feeds/${state.id}/comments`,
                    { content: newComment },
                    {
                        headers: {
                            Authorization: `Bearer ${accessToken}`,
                        },
                    }
                )
                .then(() => {
                    window.location.reload();
                })
                .catch((error) => {
                    console.error(error);
                });

            setNewComment(''); // 댓글 추가 후 새 댓글 상태 초기화
        }
    };

    const activeEnter = (e) => {
        if (e.key === 'Enter') {
            handleAddComment();
        }
    };

    const goToInfluence = () => {
        navigate('/influence', { state: state.id });
    };

    // const handleDeleteComment = (commentIndex) => {
    //     const commentToDelete = comments[commentIndex];
    //     if (commentToDelete.name === user.id) {
    //         const updatedComments = [...comments];
    //         updatedComments.splice(commentIndex, 1);
    //         setComments(updatedComments);
    //     }
    // };
    useEffect(() => {
        if (currentUrl != '/') {
            // 스크롤 이벤트 리스너 등록
            window.addEventListener('scroll', handleScroll);

            // 컴포넌트가 언마운트될 때 이벤트 리스너 제거
            return () => {
                window.removeEventListener('scroll', handleScroll);
            };
        }
    }, []);
    const handleScroll = () => {
        const scrollHeight = window.scrollY;
        const windowHeight = window.innerHeight;
        if (scrollHeight + windowHeight > document.body.offsetHeight - 1) {
            setPage((prevPage) => {
                if (prevPage != Math.floor(state.commentCount / 10) + (state.commentCount % 10 > 0 ? 1 : 0)) {
                    const newPage = prevPage + 1; // 예시로 이전 페이지에서 1 증가
                    return newPage; // 새로운 상태를 반환
                } else {
                    const newPage = prevPage;
                    return newPage;
                }
            });
        }
    };
    return (
        <div>
            {state ? (
                <div className={styles.container}>
                    {/* 게시글 주인 정보 */}
                    <div className={styles.nameContainer} onClick={gotowriterprofile}>
                        <img src={state.writer.profileImageUrl} className={styles.profileimage} />
                        <div className={styles.namebox}>
                            <div className={styles.name}>{state.writer.nickname}</div>
                            <div className={styles.id}>{formatEmail(state.writer.email)}</div>
                        </div>
                    </div>

                    {/* 게시글 이미지 */}
                    <img src={state.thumbnailUrl} className={styles.imageContainer} onClick={gotoDetailFeed} />

                    {/* 게시글 좋아요, 댓글, 이어가기 정보 */}
                    <div className={styles.tagContainer}>
                        <div className={styles.tagitem}>
                            <div className={styles.tagitemicon}>
                                {state.isLiked ? (
                                    <FavoriteOutlinedIcon
                                        style={{ width: '4vw', height: '4vw', color: 'red' }}
                                        onClick={Dislike}
                                    />
                                ) : (
                                    <FavoriteBorderOutlinedIcon
                                        style={{ width: '4vw', height: '4vw' }}
                                        onClick={Like}
                                    />
                                )}
                            </div>
                            <div>{formatK(state.likeCount)}</div>
                        </div>
                        <div className={styles.tagitem}>
                            <div className={styles.tagitemicon}>
                                <SmsOutlinedIcon style={{ width: '4vw', height: '4vw' }} />
                            </div>
                            <div>{formatK(state.commentCount)}</div>
                        </div>
                        <div className={styles.tagitem}>
                            <div className={styles.tagitemicon}>
                                <ShareOutlinedIcon style={{ width: '4vw', height: '4vw' }} onClick={goToInfluence} />
                            </div>
                            <div>{formatK(state.shareCount)}</div>
                        </div>

                        {/* 수정버튼 */}

                        {currentUrl == '/' ? (
                            <></>
                        ) : (
                            <>
                                {state.length != 0 && user != null && user.email == state.writer.email ? (
                                    <EditOutlinedIcon className={styles.editbtn} onClick={gotoEdit} />
                                ) : (
                                    <></>
                                )}
                            </>
                        )}
                    </div>

                    {/* 게시글 본문 */}
                    <div className={styles.contentContainer}>
                        <div className={styles.name}>{state.writer.nickname}</div>
                        <div className={styles.content} style={{ whiteSpace: currentUrl == '/' ? 'nowrap' : 'wrap' }}>
                            {state.review}
                        </div>
                    </div>

                    {/* 게시글 해시태그 */}
                    <div className={styles.hashTagContainer}>
                        {state.length != 0 &&
                            state.hashTags.map((v, i) => (
                                <div key={i} className={styles.hash}>
                                    <div>#{v.content}</div>
                                </div>
                            ))}
                    </div>

                    {/* 게시글 시간 정보 */}
                    <div className={styles.datebox}>{formatT(state.createdAt)}</div>

                    {/* 게시글 댓글 */}
                    {currentUrl != '/' && comment.length != 0 && (
                        <div className={styles.floatBottom}>
                            {comment.map((v, i) => (
                                <div key={i} className={styles.commentContainer}>
                                    <img src={v.writer.profileImageUrl} className={styles.commentImage} />
                                    <div className={styles.commentbox}>
                                        <div className={styles.commentInfo}>
                                            <div className={styles.name}>{v.writer.nickname}</div>
                                            <div className={styles.time}>{formatT(v.createdAt)}</div>
                                        </div>
                                        <div className={styles.comment}>{v.content}</div>
                                    </div>
                                    {/* {user.id == v.name ? <CloseOutlinedIcon onClick={() => handleDeleteComment(i)} /> : ''} */}
                                </div>
                            ))}
                        </div>
                    )}

                    {user && (
                        <div className={currentUrl == '/' ? styles.commentContainer : styles.commentContainerFix}>
                            <img src={user.profileImageUrl} className={styles.commentImage} />
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
            ) : (
                <></>
            )}
        </div>
    );
}

export default Feed;
