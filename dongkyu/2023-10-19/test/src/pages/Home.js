import React, { useEffect, useState } from "react";

export default function Home() {
  const [videos, setVideos] = useState([]);
  const BASE_URL = `http://localhost:8080`;

  useEffect(() => {
    setVideos([1, 2, 3, 4, 5]);

    return () => {
      setVideos([]);
    };
  }, []);

  const videoStyle = {
    margin: 0,
    padding: 0,
    border: "none",
    display: "inline-block", // 비디오를 인라인 요소처럼 나타냄
  };

  return (
    <div>
      {videos.map((e, k) => (
        <video id={k} width="320" height="240" autoPlay muted loop style={videoStyle}>
          <source src={`${BASE_URL}/video/${e}`} type="video/mp4" />
          Your browser does not support the video tag.
        </video>
      ))}
    </div>
  );
}
