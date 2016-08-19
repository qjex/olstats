var apiUrl = "http://127.0.0.1:8081/api"

var nameByPlatform = {};
var idsByAttr = {};
var nameByAttr = {};
var platforms;
var lists;
var selectedPlatforms = []

function getData(platformsResponse, listsResponse) {
    $.getJSON(apiUrl + "/get_platforms", function(data) {
        platformsResponse(data);
    });
    $.getJSON(apiUrl + "/get_lists", function(data) {
        listsResponse(data);
    });
}

function addPlatforms(result) {
    platforms = result.data;
    $('#loading-platforms').hide();
    for (i = 0; i < platforms.length; i++) {
        var divCheckbox = document.createElement("div");
        divCheckbox.setAttribute("class", "checkbox");
        var label = document.createElement("label");
        var cb = document.createElement("input");
        cb.setAttribute("type", "checkbox");
        cb.value = platforms[i].name;
        nameByPlatform[platforms[i].name] = platforms[i].siteName;
        label.appendChild(cb);
        label.appendChild(document.createTextNode(platforms[i].siteName));

        divCheckbox.appendChild(label);
        $("#platforms-list").append(divCheckbox);
    }
}

function addLists(result) {
    lists = result.data;
    $('#loading-lists').hide();
    for (i = 0; i < lists.length; i++) {
        var divCheckbox = document.createElement("div");
        divCheckbox.setAttribute("class", "checkbox");
        var label = document.createElement("label");
        var cb = document.createElement("input");
        cb.setAttribute("type", "checkbox");
        cb.value = lists[i].id;

        label.appendChild(cb)
        label.appendChild(document.createTextNode(lists[i].name));

        divCheckbox.appendChild(label);
        $("#users-list").append(divCheckbox);
    }
}

$(".datepicker").datepicker({
    showOn: "button",
    buttonText: "Select date"
});

function getStats(request) {
    console.log(JSON.stringify(request));
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        'type': 'POST',
        'url': apiUrl + "/get_stats",
        'data': JSON.stringify(request),
        'dataType': 'json',
        'success': function(response) {
            updateTable(response.data);
        }
    })
}

function updateTable(data) {
    // $("#table-header").empty();
    $("#data-table").bootstrapTable('destroy');
    data.sort(function(a, b) {
        return b.totalCount - a.totalCount;
    })
    var columns = [];
    columns.push({
        field: "name",
        title: "Name"
    });

    for (i = 0; i < selectedPlatforms.length; i++) {
        var fieldName = "count." + selectedPlatforms[i];
        columns.push({
            field: fieldName,
            title: selectedPlatforms[i]
        });
    }
    columns.push({
        field: "totalCount",
        title: "Total"
    });
    $("#data-table").bootstrapTable({
        columns: columns,
        data: data
    });
    $("#data-table").show();
    $("#submitButton").button('reset');
}

function buildRequest() {
    var l = $("#datepicker-from").datepicker("getDate");
    var r = $("#datepicker-to").datepicker("getDate");
    var dateFrom = 0;
    var dateTo = Math.floor(+new Date() / 1000);
    if (l != null) {
        dateFrom = Math.floor(l / 1000)
    }
    if (r != null) {
        dateTo = Math.floor(r / 1000)
    }

    var selectedLists = [];
    $('#users-list input:checked').each(function() {
        selectedLists.push($(this).attr('value'));
    });

    var customUsers = [];
    $('#users .custom-user').each(function() {
        var user = {};
        user.name = nameByAttr[$(this).attr("cur-id")];
        user.ids = idsByAttr[$(this).attr("cur-id")];
        customUsers.push(user);
    });
    var requset = {};
    requset.sites = selectedPlatforms;
    requset.userLists = selectedLists;
    requset.customUsers = customUsers;
    requset.startTime = dateFrom;
    requset.endTime = dateTo;
    return requset;
}


getData(addPlatforms, addLists)

$(document).on("change", "#platforms-list input:checkbox", function() {
    if ($(this).is(':checked')) {
        selectedPlatforms.push($(this).attr("value"));
    } else {
        var pos = selectedPlatforms.indexOf($(this).attr("value"));
        selectedPlatforms.splice(pos, 1);
    }
});

$(document).on("click", ".delete", function() {
    $(this).parent().remove();
});

var curId = 0;

function addCustomUser(name, curId) {
    $("#users").append('<div class="custom-user"' + 'cur-id="' + curId +'">' + name + '<button type="button" class="btn btn-default btn-xs delete-custom-user"> <span class="glyphicon glyphicon-star glyphicon-remove" aria-hidden="true"></span>Delete</button></div><br>');
}

$("#add-user-button").on("click", function() {
    var name;
    var ids = {};
    $('.modal-field').each(function() {
        if ($(this).attr("data-id") == "custom-user-name") {
            name = $(this).val();
        } else {
            ids[$(this).attr("data-id")] = $(this).val();
        }

    });
    idsByAttr[curId] = ids;
    nameByAttr[curId] = name;
    addCustomUser(name, curId);
    curId++;
    $('#modal').modal("hide");
});

$(document).on('click', ".delete-custom-user", function() {
    $(this).parent().remove();
});

$("#modal").on("hide.bs.modal", function() {
    $('#modal .modal-body').empty();
});

function createForm(dataId, name) {
    var div = document.createElement("div");
    div.setAttribute("class", "form-group");
    var label = document.createElement("label");
    var cb = document.createElement("input");
    cb.setAttribute("type", "text");
    cb.setAttribute("class", "form-control modal-field");
    cb.setAttribute("data-id", dataId);
    label.appendChild(document.createTextNode(name));
    div.appendChild(label);
    div.appendChild(cb);
    return div;
}


$("#modal").on("show.bs.modal", function(event) {
    $('#modal .modal-body').empty();
    var body = $(this).find(".modal-body");
    if (selectedPlatforms.length == 0) {
        body.text("No selected platforms");
    } else {
        body.append(createForm("custom-user-name", "Name"));
        for (i = 0; i < selectedPlatforms.length; i++) {
            body.append(createForm(selectedPlatforms[i], nameByPlatform[selectedPlatforms[i]]));
        }
    }
});

$('#submitButton').on('click', function() {
    $(this).button('loading');
    var request = buildRequest();
    getStats(request);
})