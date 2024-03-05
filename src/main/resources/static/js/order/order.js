$(document).ready(function() {

    $(document).find('.accept').on('click', function (e){
            e.preventDefault();
            const actionStatus = $('<input type="hidden" name="actionStatus" value="Accepted By Cleaner">');
            $('#bookingForm').append(actionStatus);
            $('#bookingForm').submit();
    });

    $(document).find('.reject').on('click', function (e){
            e.preventDefault();
            const actionStatus = $('<input type="hidden" name="actionStatus" value="Rejected By Cleaner">');
            $('#bookingForm').append(actionStatus);
            $('#bookingForm').submit();
            $('#bookingForm').submit();
    });

});
