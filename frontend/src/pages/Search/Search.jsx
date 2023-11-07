import styles from './Search.module.css';
import SearchBar from '../../components/SearchBar/SearchBar';

function Search() {
    return (
        <div className={styles.container}>
            <SearchBar />
        </div>
    );
}

export default Search;
