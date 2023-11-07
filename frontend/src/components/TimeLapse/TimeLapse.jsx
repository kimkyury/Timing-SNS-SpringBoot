import styles from "./TimeLapse.module.css";
import { useNavigate } from "react-router-dom";
import {
  CircularProgressbar,
  buildStyles,
  CircularProgressbarWithChildren,
} from "react-circular-progressbar";
import "react-circular-progressbar/dist/styles.css";
import dog from "../../assets/dog.jpg";
import dog2 from "../../assets/dog2.jpg";
import { useEffect, useState } from "react";
import ConfettiExplosion from "react-confetti-explosion";
import axios from "axios";
function TimeLapse() {
  const navigate = useNavigate();
  const [timeLaps, setTimeLaps] = useState([]);
  const [isFinished, setIsFinished] = useState(false);
  const BASE_URL = `http://k9e203.p.ssafy.io`;
  const [accessToken, setAccessToken] = useState(
    sessionStorage.getItem("accessToken")
  );

  const finishTimeLaps = () => {
    setIsFinished(!isFinished);
  };

  const takePhoto = (index) => {
    navigate(`/jeonghui`, { state: timeLaps[index] });
    // if (timeLaps[index].percent == 0) {
    //     navigate(`/jeonghui`, { state: timeLaps[index] });
    // } else {
    //     navigate(`/doChallenge`);
    // }
  };

  const continueTimeLaps = () => {
    // 타입랩스 이어가기 위한 로직 추가
    setIsFinished(false);
  };

  const discontinueTimeLaps = () => {
    // 타입랩스를 피드로 변환하는 로직 추가
    setIsFinished(false);
  };
  const largeProps = {
    force: 0.8,
    duration: 3000,
    particleCount: 300,
    width: 1600,
    colors: ["#041E43", "#1471BF", "#5BB4DC", "#FC027B", "#66D805"],
  };
  const getChallenge = () => {
    axios
      .get(`${BASE_URL}/api/v1/challenges`, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      })
      .then((response) => {
        setTimeLaps(response.data.challenges);
      })
      .catch((error) => {
        console.error(error);
      });
  };
  useEffect(() => {
    getChallenge();
  }, []);
  return (
    <div className={styles.container}>
      {timeLaps.length != 0 ? (
        <div>
          {timeLaps.map((element, index) => (
            <div key={index} className={styles.timeLaps}>
              <div
                className={styles.circularContainer}
                onClick={() => takePhoto(index)}
              >
                <CircularProgressbarWithChildren
                  value={element.percent}
                  strokeWidth="10"
                  styles={buildStyles({ pathColor: "red", textColor: "black" })}
                >
                  <img src={element.img} className={styles.timeLapsImage} />
                </CircularProgressbarWithChildren>
              </div>
            </div>
          ))}
        </div>
      ) : (
        <></>
      )}
      <div className={styles.timeLaps} onClick={() => navigate(`/create`)}>
        <div className={styles.timeLaps}>
          <CircularProgressbar
            value="40"
            text="+"
            strokeWidth="10"
            styles={buildStyles({
              pathColor: "red",
              textColor: "black",
              textSize: "50px",
            })}
          />
        </div>
      </div>

      {isFinished && (
        <div className={styles.finish}>
          <ConfettiExplosion {...largeProps} />
          <div className={styles.finishContainer}>
            <div>타임랩스를 완료했습니다!!</div>
            <div>이어하시면 21일이 추가됩니다.</div>
            <div>이어하시겠습니까?</div>
            <div className={styles.buttonContainer}>
              <button
                className={styles.continueTimeLaps}
                onClick={continueTimeLaps}
              >
                예
              </button>
              <button
                className={styles.discontinueTimeLaps}
                onClick={discontinueTimeLaps}
              >
                아니요
              </button>
            </div>

            {isFinished && (
              <div className={styles.finish}>
                <ConfettiExplosion {...largeProps} />
                <div className={styles.finishContainer}>
                  <div>타임랩스를 완료했습니다!!</div>
                  <div>이어하시면 21일이 추가됩니다.</div>
                  <div>이어하시겠습니까?</div>
                  <div className={styles.buttonContainer}>
                    <button
                      className={styles.continueTimeLaps}
                      onClick={continueTimeLaps}
                    >
                      예
                    </button>
                    <button
                      className={styles.discontinueTimeLaps}
                      onClick={discontinueTimeLaps}
                    >
                      아니요
                    </button>
                  </div>
                </div>
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
}

export default TimeLapse;
