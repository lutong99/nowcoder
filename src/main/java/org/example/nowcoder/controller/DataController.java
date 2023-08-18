package org.example.nowcoder.controller;

import org.example.nowcoder.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
public class DataController {

    private DataService dataService;

    @Autowired
    public void setDateService(DataService dataService) {
        this.dataService = dataService;
    }

    @RequestMapping(value = "/data", method = {RequestMethod.GET, RequestMethod.POST})
    public String dataPage() {
        return "site/admin/data";
    }

    @PostMapping("/data/dau")
    public String dataDAUPage(Model model,
                              @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                              @DateTimeFormat(pattern = "yyyy-MM-dd") Date end
    ) {

        Long dau = dataService.statisticDAU(from, end);
        model.addAttribute("dau", dau);
        model.addAttribute("dauFrom", from);
        model.addAttribute("dauEnd", end);
        return "forward:/data";
    }

    @PostMapping("/data/uv")
    public String dataUVPage(Model model,
                             @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                             @DateTimeFormat(pattern = "yyyy-MM-dd") Date end
    ) {

        Long uv = dataService.statisticUV(from, end);
        model.addAttribute("uv", uv);
        model.addAttribute("uvFrom", from);
        model.addAttribute("uvEnd", end);
        return "forward:/data";
    }


}
