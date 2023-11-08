import TimeLapse from '../../components/TimeLapse/TimeLapse';
import styles from './Profile.module.css';
import UserProfile from '../../components/UserProfile/USerProfile';
import MyFeed from '../../components/Feed/FeedList';
import dog from '../../assets/dog.jpg';
import React, { useEffect, useState } from 'react';
import axios from 'axios';
function Profile() {
    const BASE_URL = `http://k9e203.p.ssafy.io`;
    const [accessToken, setAccessToken] = useState(sessionStorage.getItem('accessToken'));
    const [state, setState] = useState([]);
    useEffect(() => {
        getChallenge();
    }, []);
    const getChallenge = () => {
        axios
            .get(`${BASE_URL}/api/v1/feeds`, {
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

    console.log(state.length != 0 ? '1' : '2');
    return (
        <div>
            {state.length != 0 ? (
                <div className={styles.container}>
                    <div className={styles.user_info}>
                        <UserProfile data={state} />
                    </div>
                    <div className={styles.proccessing_timelapse}>
                        <div className={styles.timeContainerName}>진행중인 타입랩스</div>
                        <TimeLapse />
                    </div>
                    <div className={styles.timeContainerName}>공개 타입랩스</div>
                    {state.feeds.length != 0 ? (
                        <div className={styles.my_timelapse}>
                            <MyFeed state={state.feeds.filter((content) => content.isPublic == false)} />
                        </div>
                    ) : (
                        <div className={styles.emptybox}>피드가 없어요</div>
                    )}
                    <div className={styles.timeContainerName}>비공개 타입랩스</div>
                    {state.feeds.length != 0 ? (
                        <div className={styles.my_timelapse}>
                            <MyFeed state={state.feeds.filter((content) => content.isPublic == true)} />
                        </div>
                    ) : (
                        <div className={styles.emptybox}>피드가 없어요</div>
                    )}
                </div>
            ) : (
                <></>
            )}
        </div>
    );
}

export default Profile;
