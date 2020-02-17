package com.neuedu.busines.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDetailsVo {
    private Integer id;
    private String name;
    private Integer categoryId;
    private String subtitle;
    private Integer status;
    private String details;
    private BigDecimal price;
    private String mainImage;
    private String subImages;
    private Integer stock;
    private Date createTime;
    private Date updateTime;
}
