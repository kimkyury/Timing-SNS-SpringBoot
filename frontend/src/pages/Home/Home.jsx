import styles from './Home.module.css';
import TimeLapse from '../../components/TimeLapse/TimeLapse';
import MainFeed from '../MainFeed/MainFeed';

function Home() {
    return (
        <div className={styles.container}>
            <TimeLapse />
            <MainFeed />
        </div>
    );
}

export default Home;
