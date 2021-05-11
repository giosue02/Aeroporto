$(document).ready(function () {
    $(function () {
        var $page = $(location).attr('href');
        var $vett = $page.split("/");
        for ($i = 0; $i < $vett.length; $i++) {
            if ($vett[$i] === "") {
                $vett.splice($i, 1);
            }
        }
        $('#myTopnav a').each(function () {
            var $href = $(this).attr('href');
            var $length = $vett.length;
            if ($href === "index.jsp" && $vett[($length - 1)] === "Aeroporto") {
                $href = "Aeroporto";
            }
            if ($page.includes($href)) {
                $(this).addClass('active');
            } else {
                $(this).removeClass('active');
            }
        });
    });
});

$(document).on("click", ".addFlight", function () {
    $("#ModalAddFlight").modal("show");
});

$(document).on("click", "#confermaAddFlight", function () {
    let sigla = $('#siglaVolo').val();
    let aeroporto = $('#aeroporto').val();
    let compagnia = $('#compagnia').val();
    let codicevolo = $('#codicevolo').val();
    let partenza = $('#partenza').val();
    let arrivo = $('#arrivo').val();
    let tipo = "";
    let flag = $('input[name="tipo"]:checked').val();
    if (flag == "and") {
        tipo = "And";
    } else if (flag == "rit") {
        tipo = "Rit";
    }

    if (sigla !== "" && aeroporto !== "" && compagnia !== "" && codicevolo !== "" && partenza !== "" && arrivo !== "" && tipo != "") {
        $.ajax({
            url: "AggiungiVolo",
            type: "POST",
            cache: false,
            data: {sigla: sigla, aeroporto: aeroporto, compagnia: compagnia, codicevolo: codicevolo, partenza: partenza, arrivo: arrivo, tipo: tipo},
            success: function () {
                $('#ModalAddFlight').modal('hide');
                $('#siglaVolo').val("");
                $('#aeroporto').val("");
                $('#and').prop('checked', false);
                $('#rit').prop('checked', false);
                location.assign("ComputerCentrale");
            },
        });
    } else {
        alert("Campi non compilati!");
    }
});

$(document).on("click", "#annullaAddFlight", function () {
    $('#siglaVolo').val("");
    $('#aeroporto').val("");
    $('#compagnia').val("");
    $('#codicevolo').val("");
    $('#partenza').val("");
    $('#arrivo').val("");
    $('#and').prop('checked', false);
    $('#rit').prop('checked', false);
});

$(document).on("click", ".addCompany", function () {
    $("#ModalAddCompany").modal("show");
});

$(document).on("click", "#confermaAddCompany", function () {
    let nome_compagnia = $('#nome_compagnia').val();

    if (nome_compagnia !== "") {
        $.ajax({
            url: "AggiungiCompagnia",
            type: "POST",
            cache: false,
            data: {nome_compagnia: nome_compagnia},
            success: function () {
                $('#ModalAddCompany').modal('hide');
                $('#nome_compagnia').val("");
                location.assign("ComputerCentrale");
            },
        });
    } else {
        alert("Campi non compilati!");
    }
});

$(document).on("click", "#annullaAddCompany", function () {
    $('#nome_compagnia').val("");
});

$(document).on("click", ".editFlight", function () {
    let chiave_volo = $(this).data('id');
    let vett = chiave_volo.split("#");
    let id = vett[0];
    let sigla = vett[1];
    let aeroporto = vett[2];
    let compagnia = vett[3];
    let codice_volo = vett[4];
    let partenza = vett[5];
    let arrivo = vett[6];
    let tipo = vett[7];
    $('#idVoloMod').text(id);
    $('#siglaVoloMod').val(sigla);
    $('#aeroportoMod').val(aeroporto);
    $('#compagniaMod option[value="' + compagnia + '"]').prop("selected", true);
    $('#codicevoloMod').val(codice_volo);
    $('#partenzaMod').val(partenza);
    $('#arrivoMod').val(arrivo);
    $("input[name=tipoMod][value=" + tipo + "]").prop('checked', true);
    $("#ModalEditFlight").modal("show");
});

$(document).on("click", "#confermaEditFlight", function () {
    let id = $('#idVoloMod').text();
    let sigla = $('#siglaVoloMod').val();
    let aeroporto = $('#aeroportoMod').val();
    let compagnia = $('#compagniaMod').val();
    let codicevolo = $('#codicevoloMod').val();
    let partenza = $('#partenzaMod').val();
    let arrivo = $('#arrivoMod').val();
    let tipo = "";
    let flag = $('input[name="tipoMod"]:checked').val();
    if (flag === "And") {
        tipo = "And";
    } else if (flag === "Rit") {
        tipo = "Rit";
    }
    if (sigla !== "" && aeroporto !== "" && compagnia !== "" && codicevolo !== "" && partenza !== "" && arrivo !== "" && tipo !== "") {
        $.ajax({
            url: "ModificaVolo",
            type: "POST",
            cache: false,
            data: {sigla: sigla, aeroporto: aeroporto, compagnia: compagnia, codicevolo: codicevolo, partenza: partenza, arrivo: arrivo, tipo: tipo, id: id},
            success: function () {
                $('#ModalEditFlight').modal('hide');
                $('#siglaVoloMod').val("");
                $('#aeroportoMod').val("");
                $('#andMod').prop('checked', false);
                $('#ritMod').prop('checked', false);
                location.assign("ComputerCentrale");
            },
        });
    } else {
        alert("Campi non compilati!");
    }
});

$(document).on("click", "#annullaEditFlight", function () {
    $('#siglaVoloMod').val("");
    $('#aeroportoMod').val("");
    $('#andMod').prop('checked', false);
    $('#ritMod').prop('checked', false);
});

$(document).on("click", ".editCompany", function () {
    let id_nome = $(this).data('id');
    let vett = id_nome.split("#");
    let nome_compagnia = vett[0];
    let id_compagnia = vett[1];
    $('#idCompagniaMod').text(id_compagnia);
    $('#nomeCompagniaMod').val(nome_compagnia);
    $("#ModalEditCompany").modal("show");
});

$(document).on("click", "#confermaEditCompany", function () {
    let nome_compagnia = $('#nomeCompagniaMod').val();
    let id_compagnia = $('#idCompagniaMod').text();
    if (nome_compagnia !== "") {
        $.ajax({
            url: "ModificaCompagnia",
            type: "POST",
            cache: false,
            data: {nome_compagnia: nome_compagnia, id_compagnia: id_compagnia},
            success: function () {
                $('#ModalEditCompany').modal('hide');
                $('#nomeCompagniaMod').val("");
                location.assign("ComputerCentrale");
            },
        });
    } else {
        alert("Campi non compilati!");
    }
});

$(document).on("click", "#annullaEditCompany", function () {
    $('#nome_compagniaMod').val("");
});

$(document).on("click", ".delFlight", function () {
    let idVolo = $(this).data('id');
    $("#idVolo").text(idVolo);
    $("#ModalDelFlight").modal("show");
});

$(document).on("click", "#cancellaFlight", function () {
    let idVolo = $("#idVolo").text();
    ;
    $.ajax({
        url: 'EliminaVolo',
        type: 'post',
        data: {idVolo: idVolo},
        success: function () {
            $('#ModalDelFlight').modal('hide');
            location.assign("ComputerCentrale");
        }
    });
});

$(document).on("click", ".delCompany", function () {
    let idCompagnia = $(this).data('id');
    $("#idCompagnia").text(idCompagnia);
    $("#ModalDelCompany").modal("show");
});

$(document).on("click", "#cancellaCompany", function () {
    let idCompagnia = $("#idCompagnia").text();
    ;
    $.ajax({
        url: 'EliminaCompagnia',
        type: 'post',
        data: {idCompagnia: idCompagnia},
        success: function () {
            $('#ModalDelCompany').modal('hide');
            location.assign("ComputerCentrale");
        }
    });
});
