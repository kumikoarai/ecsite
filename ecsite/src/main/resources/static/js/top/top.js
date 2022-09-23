'use strict'


var productData = null;

$(function() {
	$('#btn-search').click(function(event) {
		$('#product_ul').empty();
		$('#pankuzu').empty();
		search();
	});
});


function search() {

	//formの値を取得
	var formData = $('#product_saerch_form').serialize();

	//ajax通信
	$.ajax({
		type : "GET",
		url : '/top/search',
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
			var pid = Number(value.productId);
			var url = "/top/product/detail/" + pid;
			if(value.productImage != "なし") {
				$('#product_ul').append('<li><a href="' + url +'"><img src="' + value.productImage + '"><span class="textone textname">' + value.productName + '</span><span class="textone text-danger">&#165;<span>' + value.price + '</span></span></a></li>');
			} else {
				$('#product_ul').append('<li><a href="' + url +'"><span class="nonimage text-dark">画像なし</span><span class="textone textname">' + value.productName + '</span><span class="textone text-danger">&#165;<span>' + value.price + '</span></span></a></li>');
			};
		});

	}).fail(function(jqHR, textStatus, errorThrown) {
		//ajax失敗時の処理
		alert('検索処理に失敗しました。');
	}).always(function() {
		//常に実行する処理（特になし）
	});
};

