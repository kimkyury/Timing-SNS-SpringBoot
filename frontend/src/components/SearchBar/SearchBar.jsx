import styles from './SearchBar.module.css';
import { useEffect, useState, useCallback } from 'react';
import { useLocation } from 'react-router-dom';
import FeedList from '../Feed/FeedList';
import SearchIcon from '@mui/icons-material/Search';
import CloseOutlinedIcon from '@mui/icons-material/CloseOutlined';
import axios from '../../server';
import { useSelector, useDispatch } from 'react-redux';
import { setSearch, setPageID, setIsStop, setTagID, resetsearchState } from '../../store/slices/searchSlice';

function SearchBar() {
    const [inputValue, setInputValue] = useState('');
    const searchState = useSelector((state) => state.search.searchs);
    const page = useSelector((state) => state.search.searchPageId);
    const isStop = useSelector((state) => state.search.isStop);
    const tagID = useSelector((state) => state.search.tagID);
    const dispatch = useDispatch();
    const [wordList, setWordList] = useState([]);
    const accessToken = sessionStorage.getItem('accessToken');
    const [feedListKey, setFeedListKey] = useState(0);
    const formatK = (count) => {
        if (count >= 100000) {
            return (count / 1000000).toFixed(1) + '백만';
        } else if (count >= 1000) {
            return (count / 1000).toFixed(1) + '천';
        } else {
            return count;
        }
    };
    useEffect(() => {
        dispatch(resetsearchState());
    }, []);

    useEffect(() => {
        if (inputValue.length !== 0) {
            axios
                .post(
                    `/api/v1/hashtags/autocomplete`,
                    { search: inputValue },
                    {
                        headers: {
                            Authorization: `Bearer ${accessToken}`,
                        },
                    }
                )
                .then((response) => {
                    setWordList(response.data.hashtags);
                })
                .catch((error) => {
                    console.error(error);
                });
        } else {
            setWordList([]);
        }
    }, [inputValue]);

    const searchWord = (v) => {
        dispatch(resetsearchState());
        dispatch(setTagID(v.id));
    };

    const getSearcgResult = useCallback(() => {
        axios
            .get(`/api/v1/feeds/${tagID}/search?page=${page}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            })
            .then((response) => {
                if (response.data.feeds.length < 12) {
                    dispatch(setIsStop(true));
                }
                dispatch(setSearch(response.data.feeds));
                setInputValue('');
                setFeedListKey((prevKey) => prevKey + 1);
            })
            .catch((error) => {
                console.error(error);
            });
    }, [tagID, page, dispatch, accessToken]);

    useEffect(() => {
        if (tagID !== 0 && searchState.length === 0) {
            getSearcgResult();
        }
    }, [tagID, searchState, getSearcgResult]);

    useEffect(() => {
        const handleScroll = () => {
            const scrollHeight = window.scrollY;
            const windowHeight = window.innerHeight;

            if (scrollHeight + windowHeight > document.body.offsetHeight - 1) {
                if (!isStop) {
                    dispatch(setPageID(page + 1));
                }
            }
        };

        window.addEventListener('scroll', handleScroll);

        return () => {
            window.removeEventListener('scroll', handleScroll);
        };
    }, [isStop, page, dispatch]);

    useEffect(() => {
        if (page !== 1 && !isStop) {
            getSearcgResult();
        }
    }, [page, isStop, getSearcgResult]);
    console.log(searchState);
    return (
        <>
            <div className={styles.searchBar}>
                <SearchIcon className={styles.searchIcon} />
                <input
                    type="text"
                    className={styles.searchInput}
                    value={inputValue}
                    onChange={(e) => setInputValue(e.target.value)}
                    placeholder="검색"
                />
                <div className={styles.deleteButton} onClick={() => setInputValue('')}>
                    <CloseOutlinedIcon />
                </div>
            </div>
            <div>
                {wordList.length > 0 ? (
                    wordList.map((v, i) => (
                        <div key={i} className={styles.nameContainer} onClick={() => searchWord(v)}>
                            <div className={styles.profileimage}>#</div>
                            <div className={styles.namebox}>
                                <div className={styles.name}>#{v.hashtag}</div>
                                <div className={styles.id}>게시글 {formatK(v.count)}</div>
                            </div>
                        </div>
                    ))
                ) : (
                    <></>
                )}
            </div>

            <div className={styles.searchResult}>
                {searchState.length != 0 && (
                    <FeedList key={feedListKey} state={searchState.filter((content) => content.isPrivate == false)} />
                )}
            </div>
        </>
    );
}

export default SearchBar;
