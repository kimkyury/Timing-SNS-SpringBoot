import styles from "./DetailComment.module.css";
import Feed from "../../components/Feed/Feed";
import { useLocation } from "react-router";
import axios from "axios";
import { useEffect, useState } from "react";
function DetailComment() {
  const location = useLocation();
  const state = location.state;
  const [user, setUser] = useState(null);
  const BASE_URL = `http://k9e203.p.ssafy.io`;
  const [accessToken, setAccessToken] = useState(
    sessionStorage.getItem("accessToken")
  );
  useEffect(() => {
    axios
      .get(`${BASE_URL}/api/v1/members`, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      })
      .then((response) => {
        setUser(response.data);
      })
      .catch((error) => {
        console.error(error);
      });
  }, []);

  return (
    <div className={styles.container}>
      <Feed data={state} />
    </div>
  );
}

export default DetailComment;
