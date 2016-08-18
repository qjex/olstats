var apiUrl = "http://127.0.0.1:8081/api"

var platforms;
var lists;
var selectedPlatforms = []

$( ".datepicker" ).datepicker({
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
    var l = $("#datepicker-from" ).datepicker( "getDate" );
    var r = $("#datepicker-to" ).datepicker( "getDate" );
    var dateFrom = 0;
    var dateTo = Math.floor(+new Date() / 1000);
    if (l != null) {
        dateFrom =  Math.floor(l / 1000)
    }
    if (r != null) {
        dateTo =  Math.floor(r / 1000)
    }

    selectedPlatforms = [];
    $('#platforms-list input:checked').each(function() {
        selectedPlatforms.push($(this).attr('value'));
    });

    var selectedLists = [];
    $('#users-list input:checked').each(function() {
        selectedLists.push($(this).attr('value'));
    });

    var requset = {};
    requset.sites = selectedPlatforms;
    requset.userLists = selectedLists;
    requset.customUsers = [];
    requset.startTime = dateFrom;
    requset.endTime = dateTo;
    return requset;
}

$.getJSON(apiUrl + "/get_platforms", function(result) {
    platforms = result.data;
    $('#loading-platforms').hide();
    for (i = 0; i < platforms.length; i++) {
        var divCheckbox = document.createElement("div");
        divCheckbox.setAttribute("class", "checkbox");
        var label = document.createElement("label");
        var cb = document.createElement("input");
        cb.setAttribute("type", "checkbox");
        cb.value = platforms[i].name;

        label.appendChild(cb);
        label.appendChild(document.createTextNode(platforms[i].siteName));

        divCheckbox.appendChild(label);
        $("#platforms-list").append(divCheckbox);
    }
})

$.getJSON(apiUrl + "/get_lists", function(result) {
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
})

$('#submitButton').on('click', function () {
    $(this).button('loading');
    var request = buildRequest();
    getStats(request);
})
