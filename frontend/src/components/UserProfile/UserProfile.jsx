import styles from './UserProfile.module.css';
import axios from '../../server';
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import { useEffect, useState } from 'react';
import { useSearchParams, useLocation } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import SelectMenu from './SelectMenu';

function UserProfile(data) {
    console.log(data);
    const navigate = useNavigate();
    const [accessToken] = useState(sessionStorage.getItem('accessToken'));
    const [info] = useState(data.data);
    const [state, setState] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const location = useLocation();
    const currentUrl = location.pathname;
    const [searchParams] = useSearchParams();
    const email = searchParams.get('email');
    const formatEmail = (t) => {
        const s = t.indexOf('@');
        return t.substring(0, s);
    };
    const getProfile = () => {
        axios
            .get(`/api/v1/members`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            })
            .then((response) => {
                setState(response.data);
                setIsLoading(true);
            })
            .catch((error) => {
                console.error(error);
            });
    };
    const getWriterProfile = () => {
        axios
            .get(`/api/v1/members?email=${email}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            })
            .then((response) => {
                setState(response.data);
                setIsLoading(true);
            })
            .catch((error) => {
                console.error(error);
            });
    };
    useEffect(() => {
        if (location.search == '') {
            getProfile();
        } else {
            getWriterProfile();
        }
    }, [location.search]);
    // const goToUpdateprofile = () => {
    //     navigate(`/updateprofile/${state.email}`, { state });
    // };
    return (
        <>
            {isLoading ? (
                <div className={styles.container}>
                    <div className={styles.imageContainer}>
                        <img src={state.profileImageUrl} className={styles.imageContainer} />
                    </div>
                    <div className={styles.infoContainer}>
                        <div className={styles.box}>
                            {state.nickname}
                            <SelectMenu />
                        </div>
                        <div className={styles.emailBox}>{formatEmail(state.email)}</div>
                        <div className={styles.countBox}>
                            <div>{info.feedCount}</div>
                            <div className={styles.text}>timelabs </div>
                            <div className={styles.contribute}>{info.contributeCount}</div>
                            <div className={styles.text}>contribute </div>
                        </div>
                    </div>
                </div>
            ) : (
                <></>
            )}
        </>
    );
}

export default UserProfile;
