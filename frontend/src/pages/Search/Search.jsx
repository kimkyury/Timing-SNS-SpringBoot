import React, { useRef } from 'react';
import styles from './Search.module.css'; // Search 모듈의 스타일
import SearchBar from '../../components/SearchBar/SearchBar';

function Search() {
    return (
        <div className={styles.container}>
            <SearchBar />
        </div>
    );
}

export default Search;
