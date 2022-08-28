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
});