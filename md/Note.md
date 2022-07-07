## 아쉬운 점

---

- **Entity 설계의 미흡**
    - 이력을 남겨야한다는 부분에 집중하여 Entity 설계가 부족하다.
    - 요청 값 전체 엔티티 설계 -> 이력 엔티티만 설계 -> 결국 User, Review, Point History 엔티티 분리 설계
    - UserPoint 엔티티 ← history : OneToMany 로 설계했으면 어땠을까
        - User, Review, PointHistory 로 나누어 보았다.
        - PointHistory 또한 성격에 맞춰서 나누어 이력 Entity 를 설계 해야함을 깨달음
            - [관련링크](https://dataprofessional.tistory.com/70?category=355354) - 이력 Entity 설계 10가지 방법 : 읽어보면 좋다
- **프록시인듯 아닌듯한 EventServiceProxy**
    - 계획을 잡고 Spring Proxy 를 사용하여 동적으로 가짜 프록시 객체를 만들어 봐야겠다. 지금은 빈으로 등록된 서비스를 Map 으로 저장하여 구분하여 사용...
    - Proxy 객체를 만드는 과정을 만들었으나 그 이상을 사용하지 못하겠어서 위의 방법으로 만듬
        - Dynamic Proxy 방법으로 프록시 생성을 하였으나 마음에 들지 않아 폐기
            - Interface 를 상속하여 모든 메서드를 구현하는게 마음에 들지 않았음
        - CGLIB 방식의 Proxy 생성 방법을 더 공부해봐야겠다.

