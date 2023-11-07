import styles from './UserProfile.module.css';

import dog from '../../assets/dog.jpg';
function UserProfile() {
    const state = {
        profileimage: `${dog}`,
        name: '하성호',
        id: '@abcd',
        timelabs: 5,
        contribute: 2100,
        content: '남자는 등으로 말한다...',
    };
    return (
        <div className={styles.container}>
            <img src={state.profileimage} className={styles.imageContainer} />
            <div className={styles.mainContainer}>
                <div className={styles.upper}>
                    <div className={styles.articlebox}>
                        <div className={styles.name}>{state.name}</div>
                        <div>{state.id}</div>
                    </div>
                </div>
                <div className={styles.lower}>
                    <div className={styles.innerlower}>
                        <div className={styles.innerhead}>{state.timelabs}</div>
                        <div className={styles.innerfooter}>timelabs </div>
                    </div>
                    <div className={styles.innerlower}>
                        <div className={styles.innerhead}>{state.contribute}</div>
                        <div className={styles.innerfooter}>contribute </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default UserProfile;
