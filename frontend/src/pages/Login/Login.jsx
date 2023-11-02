import styles from "./Login.module.css";
import kakao from "../../assets/kakao.png";
import logo from "../../assets/logo.png";

function Login() {
  const gotokakao = () => {
    console.log("KAKAO");
  };
  return (
    <div className={styles.container}>
      <div className={styles.upppertitle}>21일 동안의 타임랩스로</div>
      <div className={styles.lowertitle}>당신만의 습관을 기르세요</div>

      <img src={kakao} className={styles.loginbtn} onClick={gotokakao} />
      <div>
        <img src={logo} className={styles.logoimg} />
      </div>
    </div>
  );
}

export default Login;
