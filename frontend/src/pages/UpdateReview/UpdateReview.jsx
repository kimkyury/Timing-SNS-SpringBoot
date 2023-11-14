import styles from './UpdateReview.module.css';
import { useLocation } from 'react-router';
import Textarea from '@mui/joy/Textarea';
import { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
function UpdateReview() {
    const { state } = useLocation();
    const BASE_URL = `https://timingkuku.shop`;
    const [accessToken] = useState(sessionStorage.getItem('accessToken'));
    const [NewReview, setNewReivew] = useState(state.review);
    const [isPrivate] = useState(state.isPrivate);
    const navigate = useNavigate();
    const updateReview = () => {
        axios
            .patch(
                `${BASE_URL}/api/v1/feeds/${state.id}`,
                { isPrivate: isPrivate, review: NewReview },
                {
                    headers: {
                        Authorization: `Bearer ${accessToken}`,
                    },
                }
            )
            .then(() => {
                navigate('/');
                window.location.reload();
            })
            .catch((error) => {
                console.error(error);
            });
    };
    return (
        <motion.div
            initial={{ opacity: 0, x: 100 }}
            animate={{ opacity: 1, x: 0 }}
            exit={{ opacity: 0, x: -100 }}
            transition={{ duration: 0.4 }}
            className={styles.container}
        >
            <Textarea
                minRows={4}
                className={styles.contentbox}
                value={NewReview}
                onChange={(e) => setNewReivew(e.target.value)}
            />
            <button onClick={updateReview} className={styles.summitbtn}>
                수정
            </button>{' '}
        </motion.div>
    );
}

export default UpdateReview;
