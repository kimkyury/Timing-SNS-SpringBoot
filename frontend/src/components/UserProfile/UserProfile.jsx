import styles from "./UserProfile.module.css";
import axios from "axios";
import React, { useEffect, useState } from "react";
function UserProfile(data) {
  const BASE_URL = `http://k9e203.p.ssafy.io`;
  const [accessToken, setAccessToken] = useState(
    sessionStorage.getItem("accessToken")
  );
  const [info, setInfo] = useState(data.data);
  const [state, setState] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const formatEmail = (t) => {
    const s = t.indexOf("@");
    return t.substring(0, s);
  };
  const getProfile = () => {
    axios
      .get(`${BASE_URL}/api/v1/members`, {
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
    getProfile();
  }, []);

  return (
    <div>
      {isLoading ? (
        <div className={styles.container}>
          <div className={styles.imagebox}>
            <img
              src={state.profileImageUrl}
              className={styles.imageContainer}
            />
          </div>
          <div className={styles.mainContainer}>
            <div className={styles.upper}>
              <div className={styles.articlebox}>
                <div className={styles.name}>{state.nickname}</div>
                <div>{formatEmail(state.email)}</div>
              </div>
            </div>
            <div className={styles.lower}>
              <div className={styles.innerlower}>
                <div className={styles.innerhead}>{info.feedCount}</div>
                <div className={styles.innerfooter}>timelabs </div>
              </div>
              <div className={styles.innerlower}>
                <div className={styles.innerhead}>{info.contributeCount}</div>
                <div className={styles.innerfooter}>contribute </div>
              </div>
            </div>
          </div>
        </div>
      ) : (
        <></>
      )}
    </div>
  );
}

export default UserProfile;
