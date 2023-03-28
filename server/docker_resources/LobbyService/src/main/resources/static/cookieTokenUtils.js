/**
 * Verifies whether the current session cookie contains username, acces-token and renewal-token
 */
function isLoginOk() {
    let username = readCookie('user-name');
    let access_token = readCookie('access-token');
    let refresh_token = readCookie('refresh-token');

    let system_time = new Date().getTime();

    // Reject token if expired (or almost expired)
    if(system_time + 5000 > getAccessTokenExpiryMoment())
    {
        return false;
    }

    // evaluates to true if all strings properly set in cookie
    return username && access_token && refresh_token;
}

/**
 * Redirects all non-authenticated users to the LobbyService login page.
 */
function anonymousIntercept() {
    if (!isLoginOk()) {
        let landingPage = getOauthContext();
        window.location.replace(landingPage);
    }
}

/**
 * Invoke this function on every page load (except login page). If an access token is present, it will be renewed
 * shortly before its expiry, using the corresponding refresh token.
 * Once called, it is not necessary to interrupt this method. A page relocate implicitly kills potentially pending
 * sleeps promises.
 */
function timedTokenRenew() {

    // If no access token defined, print a warning and stop this function.
    if (!getAccessToken()) {
        console.error("Impossible to invoke delayed token renewal when no token is present!");
        return;
    }

    let timeUntilRenew = getRemainingMilliSecondsBeforeAccessTokenExpiry();
    sleep(timeUntilRenew).then(renewTokens);
}

/**
 * Helper function to renew the access token, using the current refresh token.
 */
function renewTokens() {
    console.log("Current access token will expire within the next couple of seconds. Attempting to use refresh token to update.")

    // Get new token pair, using refresh token
    let refreshToken = getRefreshToken();
    // Lobby Service authentication meta-parameters and HTTP method
    // See original login function for body payload details.
    const init = {
        body: "grant_type=refresh_token&refresh_token=" + refreshToken.replace(/\+/g, "%2B"),
        headers: {
            Authorization: "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=",
            "Content-Type": "application/x-www-form-urlencoded"
        },
        method: "POST"
    };

    // Actually retrieves tokens from API. On success stores them in session cookie and redirects to main menu. On
    // failure displays an alert an reloads the page.
    let oauth_context = getOauthContext();
    fetch(oauth_context + "oauth/token", init)
        .then(result => result.json())  // returns a new promise, containing the parsed response (Even in case of bad
        // credentials, a parse-able json is returned. no error handling needed here.)
        .then(json => {
            // If the LS rejected the credentials:
            if (json.error) {
                alert(json.error_description);
                logout();
                location.reload();
            }
            // Else, if the credentials were accepted (token in JSON reply)
            else {
                console.log("Successfully renewed tokens using refresh token.");
                let expiryMoment = computeExpiryMoment(json.expires_in);
                persistLogin(getUserName(), json.access_token, json.refresh_token, expiryMoment, oauth_context);

                // recursively enable following token renewal based on renewed tokens
                timedTokenRenew();
            }
        })
}

function getUserName() {
    return readCookie('user-name');
}

function getAccessToken() {
    return readCookie('access-token');
}

function getRefreshToken() {
    return readCookie('refresh-token');
}

/**
 * Returns the system time at which the access token will expire. (Not to be confused with the remaining time until its
 * expiry)
 */
function getAccessTokenExpiryMoment() {
    return readCookie('access-token-expiry-moment');
}

/**
 * Returns the REST resource location to renew OAuth2 tokens.
 */
function getOauthContext() {
    return readCookie('oauth2-context');
}

/**
 * Returns the current user role, associated to the received token.
 */
function getRole() {
    return readCookie('role');
}


/**
 * Returns (based on the expiry moment) the amount of millisecond remaining until the access token expiry (including a
 * 5 second buffer period).
 */
function getRemainingMilliSecondsBeforeAccessTokenExpiry() {

    return getAccessTokenExpiryMoment() - new Date().getTime() - 5 * 1000;
}

/**
 * Deletes the tokens from the cookie, keeps the username. Then reloads the page (forces redirect to login if protected
 * page.).
 */
function logout() {
    document.cookie = "access-token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/";
    document.cookie = "refresh-token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/";
    document.cookie = "access-token-expiry-moment=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/";
    document.cookie = "role==; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/";
    window.location.replace(getContextPath());
}

// https://stackoverflow.com/questions/10730362/get-cookie-by-name
// Call it with: var value = readCookie('param-name');
function lookUpCookie(name)
{
    let nameEQ = name + "=";
    let ca = document.cookie.split(';');
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) == ' ') c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
    }
    return null;
}

function readCookie(name) {
    let value = lookUpCookie(name);
    if(value === 'undefined')
        return null;
    else
        return value;
}

/**
 * Helper function to get sleep functionality. See: https://stackoverflow.com/a/951057
 */
function sleep(time) {
    return new Promise((resolve) => setTimeout(resolve, time));
}

/**
 * Saves the access and refresh token in a session cookie.
 */
function persistLogin(username, access_token, refresh_token, access_token_expiry_moment, authority_context) {

    // escape occurrences of '+' in tokens by '%2B', so they can be safely used as URL params from here on.
    access_token = access_token.replace(/\+/g, "%2B");
    refresh_token = refresh_token.replace(/\+/g, "%2B");

    // log and persist credential data
    console.log("Username: " + username);
    console.log("Access Token: " + access_token);
    console.log("Refresh Token: " + refresh_token);
    console.log("Access Token Expiry Moment: " + access_token_expiry_moment);
    console.log("OAuth2 Service Entity Context: "+authority_context);
    document.cookie = "user-name=" + username + ";path=/";
    document.cookie = "access-token=" + access_token + ";path=/";
    document.cookie = "refresh-token=" + refresh_token + ";path=/";
    document.cookie = "access-token-expiry-moment=" + access_token_expiry_moment + ";path=/";
    document.cookie = "oauth2-context="+authority_context+";path=/";
}

function computeExpiryMoment(remainingSeconds) {
    // Add remaining millisecond to current moment in milliseconds since 1970/01/01.
    let expiry = new Date().getTime() + remainingSeconds*1000;
    return expiry;
}