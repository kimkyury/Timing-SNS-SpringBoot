import styles from './DetailFeed.module.css';
import { useEffect, useState } from 'react';
import FileDownloadOutlinedIcon from '@mui/icons-material/FileDownloadOutlined';
import DeleteOutlinedIcon from '@mui/icons-material/DeleteOutlined';
import LoopOutlinedIcon from '@mui/icons-material/LoopOutlined';
import LockOpenIcon from '@mui/icons-material/LockOpen';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import { useNavigate } from 'react-router-dom';
import axios from '../../server';
import { useLocation } from 'react-router-dom';
import { motion } from 'framer-motion';
function DetailFeed() {
    const navigate = useNavigate();
    const [accessToken] = useState(sessionStorage.getItem('accessToken'));
    const location = useLocation();
    const data = location.state;
    const [user, setUser] = useState(null);
    const [state, setState] = useState(data);
    const [newState, setNewstate] = useState();

    const updateReview = () => {
        axios
            .patch(
                `/api/v1/feeds/${newState.id}`,
                { isPrivate: !newState.isPrivate, review: newState.review },
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
    };

    const gotodetail = () => {
        setState(newState);
        navigate(`/detailcomment/${data.id}`, { state });
    };
    const download = () => {
        // 여기서 파일 다운로드 로직 실행
        axios
            .get(`/api/v1/feeds/${data.id}/videos`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
                responseType: 'blob',
            })
            .then((response) => {
                const url = window.URL.createObjectURL(response.data);
                const a = document.createElement('a');
                a.href = url;
                a.download = 'download';
                document.body.appendChild(a);
                a.click();
                setTimeout(() => {
                    window.URL.revokeObjectURL(url);
                }, 1000);
                a.remove();
            })
            .catch((error) => {
                console.error(error);
            });
    };

    const deleteFeed = () => {
        // 여기서 피드 삭제 로직 실행
        axios
            .delete(`/api/v1/feeds/${data.id}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            })
            .then(() => {
                navigate(`/`);
            })
            .catch((error) => {
                console.error(error);
            });
    };

    const relay = () => {
        axios
            .post(
                `/api/v1/challenges/${data.id}/relay`,
                {
                    startedAt: new Date().toISOString().split('T')[0],
                    goalContent: '',
                },
                {
                    headers: {
                        Authorization: `Bearer ${accessToken}`,
                    },
                }
            )
            .then(() => {
                navigate(`/`);
            })
            .catch((error) => {
                console.error(error);
            });
    };
    const getDetailFeed = () => {
        axios
            .get(`/api/v1/feeds/${data.id}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            })
            .then((response) => {
                setNewstate(response.data);
            })
            .catch((error) => {
                console.error(error);
            });
    };

    useEffect(() => {
        getDetailFeed();
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
    }, []);

    return newState && user ? (
        <motion.div
            initial={{ opacity: 0, x: 100 }}
            animate={{ opacity: 1, x: 0 }}
            exit={{ opacity: 0, x: -100 }}
            transition={{ duration: 0.4 }}
            className={styles.container}
        >
            {newState && user && (
                <>
                    <div className={styles.lock}>
                        {user.email != newState.writer.email ? (
                            <></>
                        ) : newState.isPrivate ? (
                            <LockOutlinedIcon onClick={updateReview} className={styles.icon} />
                        ) : (
                            <LockOpenIcon onClick={updateReview} className={styles.icon} />
                        )}
                    </div>

                    {/* <img src={newState.thumbnailUrl} className={styles.imageContainer} /> */}
                    <video muted autoPlay loop className={styles.imageContainer}>
                        <source
                            src={`${import.meta.env.VITE_APP_API}/api/v1/feeds/${
                                newState.id
                            }/videos/streaming?access-token=${accessToken}`}
                            type="video/mp4"
                        />
                    </video>
                    <div className={styles.etcbox}>
                        <div className={styles.etcinner} onClick={gotodetail}>
                            <LoopOutlinedIcon className={styles.etcimg} />
                            댓글보기
                        </div>
                        {user.email != newState.writer.email && (
                            <div className={styles.etcinner} onClick={relay}>
                                <LoopOutlinedIcon className={styles.etcimg} />
                                이어받기
                            </div>
                        )}
                        {user.email == newState.writer.email && (
                            <>
                                <div className={styles.etcinner} onClick={download}>
                                    <FileDownloadOutlinedIcon className={styles.etcimg} />
                                    다운로드
                                </div>
                                <div className={styles.etcinner} onClick={deleteFeed}>
                                    <DeleteOutlinedIcon className={styles.etcimg} />
                                    삭제
                                </div>
                            </>
                        )}
                    </div>
                </>
            )}
        </motion.div>
    ) : (
        <></>
    );
}
export default DetailFeed;
