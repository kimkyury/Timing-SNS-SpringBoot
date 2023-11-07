import styles from "./MainFeed.module.css";
import Feed from "../../components/Feed/Feed";
import { useEffect, useState } from "react";
import axios from "axios";

const BASE_URL = `http://k9e203.p.ssafy.io`;

function MainFeed() {
  const [state, setState] = useState([]);

  useEffect(() => {
    const accessToken = sessionStorage.getItem("accessToken");
    axios
      .get(`${BASE_URL}/api/v1/feeds/recommeded`, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      })
      .then((response) => {
        setState(response.data);
      })
      .catch((error) => {
        console.error(error);
      });
  }, []);

  useEffect(() => {
    console.log(state);
  }, [state]);

  return (
    <div className={styles.container}>
      {state.length != 0 &&
        state.map((value, index) => <Feed key={index} data={value} />)}
    </div>
  );
}

export default MainFeed;
