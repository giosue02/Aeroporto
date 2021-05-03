$(document).ready(function () {
    $(function () {
        var $page = $(location).attr('href');
        var $vett = $page.split("/");
        for ($i = 0; $i < $vett.length; $i++) {
            if ($vett[$i] === "") {
                $vett.splice($i, 1);
            }
        }
        $('#menu a').each(function () {
            var $href = $(this).attr('href');
            var $length = $vett.length;
            if ($href === "index.jsp" && $vett[($length - 1)] === "Aeroporto") {
                $href = "Aeroporto";
            }
            if ($page.includes($href)) {
                $(this).addClass('currentPage');
            } else {
                $(this).removeClass('currentPage');
            }
        });
    });
    
    
});




