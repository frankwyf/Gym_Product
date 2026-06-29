package com.gymmaster.pdfDownLoader;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gymmaster.common.BackMsg;
import com.gymmaster.entity.Bill;
import com.gymmaster.entity.Customer;
import com.gymmaster.entity.Logs;
import com.gymmaster.mail.aa.SendMailService;
import com.gymmaster.service.BillService;
import com.gymmaster.service.CustomerService;
import com.gymmaster.service.LogService;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.font.FontSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/student")
@SuppressWarnings("null")
public class StudentController1 {

    @Autowired
    BillService billService;
    @Autowired
    CustomerService customerService;

    @GetMapping("/placehold/jsoup")
    public String jsoup(int[] bil, String realPath) throws Exception {
        Map<String, Object> params = new HashMap<>();
        LambdaQueryWrapper<Bill> queryWrapper = new LambdaQueryWrapper<>();
        ArrayList<Bill> bills = new ArrayList<>();
        BigDecimal total = new BigDecimal(0);
        for (int i = 0; i<bil.length;i++) {
            for(Bill bill: billService.list(queryWrapper)){

                if(bil[i]==bill.getBid()) {
                    bills.add(bill);
                    total =total.add(bill.getFigure());
                }
            }
        }
        LambdaQueryWrapper<Customer> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Customer::getUid,bills.get(0).getUid());
        String name = customerService.getOne(queryWrapper1).getUsername();
        List<Map<String, Object>> counselList = new ArrayList<>();
        for (Bill bill: bills) {
            counselList.add(getCounsel(name, bill.getFname(), bill.getVname(), bill.getBdate().toString(), bill.getFigure().toString()));
        }


        params.put("billList", counselList);

        String html = JsoupPlaceholdUtil.placeholder("src/main/resources/templates/pdfTem/StudentReport.html", params, realPath , total);

        return html;
    }

    private Map<String, Object> getCounsel(String name, String sex, String age, String father, String mother) {
        Map<String, Object> params = new HashMap<>();
        params.put("username", name);
        params.put("facility", sex);
        params.put("venue", age);
        params.put("date", father);
        params.put("bill", mother);
        return params;
    }

    @Autowired
    LogService logService;
    @GetMapping("/export/jsoup")
    public void exportJsoup(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String x = request.getParameter("bill");

        String[] bills = x.split(",");
        int[] bil = new int[bills.length];
        for (int i = 0; i<bills.length;i++){
            bil[i] = Integer.parseInt(bills[i]);
        }

        String destPath = "src/main/resources/";
        String fileName2 = UUID.randomUUID().toString()+".jpg";
        String dataPath = "static/billQR/" + fileName2;
        String realPath = destPath + dataPath;

        String jsoupHtml = jsoup(bil,realPath);
        ConverterProperties converterProperties = new ConverterProperties();
        FontSet fontSet = new FontSet();
        if (!fontSet.addFont("src/main/resources/templates/pdfTem/SimHei.ttf")) {
            throw new RuntimeException("获取字体失败");
        }
        converterProperties.setFontProvider(new FontProvider(fontSet));

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        String fileName1 = UUID.randomUUID().toString();
        String base_url = "src/main/resources/";
        String database_value = "static/pdfPackage/"+fileName1+".pdf";
        String real = base_url+database_value;


        FileOutputStream fileOutputStream = new FileOutputStream(real);
        HtmlConverter.convertToPdf(jsoupHtml, bos, converterProperties);
        HtmlConverter.convertToPdf(jsoupHtml,fileOutputStream,converterProperties);



        Logs logs = new Logs();
        LambdaQueryWrapper<Bill> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(Bill::getBid,bil[0]);


        logs.setUid(billService.getOne(queryWrapper).getUid());
        logs.setOperation(dataPath);
        logs.setLogDate(LocalDateTime.now());
        logs.setUri(database_value);

        logService.save(logs);
        String fileName = "将jsoup生成的html转换成pdf文件";

        // 设置中文文件名
        fileName = new String(fileName.getBytes("utf-8"),"iso8859-1");
        String encode = URLEncoder.encode(fileName, "iso8859-1");

        ServletOutputStream outputStream = response.getOutputStream();
        response.setContentType("application/x-download");
        response.addHeader("Content-Disposition", "attachment; filename=" + encode + ".pdf");
        response.setCharacterEncoding("UTF-8");
        outputStream.write(bos.toByteArray());
    }
    @Autowired
    private SendMailService sendMailService;
    @GetMapping("/export/getAll")
    public BackMsg<String> sendAll(int id) throws Exception {
        LambdaQueryWrapper<Bill> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Bill::getUid,id);
        String m = "";
        for(Bill bill: billService.list(queryWrapper)){
            m = m + bill.getBid()+",";
        }
        m = m.substring(0,m.length()-1);
        sendBill(m);
        return BackMsg.success("success");
    }

    @GetMapping("/export/sendBill")
    public BackMsg<String> sendBill(String x) throws Exception {

        String[] bills = x.split(",");
        int[] bil = new int[bills.length];
        for (int i = 0; i<bills.length;i++){
            bil[i] = Integer.parseInt(bills[i]);
        }

        String destPath = "src/main/resources/";
        String fileName2 = UUID.randomUUID().toString()+".jpg";
        String dataPath = "static/billQR/" + fileName2;
        String realPath = destPath + dataPath;

        String jsoupHtml = jsoup(bil,realPath);
        ConverterProperties converterProperties = new ConverterProperties();
        FontSet fontSet = new FontSet();
        if (!fontSet.addFont("src/main/resources/templates/pdfTem/SimHei.ttf")) {
            throw new RuntimeException("获取字体失败");
        }
        converterProperties.setFontProvider(new FontProvider(fontSet));

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        String fileName1 = UUID.randomUUID().toString();
        String base_url = "src/main/resources/";
        String database_value = "static/pdfPackage/"+fileName1+".pdf";
        String real = base_url+database_value;


        FileOutputStream fileOutputStream = new FileOutputStream(real);
        HtmlConverter.convertToPdf(jsoupHtml, bos, converterProperties);
        HtmlConverter.convertToPdf(jsoupHtml,fileOutputStream,converterProperties);

        Logs logs = new Logs();
        LambdaQueryWrapper<Bill> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(Bill::getBid,bil[0]);
        logs.setUid(billService.getOne(queryWrapper).getUid());
        logs.setOperation(dataPath);
        logs.setLogDate(LocalDateTime.now());
        logs.setUri(database_value);

        logService.save(logs);
        LambdaQueryWrapper<Customer> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Customer::getUid,billService.getOne(queryWrapper).getUid());
        try {
            sendMailService.sendMail(customerService.getOne(queryWrapper1).getEmail(),real);

        }
        catch (Exception e){
            return BackMsg.error("email cannot be sent successfully");
        }
        return BackMsg.success("bill successfully sent!");
    }

}

