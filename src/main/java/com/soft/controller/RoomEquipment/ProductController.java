package com.soft.controller.RoomEquipment;

import com.soft.pojo.Product;
import com.soft.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 获取所有产品列表（用于下拉框）
     */
    @GetMapping("/productList")
    public Map<String, Object> getProductList() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Product> products = productService.list();
            result.put("code", 200);
            result.put("data", products);
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "查询失败：" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据产品ID获取功能模块列表
     */
    @GetMapping("/productFunctions")
    public Map<String, Object> getProductFunctions(@RequestParam Integer productId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Product product = productService.getById(productId);
            if (product != null && product.getFunctions() != null) {
                // 将逗号分隔的字符串转为数组
                String[] functions = product.getFunctions().split(",");
                result.put("code", 200);
                result.put("data", functions);
            } else {
                result.put("code", 400);
                result.put("msg", "该产品暂无功能模块");
            }
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "查询失败：" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}