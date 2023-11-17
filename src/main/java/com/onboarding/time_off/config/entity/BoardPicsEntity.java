package com.onboarding.time_off.config.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "p_board_pics")
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BoardPicsEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false,nullable = false,columnDefinition = "BIGINT UNSIGNED")
    private Long iboardPic;

    /**
     * 사진 파일명
     */
    @Column(nullable = false)
    private String picName;
    /**
     * 사진 순
     */
    @Column(nullable = false)
    private int picNum;


}
