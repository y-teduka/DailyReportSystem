package com.techacademy.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.repository.EmployeeRepository;
import com.techacademy.repository.ReportRepository;

@Service
public class ReportService {
    private final ReportRepository reportRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository, EmployeeRepository employeeRepository) {
        this.reportRepository = reportRepository;
        this.employeeRepository = employeeRepository;
    }

    // 日報登録
    @Transactional
    public ErrorKinds save(Report report, Employee employee, UserDetail userDetail) {
        
        //Employee employee= userDetail.getEmployee();
        // 日報重複チェック
        for (Report re : findByEmployee(employee)) {
            if (re.getReportDate() == report.getReportDate()) {

                return ErrorKinds.DATECHECK_ERROR;
            }
        }

        report.setDeleteFlg(false);
        LocalDateTime now = LocalDateTime.now();
        report.setCreatedAt(now);
        report.setUpdatedAt(now);

        reportRepository.save(report);
        return ErrorKinds.SUCCESS;
    }

    // 日報更新

    // 日報削除

    // 日報一覧表示処理
    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    // 従業員に紐づく日報検索処理
    public List<Report> findByEmployee(Employee employee) {
        List<Report> list = reportRepository.findByEmployee(employee);
        return list;
    }
}
