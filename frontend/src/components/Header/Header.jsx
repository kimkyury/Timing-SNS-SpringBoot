import styles from './Header.module.css';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import { useNavigate, useLocation } from 'react-router-dom';
function Header() {
    const navigate = useNavigate();
    const location = useLocation();
    const currentUrl = location.pathname;
    function gotoback() {
        navigate(-1);
    }
    return (
        <div className={styles.header}>
            <div className={styles.backbox}>
                {currentUrl == '/' ? <div></div> : <ArrowBackIcon className={styles.back} onClick={gotoback} />}
            </div>
            <div className={styles.title}>Timing</div>
            <div></div>
        </div>
    );
}

export default Header;
