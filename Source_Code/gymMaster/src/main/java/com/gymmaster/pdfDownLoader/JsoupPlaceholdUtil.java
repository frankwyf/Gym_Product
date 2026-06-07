package com.gymmaster.pdfDownLoader;

import com.gymmaster.qr.QrCodeUtils;
import com.itextpdf.styledxmlparser.jsoup.Jsoup;
import com.itextpdf.styledxmlparser.jsoup.nodes.Document;
import com.itextpdf.styledxmlparser.jsoup.nodes.Element;
import com.itextpdf.styledxmlparser.jsoup.nodes.Node;

import java.io.File;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsoupPlaceholdUtil {
    // 变量替换，src-html模版位置，params-进行变量替换的真实数据,key与html模版中标签的id属性一致，value为真实数据
    public static String placeholder(String src, Map<String, Object> params, String realPath, BigDecimal total) throws Exception {
        File file = new File(src);
        // 通过Jsoup创建Document对象，Document就可以表示整个html文本了。
        Document document = Jsoup.parse(file, "utf-8");

        // 设置内容文本，真正进行变量替换的方法
        setText(document, params);
        String logoPath = "src/main/resources/static/logo/logo.png";
        QrCodeUtils.encode(params.toString(),logoPath,realPath,true);
        // 将变量替换好以后，输出html文本
        String h = "<img src='"+realPath+"'>";
        String m = "<p>-----------------------------------------------------------------------------------</p>";
        String x = "<h3>total :                     "+total;
        document.getElementsByTag("body").append(m);
        document.getElementsByTag("body").append(x);

        document.getElementsByTag("body").append(h);

        String outerHtml = document.outerHtml();
        //System.out.println(outerHtml);

        return outerHtml;
    }

    // 给html模版设置文本数据，document-html模版，params-进行变量替换的真实数据
    private static void setText(Document document, Map<String, Object> params) {
        Set<Map.Entry<String, Object>> entrySet = params.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            // 获取最后一个对应的element
            Element element = document.getElementsByAttributeValue("id", entry.getKey()).last();
            if ("tr".equals(element.tagName())) {
                List<Map<String, Object>> counselList = (List<Map<String, Object>>)entry.getValue();
                // 设置行，就是把列表数据设置到html的表格行中
                setRowsText(document, element, counselList);

            } else if("td".equals(element.tagName())){
                // 对html元素设置文本
                element.text(entry.getValue().toString());
            }
            else{

            }

        }
    }

    // 把列表数据设置到html的表格行中，document-html模版，element-表示一行的元素，即tr标签。list-真实列表数据
    private static void setRowsText(Document document, Element element, List<Map<String, Object>> list) {

        if (list.isEmpty()) {
            return;
        }

        Iterator<Map<String, Object>> iterator = list.iterator();

        do {
            Map<String, Object> counsel = iterator.next();
            // 设置文本数据
            setText(document, counsel);

            if (iterator.hasNext()) {
                // 追加一行
                appendTableRow(element);
            }

        } while (iterator.hasNext());

        // 如果list集合中还有元素，则复制当前element追加到当前element后面，并循环到前面一步，
        // 如果list集合中没有元素了，则说明内容已经写完了，返回即可
    }

    // 扩展一行
    private static void appendTableRow(Element element) {
        Node parent = element.parent();
        Element tbody = (Element) parent;
        tbody.appendChild((Node) element.clone());
    }

}
