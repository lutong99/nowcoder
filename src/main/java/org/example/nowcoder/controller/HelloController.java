package org.example.nowcoder.controller;

import org.example.nowcoder.service.AlphaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/alpha")
public class HelloController {

    private AlphaService alphaService;

//    @Autowired
    public void setAlphaService(AlphaService alphaService) {
        this.alphaService = alphaService;
    }

    @ResponseBody
    @RequestMapping("/sayHello")
    public String sayHello() {
        return "Hello world";
    }


    @ResponseBody
    @GetMapping("/data")
    public String data() {
        return alphaService.find();
    }

    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("request.getMethod() = " + request.getMethod());
        System.out.println("request.getServletPath() = " + request.getServletPath());
        Enumeration<String> headerNames = request.getHeaderNames();
        System.out.println("Headers:");
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            System.out.println("  " + headerName + ": " + headerValue);
        }
        String code = request.getParameter("code");
        System.out.println("code = " + code);

        response.setContentType("text/html; charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.write("<h1>Nowcoder</h1>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "/students", method = RequestMethod.GET)
    @ResponseBody
    public String students(@RequestParam(value = "offset", defaultValue = "1", required = false) int offset,

                           @RequestParam(value = "limit", defaultValue = "10", required = false) int limit) {
        System.out.println("offset = " + offset);
        System.out.println("limit = " + limit);
        return "Some students";

    }

    @RequestMapping(value = "/student/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String student(@PathVariable(value = "id", required = true) String id) {
        System.out.println("id = " + id);
        return "Student" + id;
    }

    @RequestMapping(value = "/student", method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name, int age) {
        System.out.println("name = " + name);
        System.out.println("age = " + age);
        return "Success";
    }

    @RequestMapping(value = "/demo", method = RequestMethod.GET)
    public ModelAndView demoPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name", "齐天大圣");
        modelAndView.addObject("gender", "雄");
        modelAndView.setViewName("/demo/view");
        return modelAndView;
    }

    @RequestMapping(value = "/school", method = RequestMethod.GET)
    public String schoolDemo(Model model) {
        model.addAttribute("name", "北京大学");
        model.addAttribute("gender", "985 211 双一流 全国top2");
        return "demo/view";
    }

    @RequestMapping(value = "/emp", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> emp() {
        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "张三");
        emp.put("age", 128);
        emp.put("salary", 123000);
        return emp;
    }

    @RequestMapping(value = "/emps", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> emps() {


        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "张三");
        emp.put("age", 128);
        emp.put("salary", 123000);

        Map<String, Object> emp2 = new HashMap<>();
        emp2.put("name", "张仪");
        emp2.put("age", 30);
        emp2.put("salary", 687543268);

        Map<String, Object> emp3 = new HashMap<>();
        emp3.put("name", "商鞅");
        emp3.put("age", 44);
        emp3.put("salary", 687687987698L);

        List<Map<String, Object>> list = new ArrayList<>();
        list.add(emp);
        list.add(emp2);
        list.add(emp3);
        return list;
    }

}
