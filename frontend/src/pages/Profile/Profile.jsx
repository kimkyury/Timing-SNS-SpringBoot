import TimeLapse from '../../components/TimeLapse/TimeLapse';
import styles from './Profile.module.css';
import UserProfile from '../../components/UserProfile/UserProfile';
import MyFeed from '../../components/Feed/FeedList';
import { useEffect, useState } from 'react';
import axios from '../../server';
import { useSearchParams, useLocation } from 'react-router-dom';
import { motion } from 'framer-motion';

function Profile() {
    const location = useLocation();
    const currentUrl = location.pathname;
    const [searchParams] = useSearchParams();
    const email = searchParams.get('email');
    const [accessToken] = useState(sessionStorage.getItem('accessToken'));
    const [state, setState] = useState([]);

    useEffect(() => {
        if (currentUrl == '/profile') {
            getFeed();
        } else {
            getWriterFeed();
        }
    }, []);

    const getFeed = () => {
        axios
            .get(`/api/v1/feeds`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            })
            .then((response) => {
                console.log(response.data);
                setState(response.data);
            })
            .catch((error) => {
                console.error(error);
            });
    };
    const getWriterFeed = () => {
        axios
            .get(`/api/v1/feeds?email=${email}`, {
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
    return state.length != 0 ? (
        <motion.div
            initial={{ opacity: 0, x: 100 }}
            animate={{ opacity: 1, x: 0 }}
            exit={{ opacity: 0, x: -100 }}
            transition={{ duration: 0.4 }}
            className={styles.container}
        >
            <div className={styles.user_info}>
                <UserProfile data={state} />
            </div>

            {currentUrl == '/profile' && (
                <>
                    <div className={styles.timeContainerName}>진행중인 타입랩스</div>
                    <TimeLapse />
                </>
            )}
            <div className={styles.timeContainerName}>공개 타입랩스</div>
            {state.feeds.length != 0 ? (
                <div className={styles.my_timelapse}>
                    <MyFeed state={state.feeds.filter((content) => content.isPrivate == false)} />
                </div>
            ) : (
                <div className={styles.emptybox}>피드가 없어요</div>
            )}
            <div className={styles.timeContainerName}>비공개 타입랩스</div>
            {state.feeds.length != 0 ? (
                <div className={styles.my_timelapse}>
                    <MyFeed state={state.feeds.filter((content) => content.isPrivate == true)} />
                </div>
            ) : (
                <div className={styles.emptybox}>피드가 없어요</div>
            )}
        </motion.div>
    ) : (
        <></>
    );
}

export default Profile;
