import styles from "./DetailFeed.module.css";
import { useEffect, useState } from "react";
import FileDownloadOutlinedIcon from "@mui/icons-material/FileDownloadOutlined";
import DeleteOutlinedIcon from "@mui/icons-material/DeleteOutlined";
import LoopOutlinedIcon from "@mui/icons-material/LoopOutlined";
import LockOpenIcon from "@mui/icons-material/LockOpen";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import { useNavigate } from "react-router-dom";
import dog from "../../assets/dog.jpg";
import axios from "axios";
import { useLocation } from "react-router-dom";
function DetailFeed() {
  const location = useLocation();
  const data = location.state;
  const [user, setUser] = useState(null);
  const [state, setState] = useState(null);
  const navigate = useNavigate();
  const BASE_URL = `http://k9e203.p.ssafy.io`;
  const [accessToken, setAccessToken] = useState(
    sessionStorage.getItem("accessToken")
  );
  useEffect(() => {
    const user = { id: "@헬린이", profile_img: `${dog}` };
    setUser(user);
    // const state = {
    //   id: 1,
    //   user: {
    //     memberId: 12,
    //     email: "123@naver.com",
    //     nickname: "김정희",
    //     profileImg: `${dog}`,
    //   },
    //   parentId: null,
    //   rootId: null,
    //   goalContent: "몸짱",
    //   content: "안녕하세요!!",
    //   thumbnailUrl: `${dog}`,
    //   isPrivate: true,
    //   isLike: true,
    //   time: new Date(),
    //   hashTag: ["#운동", "#오운완"],
    //   likeCount: 1234567,
    //   commentCount: 765432,
    //   shareCount: 1000,
    //   comments: [
    //     {
    //       memberId: 12,
    //       name: "김정희",
    //       content: "뭐",
    //       profileImg: `${dog}`,
    //       time: new Date() - 10000,
    //     },
    //     {
    //       memberId: 11,

    //       name: "하성호",
    //       content: "ㅋㅋㅋ",
    //       profileImg: `${dog}`,
    //       time: new Date() - 10,
    //     },
    //     {
    //       memberId: 11,
    //       name: "하성호",
    //       content: "ㅋㅋㅋ",
    //       profileImg: `${dog}`,
    //       time: new Date() - 10,
    //     },
    //     {
    //       memberId: 11,
    //       name: "하성호",
    //       content: "ㅋㅋㅋ",
    //       profileImg: `${dog}`,
    //       time: new Date() - 10,
    //     },
    //     {
    //       memberId: 11,
    //       name: "하성호",
    //       content: "ㅋㅋㅋ",
    //       profileImg: `${dog}`,
    //       time: new Date() - 10,
    //     },
    //     {
    //       memberId: 11,
    //       name: "하성호",
    //       content: "ㅋㅋㅋ",
    //       profileImg: `${dog}`,
    //       time: new Date() - 10,
    //     },
    //     {
    //       memberId: 11,
    //       name: "하성호",
    //       content: "ㅋㅋㅋ",
    //       profileImg: `${dog}`,
    //       time: new Date() - 10,
    //     },
    //     {
    //       memberId: 11,
    //       name: "하성호",
    //       content: "ㅋㅋㅋ",
    //       profileImg: `${dog}`,
    //       time: new Date() - 10,
    //     },
    //     {
    //       memberId: 11,
    //       name: "하성호",
    //       content: "ㅋㅋㅋ",
    //       profileImg: `${dog}`,
    //       time: new Date() - 10,
    //     },
    //     {
    //       memberId: 11,
    //       name: "하성호",
    //       content: "ㅋㅋㅋ",
    //       profileImg: `${dog}`,
    //       time: new Date() - 10,
    //     },
    //     {
    //       memberId: 11,
    //       name: "하성호",
    //       content: "ㅋㅋㅋ",
    //       profileImg: `${dog}`,
    //       time: new Date() - 10,
    //     },
    //     {
    //       memberId: 11,
    //       name: "하성호",
    //       content: "ㅋㅋㅋ",
    //       profileImg: `${dog}`,
    //       time: new Date() - 10,
    //     },
    //     {
    //       memberId: 11,
    //       name: "하성호",
    //       content: "ㅋㅋㅋ",
    //       profileImg: `${dog}`,
    //       time: new Date() - 10,
    //     },
    //     {
    //       memberId: 11,
    //       name: "하성호",
    //       content: "ㅋㅋㅋ",
    //       profileImg: `${dog}`,
    //       time: new Date() - 10,
    //     },
    //     {
    //       memberId: 11,
    //       name: "하성호",
    //       content: "ㅋㅋㅋ",
    //       profileImg: `${dog}`,
    //       time: new Date() - 10,
    //     },
    //     {
    //       memberId: 11,
    //       name: "하성호",
    //       content: "ㅋㅋㅋ",
    //       profileImg: `${dog}`,
    //       time: new Date() - 10,
    //     },
    //   ],
    // };
    // setState(state);
  }, []);
  const getDetailFeed = () => {
    axios
      .get(`${BASE_URL}/api/v1/feeds/${data.id}`, {
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
  };

  const setIsPublic = () => {
    const currentState = { ...state };
    setState(currentState);
    if (currentState.isPublic) {
      currentState.isPublic = false;
    } else {
      currentState.isPublic = true;
    }
    setState(currentState);
  };

  const gotodetail = () => {
    navigate(`/detailcomment/${state.id}`, { state });
  };
  const download = () => {
    // 여기서 파일 다운로드 로직 실행
  };

  const deleteFeed = () => {
    // 여기서 피드 삭제 로직 실행
  };
  useEffect(() => {
    // getDetailFeed();
    console.log(data);
  }, []);
  return (
    <div className={styles.container}>
      {state && (
        <>
          <div className={styles.lock}>
            {user.id != state.user.id ? (
              <></>
            ) : state.isPublic ? (
              <LockOutlinedIcon onClick={setIsPublic} className={styles.icon} />
            ) : (
              <LockOpenIcon onClick={setIsPublic} className={styles.icon} />
            )}
          </div>

          <img src={state.thumbnailUrl} className={styles.imageContainer} />
          <div className={styles.etcbox}>
            <div className={styles.etcinner} onClick={gotodetail}>
              <LoopOutlinedIcon className={styles.etcimg} />
              댓글보기
            </div>
            {user.id != state.id && (
              <div className={styles.etcinner}>
                <LoopOutlinedIcon className={styles.etcimg} />
                이어받기
              </div>
            )}
            {user.id == state.user.id && (
              <>
                <div className={styles.etcinner} onClick={download}>
                  <FileDownloadOutlinedIcon className={styles.etcimg} />
                  다운로드
                </div>
                <div className={styles.etcinner} onClick={deleteFeed}>
                  <DeleteOutlinedIcon className={styles.etcimg} />
                  삭제
                </div>
              </>
            )}
          </div>
        </>
      )}
    </div>
  );
}

export default DetailFeed;
