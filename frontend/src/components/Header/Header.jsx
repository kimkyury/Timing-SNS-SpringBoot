import styles from './Header.module.css';
import { useNavigate, useLocation } from 'react-router-dom';
import Logo from '../../assets/Logo.png';
function Header() {
    const navigate = useNavigate();
    const location = useLocation();
    const currentUrl = location.pathname;
    return (
        <div className={styles.header}>
            <div className={styles.title}>
                <img src={Logo} className={styles.Logo} />
            </div>
        </div>
    );
}

export default Header;
