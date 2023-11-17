package com.onboarding.time_off.config.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Table(name = "p_type_plan")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TypePlanEntity extends BaseEntity{
    /**
     * pk
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false,nullable = false,columnDefinition = "BIGINT UNSIGNED")
    private Long itypePlan;
    /**
     * 구분
     */
    @Column(nullable = false)
    private String category;
    /**
     * 장소
     */
    @Column
    private String location;
    /**
     * 일시
     */
    @Column
    private LocalDateTime dateTime;
    /**
     * 비고
     * 100자로 최대한 간결하게 작성
     */
    @Column(length = 100)
    private String note;

}
