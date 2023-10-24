import TimeLapse from '../../components/TimeLapse/TimeLapse';
import styles from './Profile.module.css';
import UserProfile from '../../components/UserProfile/USerProfile';
import MyFeed from '../../components/Feed/MyFeed';

function Profile() {
    return (
        <div className={styles.container}>
            <div className={styles.user_info}>
                <UserProfile />
            </div>
            <div className={styles.proccessing_timelapse}>
                <TimeLapse />
            </div>
            <div className={styles.my_timelapse}>
                <MyFeed />
            </div>
        </div>
    );
}

export default Profile;
