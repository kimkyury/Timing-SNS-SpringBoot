import styles from './MainFeed.module.css';
import Feed from '../../components/Feed/Feed';
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import _ from 'lodash';
import { useSelector, useDispatch } from 'react-redux';
import { setFeed } from '../../store/slices/feedSlice';
import store from '../../store/store';
import PullToRefresh from '../../components/PullToRefresh';
import { Co2Sharp } from '@mui/icons-material';

function MainFeed() {
    const feedState = useSelector((state) => state.feed);
    const [page, setPage] = useState(1);
    const dispatch = useDispatch();
    const accessToken = sessionStorage.getItem('accessToken');
    const getRecFeed = () => {
        console.log(page);
        axios
            .get(`${import.meta.env.VITE_APP_API}/api/v1/feeds/recommended?page=${page}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            })
            .then((response) => {
                console.log(response.data);
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
            console.log('바닥');
            setPage((prevPage) => {
                const newPage = prevPage + 1; // 예시로 이전 페이지에서 1 증가
                return newPage; // 새로운 상태를 반환
            });
            console.log(page);
        }
    }, 100);
    useEffect(() => {
        console.log(page, 'change');
        if (page != 1) {
            getRecFeed();
        }
    }, [page]);
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
