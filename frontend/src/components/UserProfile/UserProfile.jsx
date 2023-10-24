import styles from './UserProfile.module.css';

import dog from '../../assets/dog.jpg';
function UserProfile() {
    const state = {
        profileimage: `${dog}`,
        name: '하성호',
        id: '@abcd',
        article: 5,
        content: '남자는 등으로 말한다...',
    };
    return (
        <div className={styles.container}>
            <div className={styles.imagebox}>
                <img src={state.profileimage} className={styles.imageContainer} />
                <div>{state.name}</div>
            </div>
            <div className={styles.mainContainer}>
                <div className={styles.upper}>
                    <div className={styles.articlebox}>
                        <div>{state.article}</div>
                        <div>게시글</div>
                    </div>
                    <div className={styles.btnbox}>
                        <button className={styles.btn}>프로필 편집</button>
                        <button className={styles.btn}>프로필 공유</button>
                    </div>
                </div>
                <div className={styles.lower}>{state.content}</div>
            </div>
        </div>
    );
}

export default UserProfile;
