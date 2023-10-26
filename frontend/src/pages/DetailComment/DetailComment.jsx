import styles from './DetailComment.module.css';
import React, { useState } from 'react';
import { useLocation } from 'react-router-dom';
import Etc from '../../components/Etc/Etc';
import dog from '../../assets/dog.jpg';
function DetailComment() {
    const location = useLocation();
    const state = location.state;
    const [comments, setComments] = useState(state.comments);
    const [newComment, setNewComment] = useState('');
    const user = { id: 'aabbccdd', profile_img: `${dog}` };

    function handleAddComment() {
        if (newComment.trim() !== '') {
            const updatedComments = [...comments];
            updatedComments.push({
                profileimage: user.profile_img,
                name: user.id,
                comment: newComment,
            });
            setComments(updatedComments);
            setNewComment(''); // 댓글 추가 후 새 댓글 상태 초기화
        }
        console.log(comments);
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
            <div className={styles.createbox}>
                <img src={user.profile_img} className={styles.imagebox} />
                <input
                    type="text"
                    className={styles.inputbox}
                    placeholder="댓글추가"
                    value={newComment}
                    onChange={(e) => setNewComment(e.target.value)}
                />
                <button onClick={handleAddComment} className={styles.btn}>
                    추가
                </button>
            </div>
            <div className={styles.commentContainer}>
                {comments.map((v, i) => (
                    <div key={i} className={styles.commentbox}>
                        <img src={v.profileimage} className={styles.imagebox} />
                        <div className={styles.innerbox}>
                            <div className={styles.commentname}>@{v.name}</div>
                            <div className={styles.commentcontent}>{v.comment}</div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default DetailComment;