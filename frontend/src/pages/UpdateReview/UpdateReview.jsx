import styles from "./UpdateReview.module.css";
import { useLocation } from "react-router";
import Textarea from "@mui/joy/Textarea";
import { useState } from "react";
function UpdateReview() {
  const { state } = useLocation();
  const [NewReview, setNewReivew] = useState(state.content);

  const updateReview = () => {};
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
