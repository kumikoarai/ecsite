'use strict'

var urlhostname = "https://ecsite-sample-kumiko.net/ecsite";

$(function() {

	/** 削除ボタンを押したら*/
	$('#btn-delete').click(function(event) {
		deleteProduct();
	});
});


/** 商品の削除処理*/
function deleteProduct() {
	//formの値を取得
	var formData = $('#product-form').serializeArray();

	console.log(formData);
	//ajax通信
	$.ajax({
		type : "DELETE",
		cache : false,
		url : urlhostname + '/admin/product/delete',
		data: formData,
		dataType : 'json',
	}).done(function(data) {
		//ajax成功時の処理
		//console.log(data);
		alert("商品を削除しました。");
		window.location.href= urlhostname + "/admin/product/list";
	}).fail(function(jqHR, textStatus, errorThrown) {
		//ajax失敗時の処理
		alert('商品の削除に失敗しました。');
	}).always(function() {
		//常に実行する処理（特になし）
	});
};
