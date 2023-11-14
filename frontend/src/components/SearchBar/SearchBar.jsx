import styles from './SearchBar.module.css';
import { useEffect, useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import FeedList from '../Feed/FeedList';
import SearchIcon from '@mui/icons-material/Search';
import CloseOutlinedIcon from '@mui/icons-material/CloseOutlined';
import axios from 'axios';
const BASE_URL = `https://timingkuku.shop`;
function SearchBar() {
    const [inputValue, setInputValue] = useState('');
    const [state, setState] = useState([]);
    const [page, setPage] = useState(1);
    const [ID, setID] = useState(null);
    const navigate = useNavigate();
    const location = useLocation();
    const [wordList, setWordList] = useState([]);

    const accessToken = useState(sessionStorage.getItem('accessToken'));
    const currentUrl = location.pathname;
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
        if (inputValue.length != 0) {
            // 예상 단어 가져오기
            console.log('asdfasfsd');
            axios
                .post(
                    `${BASE_URL}/api/v1/hashtags/autocomplete`,
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
    useEffect(() => {
        // ID 상태가 업데이트될 때마다 실행되는 부분
        console.log(ID, page);
        if (ID !== null) {
            getSearchResult();
        }
    }, [ID, page]);
    const searchWord = (v) => {
        // 검색 단어에 맞는 피드 가져오기
        console.log(v);
        setID(v.id);
    };
    useEffect(() => {
        if (currentUrl != '/') {
            // 스크롤 이벤트 리스너 등록
            window.addEventListener('scroll', handleScroll);

            // 컴포넌트가 언마운트될 때 이벤트 리스너 제거
            return () => {
                window.removeEventListener('scroll', handleScroll);
            };
        }
    }, []);
    const getSearchResult = () => {
        axios
            .get(`${BASE_URL}/api/v1/feeds/${ID}/search?page=${page}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            })
            .then((response) => {
                setState((prevData) => [...prevData, ...response.data.feeds]);
                setInputValue('');
            })
            .catch((error) => {
                console.error(error);
            });
    };

    const handleScroll = () => {
        const scrollHeight = window.scrollY;
        const windowHeight = window.innerHeight;
        if (scrollHeight + windowHeight > document.body.offsetHeight - 1) {
            console.log('바닥');
            console.log(ID);
            setPage((prevPage) => {
                if (prevPage !== Math.floor(state.length / 12) + (state.length % 12 > 0 ? 1 : 0)) {
                    const newPage = prevPage + 1;
                    return newPage;
                } else {
                    const newPage = prevPage;
                    return newPage;
                }
            });
        }
    };
    console.log(ID);

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

            {inputValue.length == 0 && (
                <div className={styles.searchResult}>
                    <FeedList state={state.filter((content) => content.isPrivate == false)} />
                </div>
            )}
        </>
    );
}

export default SearchBar;
