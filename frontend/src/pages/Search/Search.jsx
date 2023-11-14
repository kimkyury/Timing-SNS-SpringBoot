import styles from './Search.module.css';
import SearchBar from '../../components/SearchBar/SearchBar';
import { motion } from 'framer-motion';
function Search() {
    return (
        <motion.div
            initial={{ opacity: 0, x: 100 }}
            animate={{ opacity: 1, x: 0 }}
            exit={{ opacity: 0, x: -100 }}
            transition={{ duration: 0.3 }}
            className={styles.container}
        >
            <SearchBar />
        </motion.div>
    );
}

export default Search;
