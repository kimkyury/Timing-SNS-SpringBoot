import styles from "./MainFeed.module.css";
import Feed from "../../components/Feed/Feed";

function MainFeed() {
  return (
    <div className={styles.container}>
      <Feed />
      <Feed />
      <Feed />
      <Feed />
    </div>
  );
}

export default MainFeed;
