import TimeLapse from "../../components/TimeLapse/TimeLapse";
import styles from "./Profile.module.css";
import UserProfile from "../../components/UserProfile/USerProfile";
import MyFeed from "../../components/Feed/FeedList";
import dog from "../../assets/dog.jpg";
import React, { useEffect, useState } from "react";
import axios from "axios";
function Profile() {
  const BASE_URL = `http://k9e203.p.ssafy.io`;
  const [accessToken, setAccessToken] = useState(
    sessionStorage.getItem("accessToken")
  );
  const [state, setState] = useState([]);
  const getChallenge = () => {
    axios
      .get(`${BASE_URL}/api/v1/feeds`, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      })
      .then((response) => {
        console.log(response.data);
        setState(response.data);
      })
      .catch((error) => {
        console.error(error);
      });
  };
  useEffect(() => {
    getChallenge();
  }, []);

  return (
    <div className={styles.container}>
      <div className={styles.user_info}>
        <UserProfile />
      </div>
      <div className={styles.proccessing_timelapse}>
        <div className={styles.timeContainerName}>진행중인 타입랩스</div>
        <TimeLapse />
      </div>
      {state.length != 0 ? (
        <div className={styles.my_timelapse}>
          <div className={styles.timeContainerName}>공개 타입랩스</div>
          <MyFeed
            state={state.filter((content) => content.isPublic == false)}
          />
        </div>
      ) : (
        <></>
      )}
      {state.length != 0 ? (
        <div className={styles.my_timelapse}>
          <div className={styles.timeContainerName}>비공개 타입랩스</div>
          <MyFeed state={state.filter((content) => content.isPublic == true)} />
        </div>
      ) : (
        <></>
      )}
    </div>
  );
}

export default Profile;
