import TimeLapse from '../../components/TimeLapse/TimeLapse';
import styles from './Profile.module.css';
import UserProfile from '../../components/UserProfile/USerProfile';
import FeedList from '../../components/Feed/FeedList';
import dog from '../../assets/dog.jpg';
function Profile() {
    const state = {
        contents: [
            { image: `${dog}`, isPublic: true },
            { image: `${dog}`, isPublic: false },
            { image: `${dog}`, isPublic: false },
            { image: `${dog}`, isPublic: false },
            { image: `${dog}`, isPublic: false },
            { image: `${dog}`, isPublic: true },
            { image: `${dog}`, isPublic: false },
            { image: `${dog}`, isPublic: true },
            { image: `${dog}`, isPublic: false },
            { image: `${dog}`, isPublic: false },
            { image: `${dog}`, isPublic: true },
        ],
    };
    return (
        <div className={styles.container}>
            <div className={styles.box}>
                <UserProfile />
            </div>
            <div className={styles.proccessing_timelapse}>
                <div className={styles.timeContainerName}>진행중인 타입랩스</div>
                <TimeLapse />
            </div>
            <div className={styles.box}>
                <div className={styles.timeContainerName}>공개 타입랩스</div>
                <FeedList state={state.contents.filter((content) => content.isPublic == false)} />
            </div>
            <div className={styles.box}>
                <div className={styles.timeContainerName}>비공개 타입랩스</div>
                <FeedList state={state.contents.filter((content) => content.isPublic == true)} />
            </div>
        </div>
    );
}

export default Profile;
