package com.onboarding.time_off.config.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "p_location")
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@DynamicInsert
public class LocationEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,updatable = false,columnDefinition = "BIGINT UNSIGNED")
    private Long ilocation;

    @ManyToOne
    @JoinColumn(name = "iuser")
    private UserEntity userEntity;
    /**
     * 여행할곳 이름으로 저장
     */
    @Column(nullable = false,columnDefinition = "VARCHAR(30)")
    private String locationName;
    /**
     * 시작 위치 혹은 단일 위치 위도
     */
    @Column(nullable = false)
    private double startMapX;
    /**
     * 시작위치 단일 위치 경도
     */
    @Column(nullable = false)
    private double startMapY;
    /**
     * 코스로 저장 할시 도작지 위도
     */
    @Column
    private double endMapX;
    /**
     * 코스로 저장 할시 도착지 경도
     */
    @Column
    private double endMapY;
    /**
     * 양방향 설정
     */
    @OneToMany(mappedBy = "locationEntity",cascade = CascadeType.ALL)
    private List<TravelPlanEntity> travelPlanEntities= new ArrayList<>();


}
