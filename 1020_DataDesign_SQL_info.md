# 고민사항

## 1️⃣ 피드간의 부모관계를 어떻게 할 것인가?

---

### 문제사항

-   각 피드는 영향력에 대하여 그래프 혹은 별도의 피드목록 창을 제공해야한다
-   각 피드는 root를 갖게 해야하며, 또한 직계Parnet를 알 수 있어야한다
    -   직계 Parent정보 보유 시, 트리 구조 형태로 보여 줄 수 있다
    -   root 정보 보유 시, 해당 관련 Feeds를 모두 가져올 수 있다

### 해결방법

-   Feed 테이블 내에 RootId, ParentId를 둘 것

### 유의사항

-   내부로직 구현시, 그룹 분리를 위한 union-find와 같은 형식말고 보다 효율적인 알고리즘을 찾아서 개선시킬 것

## 2️⃣ Feeds에서 NotNull설정을 해야하는가?

---

### Feeds > root_id : NULL 허용

-   만약, root_id를 가진 Feed가 삭제된다면, 해당 root_id는 null로 바뀔 수 있도록 해야한다
-   그러나, **내부로직에서는 root_id를 본인을 향하게 하도록** 바꿔줘야 한다

# 각 테이블의 FK 설정 정보

기본적으로 update에 대해서 fk들은 Cascade 설정됨

### challenges

-   member 탈퇴 시 Cascade

### Comments

-   Feed 삭제 시 Cascade
-   member 탈퇴 시 Cascade

### Feed_hash_tags

-   Hash_tags_option 삭제 시 Cascade
-   Feed 삭제 시 Cascade

### Feed

-   member 탈퇴 시 Cascade
-   parent_feed 삭제 시 Set Null
-   root_feed 삭제 시 Set Null //이를 위해 해당 column을 Null허용함

### Like

-   member 탈퇴 시 Cascade
-   Feed 삭제 시 Cascade

### members

### snapshots

-   Chellange 삭제 시 Cascade

### time_lapse_hash_tags

-   challenge 삭제 시 Cascade
-   Hash_tag_option 삭제 시 Cascade
