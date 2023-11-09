import { Outlet, Navigate } from "react-router-dom";
// import { setLogged } from "../store/slices/loggedSlice";
import { useEffect } from "react";
import Toast, { Error } from "../components/Toast/Toast";
// import Toast from "../components/Toast/Toast";

function PrivateRoute() {
  //   const auth = useSelector((state) => state.auth);
  //   const accessToken = auth.accessToken;
  const accessToken = sessionStorage.getItem("accessToken");
  useEffect(() => {
    if (!accessToken) {
      Error("로그인이 필요한 기능입니다.");
    }
  }, []);

  if (!accessToken) {
    return <Navigate to="/login" />;
  }

  return (
    <>
      <Toast />
      <Outlet />
    </>
  );
}

export default PrivateRoute;
