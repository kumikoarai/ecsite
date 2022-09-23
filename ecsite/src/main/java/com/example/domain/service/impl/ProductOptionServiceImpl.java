package com.example.domain.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.service.ProductOptionService;
import com.example.entity.ProductMaxQuantity;
import com.example.entity.ProductOptionColor;
import com.example.repository.ProductMaxQuantityRepository;
import com.example.repository.ProductOptionColorRepository;

@Service
public class ProductOptionServiceImpl implements ProductOptionService {

	@Autowired
	private ProductMaxQuantityRepository productMaxQuantityRepository;

	@Autowired
	private ProductOptionColorRepository productOptionColorRepository;



	/**商品の購入数量上限と色の登録*/
	@Transactional
	@Override
	public void postMaxQuantityAndColor(ProductMaxQuantity productMaxQuantity, List<ProductOptionColor> productOptionColor) {

		//商品の購入数量上限の登録
		productMaxQuantityRepository.save(productMaxQuantity);

		//商品の色の登録
		productOptionColorRepository.saveAll(productOptionColor);
	}



	/**商品の購入数量上限の取得*/
	@Override
	public Integer getProductMaxQuantity(Integer productId) {
		ProductMaxQuantity productMaxQuantity = productMaxQuantityRepository.findByProductId(productId);
		if(productMaxQuantity == null) {
			return 0;
		}
		return productMaxQuantity.getMaxQuantity();
	}



	/**商品の色の取得*/
	@Override
	public List<String> getProductOptionColor(Integer productId) {
		 List<ProductOptionColor> productOptionColors = productOptionColorRepository.findByProductId(productId);

		 List<String> colors = new ArrayList<String> ();
		 for(ProductOptionColor item : productOptionColors) {
			 colors.add(item.getColor());
		 }
		return colors;
	}



	/**商品の購入数量上限と色の更新*/
	@Transactional
	@Override
	public void updateMaxQuantityAndColor(ProductMaxQuantity productMaxQuantity, List<ProductOptionColor> productOptionColor) {
		//商品の購入数量上限の更新
		ProductMaxQuantity findproductMaxQuantity = productMaxQuantityRepository.findByProductId(productMaxQuantity.getProductId());
		if(findproductMaxQuantity != null) {
			productMaxQuantityRepository.updateMaxQuantity(productMaxQuantity.getProductId(), productMaxQuantity.getMaxQuantity());
		} else {
			productMaxQuantityRepository.save(productMaxQuantity);
		}

		//商品の色の削除
		productOptionColorRepository.deleteByProductId(productMaxQuantity.getProductId());
		//商品の色の登録
		productOptionColorRepository.saveAll(productOptionColor);
	}

}
