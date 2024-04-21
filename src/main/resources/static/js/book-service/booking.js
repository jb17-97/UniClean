$(document).ready(function() {

    $("#hour").on("focusout", function() {
        console.log("Input lost focus");

        const cleaningType = $("#cleaningType").val();
        const serviceType = $("#serviceType").val();
        const hour = $("#hour").val();
        console.log('cleaningType : '+cleaningType+', hour : '+hour);

        if(cleaningType && serviceType && hour && hour > 0){
            populateTotalPriceField(cleaningType, serviceType, hour);
        }
    });

    const populateTotalPriceField = function(cleaningType, serviceType, hour) {
        const postData = {
            cleaningType: cleaningType,
            hour : hour,
            serviceType : serviceType
        };
        $.ajax({
            type: "POST",
            url: "http://localhost:9000/api/calculate-price",
            data: JSON.stringify(postData),
            contentType: "application/json",
            success: function(response) {
                console.log("Success:", response);
                if(response){
                    if(response && response.totalPrice && response.totalPrice > 0) {
                        $("#totalPrice").val(response.totalPrice);
                    }
                }
            },
            error: function(error) {
                console.error("Error:", error);
            }
        });
    };

    $('.btnBookingDel').click(function() {
        let bookingJson = $(this).attr('data-booking');
        const booking = convertStringToJsObject(bookingJson);
        console.log(booking);

        populateModal(booking);

        const bookingModal = new bootstrap.Modal(document.getElementById("bookingModal"), {'backdrop' :'static'});
        bookingModal.show();
    });

    const convertStringToJsObject = (dataString) => {
        // Step 1: Remove the unnecessary prefixes
        const trimmedData = dataString.replace("BookingDTO(", "").replace(")", "");
        // Step 2: Split the string into individual key-value pairs
        const keyValuePairs = trimmedData.split(", ");
        // Step 3: Create the JavaScript object using these key-value pairs
        const jsObject = {};
        keyValuePairs.forEach(pair => {
            const [key, value] = pair.split("=");
            jsObject[key] = value === "null" ? null : value;
        });
        return jsObject;
    };

    const populateModal = (booking) => {
        let deleteUrl = '/book-service/delete/' + booking.id;
        var $DeleteLink = $('#delBookingBtn');
        $DeleteLink.attr('href', deleteUrl);
    };
});



$('#serviceType').change(function() {
    var selectedValue = $(this).val();

    $("#hour").val('');
    $("#totalPrice").val('');
    $('#booking_options_container').html('');

    if(selectedValue && selectedValue == 'subscription') {
        var divToShow = $('#specific_service_container');
        $("#specificService").val('');
        divToShow.css('display', 'block');
    }
    else {
        var divToHide = $('#specific_service_container');
        divToHide.css('display', 'none');
    }
});

$('#specificService').change(function() {
    $('#booking_options_container').html('');

    const selectedValue = $(this).val();
    console.log('specificService : '+selectedValue)
    const cleaningDate = $("#cleaningDate").val();

    if(selectedValue && cleaningDate) {

       const postData = {
           specificService: selectedValue,
           cleaningDate : cleaningDate
       };

       $.ajax({
           type: "POST",
           url: "http://localhost:9000/api/booking-options",
           data: JSON.stringify(postData),
           contentType: "application/json",
           success: function(response) {
               console.log("Success:", response);
               if(response){
                   if(response && response.bookingOption) {
                       var bookingOptionsArr = response.bookingOption.split("#");

                       if(bookingOptionsArr && bookingOptionsArr.length > 0) {
                           var tableHtml = '<table class="table table-striped"><h4>Next 3 Bookings :</h4>';
                           tableHtml += '<thead><tr><th>SL</th><th>Booking Date</th></tr></thead>';
                           tableHtml += '<tbody>';
                           tableHtml += '<tr><td>' + 1 + '</td><td>' + bookingOptionsArr[0] + '</td></tr>';
                           tableHtml += '<tr><td>' + 2 + '</td><td>' + bookingOptionsArr[1] + '</td></tr>';
                           tableHtml += '<tr><td>' + 3 + '</td><td>' + bookingOptionsArr[2] + '</td></tr>';

                           tableHtml += '</tbody></table>';

                           // Append the table to the div with id 'tableContainer'
                           $('#booking_options_container').html(tableHtml);
                       }
                   }
               }
           },
           error: function(error) {
               console.error("Error:", error);
           }
       });
    };

});

