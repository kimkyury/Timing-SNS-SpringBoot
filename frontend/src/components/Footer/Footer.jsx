import styles from './Footer.module.css';
import HomeOutlinedIcon from '@mui/icons-material/HomeOutlined';
import HomeRoundedIcon from '@mui/icons-material/HomeRounded';
import PersonRoundedIcon from '@mui/icons-material/PersonRounded';
import PersonOutlineOutlinedIcon from '@mui/icons-material/PersonOutlineOutlined';
import PageviewOutlinedIcon from '@mui/icons-material/PageviewOutlined';
import PageviewRoundedIcon from '@mui/icons-material/PageviewRounded';

import { useNavigate, useLocation } from 'react-router-dom';

function Footer() {
    const navigate = useNavigate();
    const location = useLocation();

    //   const currentUrl = useSelector((state: RootState) => state.router.currentUrl);
    const currentUrl = location.pathname;

    return (
        <div className={styles.footer}>
            {currentUrl === '/' ? (
                <HomeRoundedIcon />
            ) : (
                <HomeOutlinedIcon
                    onClick={() => {
                        navigate('/');
                    }}
                />
            )}
            {/* {currentUrl === '/like' ? (
                <FavoriteRoundedIcon />
            ) : (
                <FavoriteBorderRoundedIcon
                    onClick={() => {
                        navigate('/like');
                    }}
                />
            )} */}
            {currentUrl === '/search' ? (
                <PageviewRoundedIcon />
            ) : (
                <PageviewOutlinedIcon
                    onClick={() => {
                        navigate('/search');
                    }}
                />
            )}
            {currentUrl === '/profile' ? (
                <PersonRoundedIcon />
            ) : (
                <PersonOutlineOutlinedIcon
                    onClick={() => {
                        navigate('/profile');
                    }}
                />
            )}
        </div>
    );
}

export default Footer;
