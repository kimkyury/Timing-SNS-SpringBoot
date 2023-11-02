import styles from "./SearchBar.module.css";
import { useEffect, useState } from "react";
import FeedList from "../Feed/FeedList";
import SearchIcon from "@mui/icons-material/Search";
import CloseOutlinedIcon from "@mui/icons-material/CloseOutlined";
import dog from "../../assets/dog.jpg";
import dog2 from "../../assets/dog2.jpg";

function SearchBar() {
  const [inputValue, setInputValue] = useState("");
  const [state, setState] = useState([]);
  const [wordList, setWordList] = useState([]);

  const formatK = (count) => {
    if (count >= 100000) {
      return (count / 1000000).toFixed(1) + "백만";
    } else if (count >= 1000) {
      return (count / 1000).toFixed(1) + "천";
    } else {
      return count;
    }
  };
  useEffect(() => {
    const state = [
      { image: `${dog}`, isPublic: true },
      { image: `${dog}`, isPublic: false },
      { image: `${dog}`, isPublic: false },
      { image: `${dog}`, isPublic: false },
      { image: `${dog}`, isPublic: false },
      { image: `${dog}`, isPublic: true },
      { image: `${dog}`, isPublic: false },
      { image: `${dog}`, isPublic: true },
      { image: `${dog}`, isPublic: false },
      { image: `${dog}`, isPublic: false },
    ];
    setState(state);
  }, []);

  useEffect(() => {
    if (inputValue.length != 0) {
      // 예상 단어 가져오기
      const result = [
        { word: "축구", count: 120 },
        { word: "하성호", count: 120 },
        { word: "첼시", count: 120 },
        { word: "첼시 승", count: 1203123 },
        { word: "첼시 우승", count: 120 },
      ];
      setWordList(result);
    } else {
      setWordList([]);
    }
  }, [inputValue]);

  const searchWord = () => {
    // 검색 단어에 맞는 피드 가져오기
    const state = [
      { image: `${dog2}`, isPublic: true },
      { image: `${dog2}`, isPublic: false },
      { image: `${dog2}`, isPublic: false },
      { image: `${dog2}`, isPublic: false },
      { image: `${dog2}`, isPublic: false },
      { image: `${dog2}`, isPublic: true },
      { image: `${dog2}`, isPublic: false },
      { image: `${dog2}`, isPublic: true },
      { image: `${dog2}`, isPublic: false },
      { image: `${dog2}`, isPublic: false },
      { image: `${dog2}`, isPublic: false },
      { image: `${dog2}`, isPublic: false },
    ];
    setState(state);
    setInputValue("");
  };

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
        <div className={styles.deleteButton} onClick={() => setInputValue("")}>
          <CloseOutlinedIcon />
        </div>
      </div>
      <div>
        {wordList.map((v, i) => (
          <div
            key={i}
            className={styles.nameContainer}
            onClick={() => searchWord(v.word)}
          >
            <div className={styles.profileimage}>#</div>
            <div className={styles.namebox}>
              <div className={styles.name}>#{v.word}</div>
              <div className={styles.id}>게시글 {formatK(v.count)}</div>
            </div>
          </div>
        ))}
      </div>

      {inputValue.length == 0 && (
        <div>
          <FeedList
            state={state.filter((content) => content.isPublic == false)}
          />
        </div>
      )}
    </>
  );
}

export default SearchBar;
