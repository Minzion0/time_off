package com.onboarding.time_off.config.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "p_board")
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)

public class BoardEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,updatable = false,columnDefinition = "BIGINT UNSIGNED")
    private Long iboard;

    @ManyToOne
    @JoinColumn(name = "itravelPlan")
    private TravelPlanEntity travelPlanEntity;

    @ManyToOne
    @JoinColumn(name = "iuser")
    private UserEntity userEntity;

    /**
     * 본문
     */
    @Column(nullable = false,columnDefinition = "TEXT")
    private String ctnt;
    /**
     * 메인 썸네일
     */
    @Column
    private String mainPic;



}
