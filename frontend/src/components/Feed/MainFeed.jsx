import styles from "./MainFeed.module.css"
import FavoriteBorderOutlinedIcon from '@mui/icons-material/FavoriteBorderOutlined';
import FavoriteOutlinedIcon from '@mui/icons-material/FavoriteOutlined';
import SmsOutlinedIcon from '@mui/icons-material/SmsOutlined';
import ShareOutlinedIcon from '@mui/icons-material/ShareOutlined';
import dog from "../../assets/dog.jpg"
function Feed() {
    const state={
        profileimage:`${dog}`,
        name:"kim",
        id:"@abcd",
        image:`${dog}`,
        isLiked:true,
        likes:123,
        comment:321,
        share:1000,
        hash:["#개","#댕댕이","#시바","#산책"],
        comments:[{name:"시바",comment:"뭐"},{name:"씨바",comment:"ㅋㅋㅋ"},{name:"치바",comment:"ㅎㅎㅎㅎ"}]
    }
    return (
        <div className={styles.container}>
            <div className={styles.nameContainer}>
                <div>
                    <img src={state.profileimage} className={styles.profileimage}/>
                </div>
                <div className={styles.namebox}>
                    <div className={styles.name}>{state.name}</div>
                    <div className={styles.id}>{state.id}</div>
                </div>
                <div className={styles.etc}>etc</div>
                
            </div>
            <div className={styles.imageContainer}>
                <img src={state.image} className={styles.imageContainer}/>
            </div>
            <div className={styles.tagContainer}>
                <div className={styles.tagitem}>
                    <div>{state.isLiked ? <FavoriteOutlinedIcon/>:<FavoriteBorderOutlinedIcon/>}</div>
                    <div>{state.likes}</div>
                    
                </div>
                <div className={styles.tagitem}>
                    <div><SmsOutlinedIcon/></div>
                    <div>{state.comment}</div>
                    
                </div>
                <div className={styles.tagitem}>
                    <div><ShareOutlinedIcon/></div>
                    <div>{state.share}</div>
                    
                </div>
                
            </div>
            <div className={styles.hashTagContainer}>
            {state.hash.map((v, i) => (
                      <div key={i} className={styles.hash}>
                        <div>{v}</div>
                      </div>
                    ))}
            </div>
            <div className={styles.commentContainer}>
            {state.comments.map((v, i) => (
                      <div key={i} className={styles.commentbox}>
                        <div className={styles.commentname}>{v.name} : </div>
                        <div className={styles.commentcontent}>{v.comment}</div>
                      </div>
                    ))}
            </div>
        </div>
    )
  }
  
  export default Feed