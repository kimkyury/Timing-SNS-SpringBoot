import styles from './DetailComment.module.css';
import Feed from '../../components/Feed/Feed';
import { useLocation } from 'react-router';
import axios from 'axios';
import { useEffect, useState } from 'react';
import { motion } from 'framer-motion';
function DetailComment() {
    const location = useLocation();
    const ID = location.state.id;
    const [state, setState] = useState();
    const [user, setUser] = useState(null);
    const BASE_URL = `https://timingkuku.shop`;
    const [accessToken, setAccessToken] = useState(sessionStorage.getItem('accessToken'));
    const getDetailFeed = () => {
        axios
            .get(`${BASE_URL}/api/v1/feeds/${ID}`, {
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
    useEffect(() => {
        getDetailFeed();
        axios
            .get(`${BASE_URL}/api/v1/members`, {
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
    }, [state]);

    return state ? (
        <motion.div
            initial={{ opacity: 0, x: 100 }}
            animate={{ opacity: 1, x: 0 }}
            exit={{ opacity: 0, x: -100 }}
            transition={{ duration: 0.4 }}
            className={styles.container}
        >
            <Feed data={state} />
        </motion.div>
    ) : (
        <></>
    );
}

export default DetailComment;
