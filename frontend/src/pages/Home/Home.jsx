import styles from "./Home.module.css"
import TimeLapse from "../../components/TimeLapse/TimeLapse"
import MainFeed from "../../components/Feed/MainFeed"

function Home() {

  return (
      <div className={styles.container}>
        <div className={styles.Home_timelapse}>
          <TimeLapse/>
        </div>
        <div className={styles.Home_main}>
          <MainFeed/>

        </div>
        
      </div>
  )
}

export default Home