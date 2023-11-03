import { useEffect, useState } from "react";
import styles from "./Feed.module.css";
import { useNavigate, useLocation } from "react-router-dom";
import FavoriteBorderOutlinedIcon from "@mui/icons-material/FavoriteBorderOutlined";
import FavoriteOutlinedIcon from "@mui/icons-material/FavoriteOutlined";
import SmsOutlinedIcon from "@mui/icons-material/SmsOutlined";
import ShareOutlinedIcon from "@mui/icons-material/ShareOutlined";
import EditOutlinedIcon from "@mui/icons-material/EditOutlined";
import dog from "../../assets/dog.jpg";
function Feed() {
  const navigate = useNavigate();
  const location = useLocation();
  const currentUrl = location.pathname;

  const [user, setUser] = useState(null);
  const [state, setState] = useState([]);
  const [newComment, setNewComment] = useState("");

  useEffect(() => {
    const user = {
      memberId: 12,
      email: "doglover18@naver.com",
      nickname: "김정희",
      profileImg: `${dog}`,
    };
    setUser(user);
    const state = {
      id: 1,
      user: {
        memberId: 12,
        email: "doglover18@naver.com",
        nickname: "김정희",
        profileImg: `${dog}`,
      },
      parentId: null,
      rootId: null,
      goalContent: "몸짱",
      content: "안녕하세요!!",
      thumbnailUrl: `${dog}`,
      isPrivate: true,
      isLike: true,
      time: new Date(),
      hashTag: ["#운동", "#오운완"],
      likeCount: 1234567,
      commentCount: 765432,
      shareCount: 1000,
      comments: [
        {
          memberId: 12,
          name: "김정희",
          content: "뭐",
          profileImg: `${dog}`,
          time: new Date() - 10000,
        },
        {
          memberId: 11,

          name: "하성호",
          content: "ㅋㅋㅋ",
          profileImg: `${dog}`,
          time: new Date() - 10,
        },
        {
          memberId: 11,
          name: "하성호",
          content: "ㅋㅋㅋ",
          profileImg: `${dog}`,
          time: new Date() - 10,
        },
        {
          memberId: 11,
          name: "하성호",
          content: "ㅋㅋㅋ",
          profileImg: `${dog}`,
          time: new Date() - 10,
        },
        {
          memberId: 11,
          name: "하성호",
          content: "ㅋㅋㅋ",
          profileImg: `${dog}`,
          time: new Date() - 10,
        },
        {
          memberId: 11,
          name: "하성호",
          content: "ㅋㅋㅋ",
          profileImg: `${dog}`,
          time: new Date() - 10,
        },
        {
          memberId: 11,
          name: "하성호",
          content: "ㅋㅋㅋ",
          profileImg: `${dog}`,
          time: new Date() - 10,
        },
        {
          memberId: 11,
          name: "하성호",
          content: "ㅋㅋㅋ",
          profileImg: `${dog}`,
          time: new Date() - 10,
        },
        {
          memberId: 11,
          name: "하성호",
          content: "ㅋㅋㅋ",
          profileImg: `${dog}`,
          time: new Date() - 10,
        },
        {
          memberId: 11,
          name: "하성호",
          content: "ㅋㅋㅋ",
          profileImg: `${dog}`,
          time: new Date() - 10,
        },
        {
          memberId: 11,
          name: "하성호",
          content: "ㅋㅋㅋ",
          profileImg: `${dog}`,
          time: new Date() - 10,
        },
        {
          memberId: 11,
          name: "하성호",
          content: "ㅋㅋㅋ",
          profileImg: `${dog}`,
          time: new Date() - 10,
        },
        {
          memberId: 11,
          name: "하성호",
          content: "ㅋㅋㅋ",
          profileImg: `${dog}`,
          time: new Date() - 10,
        },
        {
          memberId: 11,
          name: "하성호",
          content: "ㅋㅋㅋ",
          profileImg: `${dog}`,
          time: new Date() - 10,
        },
        {
          memberId: 11,
          name: "하성호",
          content: "ㅋㅋㅋ",
          profileImg: `${dog}`,
          time: new Date() - 10,
        },
        {
          memberId: 11,
          name: "하성호",
          content: "ㅋㅋㅋ",
          profileImg: `${dog}`,
          time: new Date() - 10,
        },
      ],
    };
    setState(state);
  }, []);

  const gotoDetailComment = () => {
    if (currentUrl == "/") {
      navigate(`/detailcomment/${state.id}`, { state });
    }
  };
  const gotoDetailFeed = () => {
    navigate(`/detailfeed/${state.id}`, { state });
  };

  const gotoEdit = () => {
    navigate(`/updatereview/${state.id}`, { state });
  };

  const formatK = (count) => {
    if (count >= 100000) {
      return (count / 1000000).toFixed(1) + "백만";
    } else if (count >= 1000) {
      return (count / 1000).toFixed(1) + "천";
    } else {
      return count;
    }
  };
  const formatT = (c) => {
    if (c / 60 < 1) {
      return (c & 60).toFixed(0) + "초전";
    } else if (c / 60 >= 1 && c / 60 / 60 < 1) {
      return (c / 60).toFixed(0) + "분전";
    } else if (c / 60 / 60 > 1) {
      return (c / 60 / 60).toFixed(0) + "시간전 ";
    }
  };
  const formatEmail = (t) => {
    const s = t.indexOf("@");
    return t.substring(0, s);
  };
  const handleAddComment = () => {
    if (newComment.trim() !== "") {
      const updatedComments = [...state.comments];
      updatedComments.push({
        profileImg: state,
        memberId: state.user.id,
        content: newComment,
        time: new Date(),
      });
      state.content = updatedComments;
      setState(state);
      setNewComment(""); // 댓글 추가 후 새 댓글 상태 초기화
    }
  };

  const activeEnter = (e) => {
    if (e.key === "Enter") {
      handleAddComment();
    }
  };

  // const handleDeleteComment = (commentIndex) => {
  //     const commentToDelete = comments[commentIndex];
  //     if (commentToDelete.name === user.id) {
  //         const updatedComments = [...comments];
  //         updatedComments.splice(commentIndex, 1);
  //         setComments(updatedComments);
  //     }
  // };
  return (
    <div>
      {state.length != 0 ? (
        <div className={styles.container}>
          {/* 게시글 주인 정보 */}
          <div className={styles.nameContainer}>
            <img src={state.user.profileImg} className={styles.profileimage} />
            <div className={styles.namebox}>
              <div className={styles.name}>{state.user.nickname}</div>
              <div className={styles.id}>{formatEmail(state.user.email)}</div>
            </div>
          </div>

          {/* 게시글 이미지 */}
          <img
            src={state.thumbnailUrl}
            className={styles.imageContainer}
            onClick={gotoDetailFeed}
          />

          {/* 게시글 좋아요, 댓글, 이어가기 정보 */}
          <div className={styles.tagContainer}>
            <div className={styles.tagitem}>
              <div className={styles.tagitemicon}>
                {state.isLiked ? (
                  <FavoriteOutlinedIcon
                    style={{ width: "4vw", height: "4vw" }}
                  />
                ) : (
                  <FavoriteBorderOutlinedIcon
                    style={{ width: "4vw", height: "4vw" }}
                  />
                )}
              </div>
              <div>{formatK(state.likeCount)}</div>
            </div>
            <div className={styles.tagitem}>
              <div className={styles.tagitemicon}>
                <SmsOutlinedIcon style={{ width: "4vw", height: "4vw" }} />
              </div>
              <div>{formatK(state.commentCount)}</div>
            </div>
            <div className={styles.tagitem}>
              <div className={styles.tagitemicon}>
                <ShareOutlinedIcon style={{ width: "4vw", height: "4vw" }} />
              </div>
              <div>{formatK(state.shareCount)}</div>
            </div>

            {/* 수정버튼 */}

            {currentUrl == "/" ? (
              <></>
            ) : (
              <>
                {state.length != 0 && user.memberId == state.user.memberId ? (
                  <EditOutlinedIcon
                    className={styles.editbtn}
                    onClick={gotoEdit}
                  />
                ) : (
                  <></>
                )}
              </>
            )}
          </div>

          {/* 게시글 본문 */}
          <div className={styles.contentContainer}>
            <div className={styles.name}>{state.user.nickname}</div>
            <div className={styles.content}>{state.content}</div>
          </div>

          {/* 게시글 해시태그 */}
          <div className={styles.hashTagContainer}>
            {state.length != 0 &&
              state.hashTag.map((v, i) => (
                <div key={i} className={styles.hash}>
                  <div>{v}</div>
                </div>
              ))}
          </div>

          {/* 게시글 시간 정보 */}
          <div>{formatT(new Date() - state.time)}</div>

          {/* 게시글 댓글 */}
          {currentUrl != "/" && state.length != 0 && (
            <div className={styles.floatBottom}>
              {state.comments.map((v, i) => (
                <div key={i} className={styles.commentContainer}>
                  <img src={v.profileImg} className={styles.commentImage} />
                  <div className={styles.commentbox}>
                    <div className={styles.commentInfo}>
                      <div className={styles.name}>{v.name}</div>
                      <div className={styles.time}>
                        {formatT(new Date() - v.time)}
                      </div>
                    </div>
                    <div className={styles.comment}>{v.content}</div>
                  </div>
                  {/* {user.id == v.name ? <CloseOutlinedIcon onClick={() => handleDeleteComment(i)} /> : ''} */}
                </div>
              ))}
            </div>
          )}

          {user && (
            <div
              className={
                currentUrl == "/"
                  ? styles.commentContainer
                  : styles.commentContainerFix
              }
            >
              <img src={user.profileImg} className={styles.commentImage} />
              <input
                type="text"
                className={styles.commentInput}
                placeholder="댓글 달기..."
                value={newComment}
                onChange={(e) => setNewComment(e.target.value)}
                onClick={gotoDetailComment}
                onKeyDown={(e) => activeEnter(e)}
              />
              {newComment.length != 0 && (
                <button
                  onClick={handleAddComment}
                  className={styles.commentBtn}
                >
                  추가
                </button>
              )}
            </div>
          )}
        </div>
      ) : (
        <></>
      )}
    </div>
  );
}

export default Feed;
