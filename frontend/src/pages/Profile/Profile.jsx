import TimeLapse from "../../components/TimeLapse/TimeLapse"
import styles from "./Profile.module.css"
import UserProfile from "../../components/UserProfile/USerProfile"

function Profile() {

    return (
        <div className={styles.container}>
          <div className={styles.user_info}><UserProfile/></div>
          <div className={styles.proccessing_timelapse}>
            <TimeLapse/>
          </div>
          <div className={styles.my_timelapse}>my_timelapse</div>
        </div>
    )
  }
  
  export default Profile