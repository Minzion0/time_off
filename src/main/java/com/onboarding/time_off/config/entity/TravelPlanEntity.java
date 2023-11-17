package com.onboarding.time_off.config.entity;

import com.onboarding.time_off.config.entity.model.UserMbtiEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "p_travel_plan")
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@DynamicInsert
public class TravelPlanEntity extends BaseEntity{
    /**
     * 여행계획 pk
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false,nullable = false,columnDefinition = "BIGINT UNSIGNED")
    private Long itravelPlan;
//
//    /**
//     * userEntity fk 설정
//     * 글을 쓴 유저
//     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "iuser")
    private UserEntity userEntity;
//
//    /**
//     * 여행 계획명
//     */
    @Column(nullable = false,length = 50)
    private String planName;

    /**
     * 계획 타입 설정 J(계획형),P(즉흥형)
     */
    @Column(nullable = false)
    private UserMbtiEnum planType;

    /**
     * fk 설정
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ilocation")
    private LocationEntity locationEntity;

//    @OneToMany(mappedBy = "travelPlanEntity",cascade = CascadeType.ALL)
//    private List<TravelPlanEntity> travelPlanEntities= new ArrayList<>();

}
