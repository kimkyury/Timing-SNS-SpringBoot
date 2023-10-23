import styles from "./Footer.module.css"
import HomeOutlinedIcon from "@mui/icons-material/HomeOutlined";
import HomeRoundedIcon from "@mui/icons-material/HomeRounded";
import FavoriteRoundedIcon from '@mui/icons-material/FavoriteRounded';
import FavoriteBorderRoundedIcon from '@mui/icons-material/FavoriteBorderRounded';
import PersonRoundedIcon from '@mui/icons-material/PersonRounded';
import PersonOutlineOutlinedIcon from '@mui/icons-material/PersonOutlineOutlined';
import PageviewOutlinedIcon from '@mui/icons-material/PageviewOutlined';
import PageviewRoundedIcon from '@mui/icons-material/PageviewRounded';
import { useEffect } from "react";
// import { useSelector } from "react-redux";
// import { RootState } from "../../store/store";

import { useNavigate, useLocation } from "react-router-dom";

function Footer() {
  const navigate = useNavigate();
  const location = useLocation();

  //   const currentUrl = useSelector((state: RootState) => state.router.currentUrl);
  const currentUrl = location.pathname;

  useEffect(() => {
    console.log(currentUrl);
  }, [currentUrl]);

  return (
    <div>
        <div className={styles.footer}>
        {currentUrl === "/" ? (
          <HomeRoundedIcon/>
        ) : (
          <HomeOutlinedIcon
            onClick={() => {
              navigate("/");
            }}
          />
        )}
        {currentUrl === "/like" ? (
          <FavoriteRoundedIcon />
        ) : (
          <FavoriteBorderRoundedIcon
            onClick={() => {
              navigate("/like");
            }}
          />
        )}
        {currentUrl === "/search" ? (
          <PageviewRoundedIcon />
        ) : (
          <PageviewOutlinedIcon
            onClick={() => {
              navigate("/search");
            }}
          />
        )}
        {currentUrl === "/profile" ? (
          <PersonRoundedIcon />
        ) : (
          <PersonOutlineOutlinedIcon
            onClick={() => {
              navigate("/profile");
            }}
          />
        )}
      </div>
    </div>
  );
}

export default Footer;
