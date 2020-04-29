Toast = {
    success: function(message){
        Swal.fire({
            position: 'top-end',
            icon: 'success',
            title: message,
            showConfirmButton: false,
            timer: 3000
        })
    },
    error: function () {
        Swal.fire({
            position: 'top-end',
            icon: 'error',
            title: message,
            showConfirmButton: false,
            timer: 3000
        })
    },

    warning: function () {
        Swal.fire({
            position: 'top-end',
            icon: 'warning',
            title: message,
            showConfirmButton: false,
            timer: 3000
        })
    }
};