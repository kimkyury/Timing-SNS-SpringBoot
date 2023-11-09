import styles from "./UpdateReview.module.css";
import { useLocation } from "react-router";
import Textarea from "@mui/joy/Textarea";
import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
function UpdateReview() {
  const { state } = useLocation();
  const BASE_URL = `http://k9e203.p.ssafy.io`;
  const [accessToken] = useState(sessionStorage.getItem("accessToken"));
  const [NewReview, setNewReivew] = useState(state.review);
  const [isPrivate] = useState(state.isPrivate);
  const navigate = useNavigate();
  console.log(isPrivate);
  const updateReview = () => {
    console.log(state);
    console.log("asdfasdf");
    console.log(NewReview);
    axios
      .patch(
        `${BASE_URL}/api/v1/feeds/${state.id}`,
        { isPrivate: isPrivate, review: NewReview },
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      )
      .then(() => {
        navigate("/");
      })
      .catch((error) => {
        console.error(error);
      });
  };
  return (
    <div className={styles.container}>
      <Textarea
        minRows={4}
        className={styles.contentbox}
        value={NewReview}
        onChange={(e) => setNewReivew(e.target.value)}
      />
      <button onClick={updateReview} className={styles.summitbtn}>
        수정
      </button>
    </div>
  );
}

export default UpdateReview;
