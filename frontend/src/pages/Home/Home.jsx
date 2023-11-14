import styles from './Home.module.css';
import TimeLapse from '../../components/TimeLapse/TimeLapse';
import MainFeed from '../MainFeed/MainFeed';
import { motion } from 'framer-motion';
function Home() {
    // const cookies = document.cookie;
    // console.log(cookies);
    return (
        <motion.div
            initial={{ opacity: 0, x: 100 }}
            animate={{ opacity: 1, x: 0 }}
            exit={{ opacity: 0, x: -100 }}
            transition={{ duration: 0.4 }}
            className={styles.container}
        >
            <TimeLapse />
            <MainFeed />
        </motion.div>
    );
}

export default Home;
