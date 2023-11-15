import styles from './MainFeed.module.css';
import Feed from '../../components/Feed/Feed';
import { useEffect } from 'react';
import axios from '../../server';
import _ from 'lodash';
import { useSelector, useDispatch } from 'react-redux';
import { setFeed } from '../../store/slices/feedSlice';
import PullToRefresh from '../../components/PullToRefresh';

function MainFeed() {
    const feedState = useSelector((state) => state.feed);
    const dispatch = useDispatch();

    const getRecFeed = () => {
        const accessToken = sessionStorage.getItem('accessToken');
        axios
            .get(`/api/v1/feeds/recommended`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            })
            .then((response) => {
                dispatch(setFeed(response.data));
            })
            .catch((error) => {
                console.error(error);
            });
    };

    useEffect(() => {
        if (feedState.feeds.length == 0) {
            getRecFeed();
        }
    }, []);

    useEffect(() => {
        // 스크롤 이벤트 리스너 등록
        window.addEventListener('scroll', handleScroll);

        // 컴포넌트가 언마운트될 때 이벤트 리스너 제거
        return () => {
            window.removeEventListener('scroll', handleScroll);
        };
    }, []);

    // 스크롤 이벤트 핸들러
    const handleScroll = _.debounce(() => {
        // 현재 스크롤 위치
        const scrollHeight = window.scrollY;

        // 창의 높이
        const windowHeight = window.innerHeight;
        // 화면 바닥에 도달했을 때 추가 데이터 로드
        if (scrollHeight + windowHeight > document.body.offsetHeight - 1) {
            getRecFeed();
        }
    }, 100);
    const handleRefresh = () => {
        if (window.scrollY <= 0) {
            window.location.reload();
        }
    };

    return (
        <PullToRefresh onRefresh={handleRefresh}>
            <div className={styles.container}>
                {feedState.feeds.length != 0 &&
                    feedState.feeds.map((value, index) => <Feed key={index} data={value} />)}
            </div>
        </PullToRefresh>
    );
}

export default MainFeed;