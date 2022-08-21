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
		    $('#product_ul').append('<li><img src="' + value.productImage + '"><p>' + value.productName + '</p></li>');
		});

	}).fail(function(jqHR, textStatus, errorThrown) {
		//ajax失敗時の処理
		alert('検索処理に失敗しました。');
	}).always(function() {
		//常に実行する処理（特になし）
	});
};

