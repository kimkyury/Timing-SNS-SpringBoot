import styles from "./MainFeed.module.css";
import Feed from "../../components/Feed/Feed";
import { useEffect, useState } from "react";
import axios from "axios";
import _ from "lodash";
import ReactPullToRefresh from "react-pull-to-refresh";
const BASE_URL = `http://k9e203.p.ssafy.io`;

function MainFeed() {
  const [state, setState] = useState([]);
  // const [data, setData] = useState([]);
  // const [page, setPage] = useState("");
  // if (!JSON.parse(sessionStorage.getItem("myData"))) {
  //   console.log("chrlghk");
  //   // 데이터 저장
  //   const dataToStore = { FeeDPage: 1 };
  //   sessionStorage.setItem("myData", JSON.stringify(dataToStore));
  // }
  // useEffect(() => {
  //   console.log(JSON.parse(sessionStorage.getItem("myData")));
  //   const storedData = JSON.parse(sessionStorage.getItem("myData"));
  //   setPage(storedData.FeeDPage);
  //   console.log(page);
  //   if (page != 1) {
  //     getRecFeed();
  //   }
  // }, [page]);
  useEffect(() => {
    getRecFeed();
  }, []);
  const handleRefresh = () => {
    console.log("refresh");
    window.location.reload();
  };
  const getRecFeed = () => {
    const accessToken = sessionStorage.getItem("accessToken");
    axios
      .get(`${BASE_URL}/api/v1/feeds/recommended`, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      })
      .then((response) => {
        setState((prevData) => [...prevData, ...response.data]);
      })
      .catch((error) => {
        console.error(error);
      });
  };
  useEffect(() => {
    // 스크롤 이벤트 리스너 등록
    window.addEventListener("scroll", handleScroll);

    // 컴포넌트가 언마운트될 때 이벤트 리스너 제거
    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, []);
  // 스크롤 이벤트 핸들러
  const handleScroll = _.debounce(() => {
    // 문서의 높이
    const documentHeight = document.documentElement.offsetHeight;

    // 현재 스크롤 위치
    const scrollHeight = window.scrollY;

    // 창의 높이
    const windowHeight = window.innerHeight;
    // 화면 바닥에 도달했을 때 추가 데이터 로드
    // console.log(documentHeight);
    // console.log(scrollHeight);
    // console.log(windowHeight);
    if (scrollHeight + windowHeight > document.body.offsetHeight - 1) {
      // const storedData = JSON.parse(sessionStorage.getItem("myData"));
      console.log("바닥");
      getRecFeed();
      // const dataToStore = { FeeDPage: storedData.FeeDPage + 1 };
      // sessionStorage.setItem("myData", JSON.stringify(dataToStore));
      // setPage(storedData.FeeDPage + 1);
    }
  }, 100);
  console.log(state);
  return (
    <ReactPullToRefresh onRefresh={handleRefresh}>
      <div className={styles.container}>
        {state.length != 0 &&
          state.map((value, index) => <Feed key={index} data={value} />)}
      </div>
    </ReactPullToRefresh>
  );
}

export default MainFeed;
