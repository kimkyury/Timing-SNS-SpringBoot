import styles from "./UpdateProfile.module.css";
import { useLocation } from "react-router";
import Textarea from "@mui/joy/Textarea";
import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { motion } from "framer-motion";
function UpdateProfile() {
  const { state } = useLocation();
  console.log(state);
  const BASE_URL = `http://k9e203.p.ssafy.io`;
  const [accessToken] = useState(sessionStorage.getItem("accessToken"));
  const [newNickName, setNewNickName] = useState(state.nickname);
  const [newProfileIMG, setNewProfileIMG] = useState(state.profileImageUrl);
  const navigate = useNavigate();
  const updateProfile = () => {
    // console.log(newProfileIMG);
    const formData = new FormData();
    formData.append(
      "memberUpdateRequest",
      JSON.stringify({ nickname: newNickName })
    );

    formData.append("profileImage", state.profileImageUrl);
    axios
      .patch(`${BASE_URL}/api/v1/members`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: `Bearer ${accessToken}`,
        },
      })
      .then((res) => {
        navigate("/profile");
      })
      .catch((error) => {
        console.error(error);
      });
  };
  const handleFileChange = (e) => {
    const selectedFiles = e.target.files;
    console.log(e.target.value);
    console.log(e.target);
    setNewProfileIMG([...selectedFiles]);
  };
  return (
    <motion.div
      initial={{ opacity: 0, x: 100 }}
      animate={{ opacity: 1, x: 0 }}
      exit={{ opacity: 0, x: -100 }}
      transition={{ duration: 0.4 }}
      className={styles.container}
    >
      <Textarea
        minRows={4}
        className={styles.contentbox}
        value={newNickName}
        onChange={(e) => setNewNickName(e.target.value)}
      />
      <input
        type="file"
        id="file"
        multiple="multipart"
        // value={newProfileIMG}
        onChange={handleFileChange}
      ></input>
      <button onClick={updateProfile} className={styles.summitbtn}>
        수정
      </button>
    </motion.div>
  );
}

export default UpdateProfile;
