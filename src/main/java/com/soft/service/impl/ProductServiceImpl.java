package com.soft.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.pojo.Product;
import com.soft.service.ProductService;
import com.soft.mapper.ProductMapper;
import org.springframework.stereotype.Service;

/**
* @author 12
* @description 针对表【t_product(产品表)】的数据库操作Service实现
* @createDate 2026-07-11 23:07:48
*/
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
    implements ProductService{

}




