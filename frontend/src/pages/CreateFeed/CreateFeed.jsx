import styles from "./CreateFeed.module.css"
import Input from '@mui/joy/Input';
function CreateFeed() {

    return (
        <div className={styles.container}>
            <div className={styles.boxname}>시작일</div>
            <Input type="date" />
            <div className={styles.boxname}>#태그</div>
            <Input type="text" />
            <div className={styles.boxname}>달성목표</div>
            <Input type="text" />
            <button className={styles.btn}>제출</button>
        </div>
    )
  }
  
  export default CreateFeed