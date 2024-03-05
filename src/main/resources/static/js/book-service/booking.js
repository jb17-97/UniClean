$(document).ready(function() {

    $("#hour").on("focusout", function() {
        console.log("Input lost focus");

        const cleaningType = $("#cleaningType").val();
        const hour = $("#hour").val();
        console.log('cleaningType : '+cleaningType+', hour : '+hour);

        if(cleaningType && hour && hour > 0){
            populateTotalPriceField(cleaningType, hour);
        }
    });

    const populateTotalPriceField = function(cleaningType, hour) {
        const postData = {
            cleaningType: cleaningType,
            hour : hour
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

