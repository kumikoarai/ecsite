

$(function() {
	var nowchecked = $('input[name="release"]:checked').val();
	$('input[name="release"]').click(function(){
		if($(this).val() == nowchecked) {
			$(this).prop('checked', false);
			nowchecked = false;
		} else {
			nowchecked = $(this).val();
		}
	});

	$('#file').on('change', function (e) {
	    var reader = new FileReader();
	    reader.onload = function (e) {
	        $("#preview").attr('src', e.target.result);
	    }
	    reader.readAsDataURL(e.target.files[0]);
	});


	$("#add_color").click(function() {
		//console.log("add_colorをクリックした");
		var thisinput = $(this);
		thisinput.parent(".form-group").append('<input type="text" placeholder="カラーを入力" class="form-control" id="colors" name="colors" value=""><span class="color_sakujo">削除</span>');

		$(".color_sakujo").click(function() {
			//console.log("color_sakujoをクリックした");
			var thissakujo = $(this);
			thissakujo.prev("input").remove();
			thissakujo.remove();
		});
	});
	$(".color_sakujo").click(function() {
		/*console.log("color_sakujoをクリックした");*/
		var thissakujo = $(this);
		thissakujo.prev("input").remove();
		thissakujo.remove();
	});


});