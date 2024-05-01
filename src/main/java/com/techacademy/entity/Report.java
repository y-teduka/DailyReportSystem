package com.techacademy.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalDate;

import org.hibernate.annotations.SQLRestriction;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "reports")
@SQLRestriction("delete_flg = false")
public class Report {

    // 主キー。自動生成
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    // 日付
    @Column(nullable = false)
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reportDate;

    // タイトル
    @Column(nullable = false)
    @NotEmpty
    @Length(max = 100)
    private String title;

    // 内容
    @Length(max = 600)
    @Column(columnDefinition = "LONGTEXT",nullable = false)
    @NotEmpty
    private String content;

    // 社員番号
    @ManyToOne
    @JoinColumn(name = "employee_code", referencedColumnName = "code", nullable = false)
    private Employee employee;

    // 削除フラグ
    @Column(columnDefinition = "TINYINT",nullable = false)
    private boolean deleteFlg;

    // 登録日時
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // 更新日時
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
