import styles from "./Home.module.css";
import TimeLapse from "../../components/TimeLapse/TimeLapse";
import MainFeed from "../MainFeed/MainFeed";
import { Cookies } from "react-cookie";
function Home() {
  const cookies = document.cookie;
  console.log(cookies);
  return (
    <div className={styles.container}>
      <TimeLapse />
      <MainFeed />
    </div>
  );
}

export default Home;
