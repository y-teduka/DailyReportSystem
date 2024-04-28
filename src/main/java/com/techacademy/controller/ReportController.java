package com.techacademy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.constants.ErrorMessage;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.service.EmployeeService;
import com.techacademy.service.ReportService;
import com.techacademy.service.UserDetail;

@Controller
@RequestMapping("reports")
public class ReportController {
    private final ReportService reportService;
    private final EmployeeService employeeService;

    @Autowired
    public ReportController(ReportService reportService, EmployeeService employeeService) {
        this.reportService = reportService;
        this.employeeService = employeeService;
    }

    // 日報一覧画面
    @GetMapping
    public String list(Model model) {
        model.addAttribute("listSize", reportService.findAll().size());
        model.addAttribute("reportList", reportService.findAll());
        return "reports/list";
    }

    // 日報新規登録画面
    @GetMapping(value = "/add")
    public String create(@ModelAttribute Report report, @AuthenticationPrincipal UserDetail userDetail, Model model) {
        model.addAttribute("employee", userDetail.getEmployee());
        return "reports/new";
    }

    // 日報新規登録処理
    @PostMapping(value = "/add")
    public String add(@Validated Report report, BindingResult res, Employee employee,
            @AuthenticationPrincipal UserDetail userDetail, Model model) {
        report.setEmployee(userDetail.getEmployee());
        employee = userDetail.getEmployee();
        // 入力チェック
        if (res.hasErrors()) {
            return create(report, userDetail, model);
        }
        // データ登録
        ErrorKinds result = reportService.save(report, employee, userDetail);
        // 業務チェック
        if (ErrorMessage.contains(result)) {
            model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
            return create(report, userDetail, model);
        }
        return "redirect:/reports";
    }

    // 日報詳細画面
    @GetMapping(value = "/{id}/")
    public String detail(@PathVariable Integer id, Model model) {
        model.addAttribute("report", reportService.findById(id));
        return "reports/detail";
    }

    // 日報削除処理
    @PostMapping(value = "/{id}/delete")
    public String delete(@PathVariable Integer id, Model model) {
        reportService.delete(id);
        return "redirect:/reports";
    }

    // 日報更新画面
    @GetMapping(value = "/{id}/update")
    public String edit(@PathVariable Integer id, Report report,Model model) {
        // 遷移元によって処理を分ける
        if (id == null) {
            // idがnullの場合
            return "reports/update";
        } else {
            // nullでない場合
            model.addAttribute("report", reportService.findById(id));
            return "reports/update";
        }
    }

    // 日報更新処理
    @PostMapping(value = "/{id}/update")
    public String update(@PathVariable Integer id,@Validated Report report, BindingResult res,Employee employee,
            Model model) {
        // 入力チェック
        if (res.hasErrors()) {
            // エラーの場合idにnullを入れて更新画面に遷移
            id = null;
            return edit(id, report,model);
        }
        
        //employee.setName(report.getEmployee().getName());
        employee=report.getEmployee();
        reportService.update(id,report,employee);
        return "redirect:/reports";
    }

}
