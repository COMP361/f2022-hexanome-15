function registerButtonHandlers() {

    $('#remove-account').on('click', deleteSelfUser);
    $('#colour-update').on('click', updateSelfUserColour);
    $('#password-update').on('click', updateSelfUserPassword);

    // register same callback for enter on password field
    $('#password-field').keypress(function (event) {
        let keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode == '13') {
            updateSelfUserPassword();
        }
    });
}

function fillPreferenceTable(userdata) {

    // display name
    $('#name-td').text(capitalizeFirstLetter(getUserName()));

    // display colour
    $('#colour-field').attr('value', '#' + userdata.preferredColour);

    // display role
    let role = userdata.role;
    $('#role-td').text(capitalizeFirstLetter(role.split('_')[1]));

}

function getUserDataAndFillPreferenceTable() {

    // get json bundle with all user data as JSON from api backend
    fetch(getContextPath() + '/api/users/' + getUserName() + '?access_token=' + getAccessToken())
        .then(result => result.json())
        .then(json => {
            if (json.error)
                throw Error(json.error);
            else {
                console.log("Retrieved account details are:" + json);
                fillPreferenceTable(json)
            }
        })
        .catch(error => { // logout, redirect to login in case the credentials were rejected.
            console.log(error);
            logout();
        });
}

/**
 * Sends an API request to update the colour of an existing user. Only for use by the user owning the account to be
 * modified.
 */
function updateSelfUserColour() {

    //extract color from users colour picker
    let nextColour = document.getElementById('colour-field').value.slice(1).toUpperCase();
    console.log('Changing preferred colour of ' + getUserName() + ' to: ' + nextColour);

    // build a payload object that complies to the format expected by the backend:
    let postdata = {"colour": nextColour};

    // actually send request to API
    fetch(getContextPath() + '/api/users/' + getUserName() + '/colour?access_token=' + getAccessToken(), {
        headers: {
            'Content-Type': 'application/json'
        },
        method: 'post',
        body: JSON.stringify(postdata)
    })
        .then(reply => {
            if (reply.ok)

                // update page content
                location.reload();
            else {
                reply.text()
                    .then(text => alert(text));
            }
        })
}

/**
 * Sends an API request to update the password of an existing user. Only for use by the user owning the account to be
 * modified.
 */
function updateSelfUserPassword() {

    //extract next password from input field
    let passwordField = document.getElementById('password-field');
    let nextPassword = passwordField.value;
    console.log('Changing password of ' + getUserName() + ' to: ----------');

    // build a payload object that complies to the format expected by the backend:
    // Note: old password is not needed, for an admin token is used to authenticate the request.
    let postdata = {"nextPassword": nextPassword, "oldPassword": "---"};

    // actually send request to API
    fetch(getContextPath() + '/api/users/' + getUserName() + '/password?access_token=' + getAccessToken(), {
        headers: {
            'Content-Type': 'application/json'
        },
        method: 'post',
        body: JSON.stringify(postdata)
    })
        .then(reply => {
            if (reply.ok)

                // update list of displayed users
                location.reload();
            else {
                passwordField.value='';
                reply.text()
                    .then(text => alert(text));
            }
        })
}

/**
 * Sends a request to api to delete an existing user by name. Only for use by the user owning the account to be
 * modified.
 * @param user
 */
function deleteSelfUser() {
    console.log("Deleting user: " + getUserName());

    fetch(getContextPath() + '/api/users/' + getUserName() + '?access_token=' + getAccessToken(), {
        method: 'delete',
    })
        .then(reply => {
            if (reply.ok) {
                logout();
            } else {
                reply.text()
                    .then(text => alert(text));
            }
        })
}