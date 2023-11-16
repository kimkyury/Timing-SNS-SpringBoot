import styles from './MainFeed.module.css';
import Feed from '../../components/Feed/Feed';
import React, { useEffect, useState } from 'react';
import axios from '../../server';
import _ from 'lodash';
import { useSelector, useDispatch } from 'react-redux';
import { setFeed, setPageID, setIsStop, resetFeedState } from '../../store/slices/feedSlice';
import store from '../../store/store';
import PullToRefresh from '../../components/PullToRefresh';
import { Co2Sharp } from '@mui/icons-material';

function MainFeed() {
    const feedState = useSelector((state) => state.feed);
    // const [page, setPage] = useState(1);
    const page = useSelector((state) => state.feed.pageID);
    const isStop = useSelector((state) => state.feed.isStop);
    const dispatch = useDispatch();
    const accessToken = sessionStorage.getItem('accessToken');
    const getRecFeed = () => {
        console.log(page, 'asdfasdfsad');
        axios
            .get(`/api/v1/feeds/recommended?page=${page}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            })
            .then((response) => {
                console.log(response.data.length, 'aaaaaaaaaaaaa');
                if (response.data.length < 9) {
                    dispatch(setIsStop(true));
                }
                dispatch(setFeed(response.data));
            })
            .catch((error) => {
                console.error(error);
            });
    };
    useEffect(() => {
        console.log('load');
        if (feedState.feeds.length == 0) {
            console.log('start');
            getRecFeed();
        }
    }, []);
    useEffect(() => {
        const handleScroll = () => {
            // 현재 스크롤 위치
            const scrollHeight = window.scrollY;

            // 창의 높이
            const windowHeight = window.innerHeight;

            // 화면 바닥에 도달했을 때 추가 데이터 로드
            if (scrollHeight + windowHeight > document.body.offsetHeight - 1) {
                if (!isStop) {
                    dispatch(setPageID(page + 1));
                }
            }
        };

        // 스크롤 이벤트 리스너 등록
        window.addEventListener('scroll', handleScroll);

        // 컴포넌트가 언마운트될 때 이벤트 리스너 제거
        return () => {
            window.removeEventListener('scroll', handleScroll);
        };

        // isStop이 변경될 때만 리렌더링
    }, [isStop]);

    useEffect(() => {
        console.log(page, 'change');
        console.log(isStop);
        if (page != 1) {
            if (!isStop) {
                getRecFeed();
            }
        }
    }, [page]);
    const handleRefresh = () => {
        if (window.scrollY <= 0) {
            dispatch(resetFeedState());
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
