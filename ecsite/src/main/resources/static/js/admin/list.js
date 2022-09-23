'use strict'


var productData = null;

$(function() {
	$('#btn-search').click(function(event) {
		$('#product_ul').empty();
		search();
	});
});


function search() {

	//formの値を取得
	var formData = $('#product_saerch_form').serialize();

	//ajax通信
	$.ajax({
		type : "GET",
		url : '/admin/product/search',
		data: formData,
		dataType : 'json',
		contentType: 'application/json; charset=UTF-8',
		cache : false,
		timeout : 5000,
	}).done(function(data) {
		//ajax成功時の処理
		//console.log(data);
		//JSONを変数に入れる
		productData = data;

		//作成
		$.each(productData, function(index, value) {
			var text ="";

			//公開状況
		    if(value.release == true) {
		    	text = '<span class="release-open" ><span class="release" >公開</span></span>';
		    } else {
		    	text = '<span class="release-close" ><span class="release" >非公開</span></span>';
		    };
			var pid = Number(value.productId);
			var url = "/admin/product/detail/" + pid;

			if(value.productImage != "なし") {
				$('#product_ul').append('<li><a href="' + url +'">' + text + '<img src="' + value.productImage + '"><span class="textone textname">' + value.productName + '</span><span class="textone text-danger">&#165;<span>' + value.price + '</span></span></a></li>');
			} else {
				$('#product_ul').append('<li><a href="' + url +'">' + text + '<span class="nonimage text-dark">画像なし</span><span class="textone textname">' + value.productName + '</span><span class="textone text-danger">&#165;<span>' + value.price + '</span></span></a></li>');
			};
		});

	}).fail(function(jqHR, textStatus, errorThrown) {
		//ajax失敗時の処理
		alert('検索処理に失敗しました。');
	}).always(function() {
		//常に実行する処理（特になし）
	});
};

