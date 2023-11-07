import styles from './DetailComment.module.css';
import Feed from '../../components/Feed/Feed';
import { useLocation } from 'react-router';
import { useEffect } from 'react';

function DetailComment() {
    const location = useLocation();
    const state = location.state;

    useEffect(() => {
        console.log(state);
    }, []);

    return (
        <div className={styles.container}>
            <Feed state={state} />
        </div>
    );
}

export default DetailComment;
