/**
 * Helper functions for anything login / token related.
 */

/**
 * Prefills the username field and preselects password field in case the information is present in a cookie
 */
function preFillUserNameField() {
    let username = readCookie('user-name');
    if (username) {
        $('#user-name-field').val(username);
        $('#password-field').focus();
    }
}

/**
 * Assigns the credential based token retrieval function a click on login / enter press.
 */
function registerLoginHandler() {

    // register enter press on password field
    $('#password-field').keypress(function (event) {
        let keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode == '13') {
            login();
        }
    });

    // register click on login button
    $('#loginButton').on('click', login);
}

/**
 * Retrieves an OAuth2 token pair using the provided credentials. On success the token is stored in a cookie and the
 * user is redirected to the main menu.
 */
function login() {

    // read out login data from input fields
    let username = document.getElementById('user-name-field').value.toLowerCase();
    let password = document.getElementById('password-field').value;

    // disable button until login attempt is finished
    $('#loginButton').prop("disabled", "true");

    // Lobby Service authentication meta-parameters and HTTP method
    const init = {
        body: "grant_type=password&username=" + username + "&password=" + password.replace(/\+/g, "%2B"),
        // Note: plus escaping required here since body
        // follows URL param syntax and is parsed like an URL string on server side (see header parameter "urlencoded").
        headers: {
            Authorization: "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=", // echo -n "bgp-client-name:bgp-client-pw"
                                                                             // | base64
            "Content-Type": "application/x-www-form-urlencoded"
        },
        method: "POST"
    };

    // Actually retrieves tokens from API. On success stores them in session cookie and redirects to main menu. On
    // failure displays an alert an reloads the page.
    fetch(getContextPath() + '/oauth/token', init)
        .then(result => result.json())  // returns a new promise, containing the parsed response (Even in case of bad
        // credentials, a parse-able json is returned. no error handling needed here.)
        .then(json => {
            // If the LS rejected the credentials:
            if (json.error) {
                alert(json.error_description);
                location.reload();
            }
            // Else, if the credentials were accepted (token in JSON reply)
            else {
                // Compute exact moment (absolute) when token will expire
                let expiryMoment = computeExpiryMoment(json.expires_in);

                // save location used for login (current page) so this oauth2 endpoint can be contacted, even in case
                // of a forward to other domains / ports.
                let authority_context = window.location.href;

                persistLogin(username, json.access_token, json.refresh_token, expiryMoment, authority_context);
                forwardToLanding();
            }
        })
    // Apparently not possible to force DOM update from promise finally. Therefore redirect to same page and fore
    // reload if bad credentials.
}


/**
 * Redirects an authenticated user to the principal menu.
 * For admins, this is eht user management panel
 * For players this is the lobby
 */
function forwardToLanding() {
    // Determine whether logged in user is admin or player
    fetch(getContextPath() + '/oauth/role?access_token=' + getAccessToken())
        .then(result => result.json())
        .then(json => {
            // Save role in session cookie (UI navbar changes depending on role)
            document.cookie = "role="+json[0].authority+";path=/";

            // Redirect players to session panel, admins to user management panel
            if (json[0].authority === 'ROLE_SERVICE')
                window.location.href = getContextPath() + "/service.html";
            else
                window.location.href = getContextPath() + "/lobby.html";
        })
}



