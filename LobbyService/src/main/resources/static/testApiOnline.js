/**
 * Helper functions to enable a warning block in case any communication with the server failed.
 */

function testApiOnline() {
    fetch(getContextPath() + '/api/online')
        .then(response => {
            if(response.status != 200)
                markOffline();
        })
        .catch(() => markOffline());    // for some reason catch clause is triggered.
}

function markOffline() {
    console.log("removing class");
    $('#notonline').removeClass('d-none');
}