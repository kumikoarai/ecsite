package com.example.domain.service;

import java.util.List;

import com.example.entity.ProductMaxQuantity;
import com.example.entity.ProductOptionColor;

public interface ProductOptionService {

	/**商品の購入数量上限と色の登録*/
	public void postMaxQuantityAndColor(ProductMaxQuantity productMaxQuantity, List<ProductOptionColor> productOptionColor);

	/**商品の購入数量上限と色の更新*/
	public void updateMaxQuantityAndColor(ProductMaxQuantity productMaxQuantity, List<ProductOptionColor> productOptionColor);

	/**商品の購入数量上限の取得*/
	public Integer getProductMaxQuantity(Integer productId);

	/**商品の色の取得*/
	public List<String> getProductOptionColor(Integer productId);
}
