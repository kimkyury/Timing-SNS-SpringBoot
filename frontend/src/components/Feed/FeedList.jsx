import { useNavigate } from "react-router-dom";
import styles from "./FeedList.module.css";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import axios from "axios";
import { useState } from "react";

const BASE_URL = `http://k9e203.p.ssafy.io`;
function FeedList(data) {
  const [state, setState] = useState(data.state);
  const navigate = useNavigate();
  const accessToken = sessionStorage.getItem("accessToken");
  console.log("myfeed");
  console.log(state);
  const goToFeed = (i) => {
    getDetailFeed(i);
  };
  const getDetailFeed = (i) => {
    axios
      .get(`${BASE_URL}/api/v1/feeds/${i}`, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      })
      .then((response) => {
        setState(response.data);
        console.log(state, i, "datas");
        navigate(`/detailfeed/${i}`, {
          state: state.find((item) => item.id === i),
        });
      })
      .catch((error) => {
        console.error(error);
      });
  };
  return state ? (
    <div className={styles.myfeedcontainerName}>
      {/* 나의 피드 */}
      <div className={styles.image_container}>
        {Array.isArray(state) &&
          state.map((v, i) => (
            <div
              key={i}
              className={styles.imagebox}
              onClick={() => goToFeed(v.id)}
            >
              <img
                src={v.image}
                style={{ filter: v.isPublic ? "blur(5px)" : "" }}
              />
              {v.isPublic && <LockOutlinedIcon className={styles.lock} />}
            </div>
          ))}
      </div>
    </div>
  ) : (
    <></>
  );
}

export default FeedList;
