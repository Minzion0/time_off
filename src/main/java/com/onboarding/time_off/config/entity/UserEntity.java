package com.onboarding.time_off.config.entity;

import com.onboarding.time_off.config.entity.model.SocialEnum;
import com.onboarding.time_off.config.entity.model.UserGradeEnum;
import com.onboarding.time_off.config.entity.model.UserMbtiEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_user")
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@DynamicInsert
public class UserEntity extends BaseEntity{
    /**
     * 유저 pk
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false,nullable = false,columnDefinition = "BIGINT UNSIGNED")
    private Long iuser;
    /**
     * Email 저장 아이디 데신 활용한다
     */
    @Column(nullable = false)
    private String email;
    /**
     * 카카오,네이버 회원인지 구분한다
     */
    @Column(nullable = false)
    private SocialEnum social;
    /**
     * 유저의 프로필사진 소셜 프로필사진을 기본으로 들고온다
     */
    @Column
    private String uPic;
    /**
     * 유저의 mbit중 J계획형인지 P즉흥형인지 파악
     */
    @Column
    private UserMbtiEnum mbti;
    /**
     * 회원 주소
     */
    @Column
    private String arr;
    /**
     * 회원 등급
     */
    @Column(nullable = false)
    private UserGradeEnum grade;

    /**
     * 게시물과 유저의 관계설정
     */
    @OneToMany(mappedBy = "userEntity",cascade = CascadeType.ALL)
    private List<TravelPlanEntity> travelPlanEntities = new ArrayList<>();

    @OneToMany(mappedBy = "userEntity",cascade = CascadeType.ALL )
    private List<LocationEntity> locationEntities = new ArrayList<>();

    @OneToMany(mappedBy = "userEntity",cascade = CascadeType.ALL)
    private List<BoardEntity> boardEntities= new ArrayList<>();
}
