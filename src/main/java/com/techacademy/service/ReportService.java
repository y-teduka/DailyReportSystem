package com.techacademy.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
    private final EmployeeRepository employeerepository;

    @Autowired
    public ReportService(ReportRepository reportRepository, EmployeeRepository employeeRepository) {
        this.reportRepository = reportRepository;
        this.employeerepository = employeeRepository;
    }

    // 日報登録
    @Transactional
    public ErrorKinds save(Report report, Employee employee, UserDetail userDetail) {

        // 日報重複チェック
        for (Report re : findByEmployee(employee)) {
            if (re.getReportDate().equals(report.getReportDate())) {

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
    @Transactional
    public ErrorKinds update(Integer id, Report report,Report  nyuuryoku, Employee employee) {

        // 日報重複チェック
        for (Report re : findByEmployee(employee)) {
            // データベースのReportDateと入力したReportDateが同じか
            if (re.getReportDate().equals(nyuuryoku.getReportDate())) {
                if(!(re.getId().equals(nyuuryoku.getId()))) {

                return ErrorKinds.DATECHECK_ERROR;
                }
            }
        }
        // idでDBを検索
        Optional<Report> option = reportRepository.findById(id);
        // 検索したidのCreateAtを取得してreportエンティティに格納
        report.setCreatedAt(option.get().getCreatedAt());
        LocalDateTime now = LocalDateTime.now();
        report.setUpdatedAt(now);
        report.setReportDate(nyuuryoku.getReportDate());
        reportRepository.save(report);
        return ErrorKinds.SUCCESS;
    }

    // 日報一覧表示処理
    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    // 従業員に紐づく日報検索処理
    public List<Report> findByEmployee(Employee employee) {
        List<Report> list = reportRepository.findByEmployee(employee);
        return list;
    }

    // 日報削除処理
    @Transactional
    public Report delete(Integer id) {
        Report report = findById(id);
        report.setDeleteFlg(true);

        return reportRepository.save(report);
    }

    // idで１件検索
    public Report findById(Integer id) {
        Optional<Report> option = reportRepository.findById(id);
        // 取得できなかった場合はnullを返す
        Report report = option.orElse(null);
        return report;
    }

}