import React, { useEffect } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";

const KakaoLogin = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const accessToken = searchParams.get("access-token"); // test

  useEffect(() => {
    if (accessToken) {
      alert("로그인 성공");
      console.log(accessToken);
      sessionStorage.setItem("accessToken", accessToken);
      navigate("/");
    }

    return () => {};
  }, [accessToken, navigate]);

  return (
    <div>
      <p>로그인</p>
    </div>
  );
};

export default KakaoLogin;
