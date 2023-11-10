import { useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
function TokenRefresher() {
  const BASE_URL = `http://k9e203.p.ssafy.io`;
  const navigate = useNavigate;
  useEffect(() => {
    console.log("실행됨?");
    const refreshAPI = axios.create({
      baseURL: `${BASE_URL}`,
    });
    const interceptor = axios.interceptors.response.use(
      function (response) {
        return response;
      },
      async function (error) {
        const originalConfig = error.config;
        console.log(error);
        const msg = error.message;
        const status = error.code;
        if (status == 401) {
          if (msg == "access token expired") {
            await axios({
              url: `${BASE_URL}/api/v1/auth/reissue`,
              method: "POST",
              headers: {
                accesstoken: sessionStorage.getItem("accessToken"),
                refreshToken: document.cookie.getItem("refresh-token"),
              },
            })
              .then((res) => {
                console.log(res);
                localStorage.setItem("token", res.data.accessToken);
                originalConfig.headers["Authorization"] =
                  "Bearer " + res.data.accessToken;
                return refreshAPI(originalConfig);
              })
              .then(() => {
                window.location.reload();
              });
          } else if (msg == "refresh token expired") {
            localStorage.clear();
            navigate("/login");
            alert("Refresh token expired");
          } else if (msg == "mail token expired") {
            alert("다시요청해주세요");
          }
        } else if (status == 400 || status == 404 || status == 409) {
          alert(msg);
        }
        return Promise.reject(error);
      }
    );
    return () => {
      axios.interceptors.response.eject(interceptor);
    };
  }, []);
  return <div></div>;
}
export default TokenRefresher;
