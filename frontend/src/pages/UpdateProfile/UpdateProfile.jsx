import styles from "./UpdateProfile.module.css";
import { useLocation } from "react-router";
import Textarea from "@mui/joy/Textarea";
import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
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
        console.log(res);
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
    <div className={styles.container}>
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
    </div>
  );
}

export default UpdateProfile;
