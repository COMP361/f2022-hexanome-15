let allSessions;

function prepareLobbyPage() {

    // get list of games that are available and populate form drop down menu
    populateSessionOptions();

    // ARL-long poll on "available-sessions"-resource and update displayed table if something changed
    observeResource(getContextPath() + '/api/sessions?hash=', onSessionsChanged, markOffline, "");
}

/**
 * Retrieves list of start-able game kinds from server and populates drop down menu of "start new session" form.
 */
function populateSessionOptions() {

    // retrieve list of all options from server
    fetch(getContextPath() + "/api/gameservices")
        .then(result => result.json())
        .then(options => {    // add each of retrieved "options" to form element with id "boardgameoptions"

            // Add entries to drop down menu
            $.each(options, function (i, item) {
                $('<option value="' + item.name + '">' + item.displayName + '</option>').appendTo('#boardgameoptions');
            })

            // Enable "create" button, if options carries at least one entry
            if (options.length > 0) {
                let startButton = $('#start-session-button');
                startButton.removeClass('disabled');
                startButton.on('click', startSession);
            }
        });
}

/**
 * Called whenever the sessions resource observer registers an update
 * @param sessions as the new sessions bundle. The actual sessions are in a field called sessions.
 */
function onSessionsChanged(sessions) {

    allSessions = sessions.sessions;

    // Build the HTML table, based on the received session information.
    updateSessionsTable(sessions.sessions);

    // Add listeners for the generated action buttons in the generated HTML table.
    associateJoinButtons();
    associateLeaveButtons();
    associateDeleteButtons();
    associateLaunchButtons();
    associatePlayButtons();
}

/**
 * Called by the Async Get, whenever the list of available sessions changes on server side.
 * Iterates over the received collection and rebuilds the DOM, specifically the #sessiontable table.
 * @param sessions
 */
function updateSessionsTable(sessions) {

    // reset sessions table content and re-build table's static columns (first row, bold)
    $('#sessiontable').empty();
    $('<tr>' +
        '<th id="game">Game</th>' +
        '<th id="creator">Creator</th>' +
        '<th id="fill">Players</th>' +
        '<th id="action"> </th>' +
        '</tr>'
    ).appendTo('#sessiontable');

    let userActiveInASession = isInAnySession(getUserName(), sessions);

    // iterate over players, print all info per player, and a remove button
    $.each(sessions, function (key, session) {
        console.log(key + " - " + session);
        $('<tr>' +
            '<td>' + session.gameParameters.displayName + '</td>' +
            '<td>' + capitalizeFirstLetter(session.creator) + '</td>' +
            '<td>' + getPlayersIndicatorString(session) + '</td>' +
            '<td>        ' +
            '<div id="actions-' + key + '" class="input-group-append float-right">\n' +
            buildActionButtons(key, getUserName(), session, userActiveInASession) +
            '</div>' +
            '</td>' +
            '</tr>').appendTo('#sessiontable');

        // also register callback for the newly created "remove" button.
        // $('#join-' + key).on('click', {id: key}, joinSession);
        // });
    })
}

/**
 * Prepares HTML code for the buttons section of a session row.
 * If the player is active in at least on session (creator or player), all join buttons are deactivated. If the session
 * matches one where the player is creator, "join is replaced by "Delete" and "Launch", where the latter is only active
 * if the required amount of players is present. In case the session is already running leave / launch are replaced by
 * a "Play" button that redirects to the game's landing page. If the user is just a player participant of the session,
 * "join" is replaced by "Leave".
 *
 * @param sessionkey as id of the session for which the buttons are generated
 * @param player as the name of the currently logged in player
 * @param sessions as the map with all sessions by their ids
 * @param active as flag to indicate whether the player is already involved in at least one session.
 * @returns {string} HTML code for the required buttons.
 */
function buildActionButtons(sessionkey, player, session, active) {

    // If the session is already running, only "watch" and "play" are possible - depending on the player's affiliation
    if (isRunningSession(session)) {
        if (isInThisSession(player, session))
            return '<button class="btn btn-outline-success" type="button" id="play-' + sessionkey + '">Play</button>\n';
        else
            return '<button class="btn btn-outline-success" type="button" id="play-' + sessionkey + '">Watch</button>\n';
    }

    // If the session is not yet launched it depends on "already in session, already creator, already full"
    // If not yet involved
    if (!isInThisSession(player, session)) {
        // Join is only possible if the session is not yet full
        if (isFull(session))
            return '<button class="btn btn-outline-primary disabled" type="button" id="join-' + sessionkey + '">Join</button>\n';
        else
            return '<button class="btn btn-outline-primary" type="button" id="join-' + sessionkey + '">Join</button>\n';
    }

    // If already in session it depends. Players can only leave the game. Creators can delete (and if ready) launch the
    // session
    if (isNonCreatorPlayer(player, session))
        return '<button class="btn btn-outline-warning" type="button" id="leave-' + sessionkey + '">Leave</button>\n';

    // Game can only be launched if minimum amount players present
    else {
        return '<button class="btn btn-outline-danger" style="margin-right: 5px" type="button" id="delete-' + sessionkey + '">Delete</button>\n' +
            '<button class="btn btn-outline-primary ' + buildDisabledLaunchTag(session) + '" type="button" id="launch-' + sessionkey + '">Launch</button>';
    }
}

function isInThisSession(player, session) {
    return session.players.includes(player);
}

function isFull(session) {
    return session.players.length == session.gameParameters.maxSessionPlayers;
}

/**
 * Helper function to determine whether a provided session is already running.
 * @param session
 */
function isRunningSession(session) {
    return session.launched === true;
}

/**
 * Helper function that tells whether a player is a non-creator player of a session.
 */
function isNonCreatorPlayer(player, session) {
    let isCreator = player === session.creator;
    let isPlayer = session.players.includes(player);

    return isPlayer && !isCreator;
}

/**
 * Helper function that find out whether a provided session has the right amount of player to be launched. If so, the
 * empty string is returned. Otherwise 'disabled' is returned.
 */
function buildDisabledLaunchTag(session) {

    let currentAmount = session.players.length;
    if (session.gameParameters.minSessionPlayers > currentAmount)
        return 'disabled';
    return '';
}

/**
 * Builds an indicator string that tells how many players are allows in a session.
 * If the max and min value are identical, the string displays just a value, not an interval.
 * @param session
 */
function getPlayerIntervalString(session) {
    let min = session.gameParameters.minSessionPlayers;
    let max = session.gameParameters.maxSessionPlayers;

    if (min === max)
        return min;
    else
        return min + '-' + max;
}

/**
 * Builds an indicator string that tells amount and set of players registered to a session.
 */
function getPlayersIndicatorString(session) {

    let players = session.players;
    let playerString = '[' + players.length + '/' + getPlayerIntervalString(session) + ']: ';

    $.each(players, function (index, player) {
        if (index == 0)
            playerString = playerString + capitalizeFirstLetter(player);
        else
            playerString = playerString + ', ' + capitalizeFirstLetter(player);

    });
    return playerString;
}

/**
 * triggered by "start"-button. Looks up which game type has been selected in '#boardgameoptions' field, builds a
 * session bean and sends it to the rest endpoint.
 */
function startSession() {

    let selectedGame = document.getElementById('boardgameoptions');

    let createSessionForm = {
        "creator": getUserName(),
        "game": selectedGame.value,
        "savegame": ""
    }

    fetch(getContextPath() + '/api/sessions?access_token=' + getAccessToken(), {
        headers: {
            'Content-Type': 'application/json'
        },
        method: 'post',
        body: JSON.stringify(createSessionForm)
    }).then(reply => {
        if (reply.status == 401)
            logout(); // ToDo: First try to renew token, call logout() if that failed.
        if (reply.status != 200)
            console.log("Failed to start session. Server replied: " + reply.status);
    });
}

/**
 * Analyzes a received sessions object and tells whether the provided player is associated as creator-or-player in at
 * least one session.
 * @param playername
 * @param sessions
 */
function isInAnySession(playername, sessions) {
    let inSession = false;

    // mark inSession true if player appears in at least one session
    $.each(sessions, function (sessionid, session) {
        inSession = inSession || session.players.includes(getUserName());
    });
    return inSession;
}

/**
 * Assigns join-user-to-selected-session API call to all enabled "join" buttons.
 */
function associateJoinButtons() {
    let joinButtons = $('[id^=join-]');
    $.each(joinButtons, function (index, joinButton) {
        let sessionId = joinButton.id.substring(5);
        if (!$(joinButton).hasClass('disabled'))
            $(joinButton).on('click', function (event) {
                joinSession(sessionId);
            });
    });
}

/**
 * Assigns delete-selected-session API call to all enabled "delete" buttons.
 */
function associateDeleteButtons() {
    let deleteButtons = $('[id^=delete-]');
    $.each(deleteButtons, function (index, deleteButton) {
        let sessionId = deleteButton.id.substring(7);
        $(deleteButton).on('click', function (event) {
            deleteSession(sessionId);
        });
    });
}

/**
 * Helper function to request adding a user to a resource.
 * @param sessionid
 */
function joinSession(sessionid) {

    fetch(getContextPath() + '/api/sessions/' + sessionid + '/players/' + getUserName() + '?access_token=' + getAccessToken(), {
        method: 'put',
    })
        .then(result => {
            if (result.status == 401)
                throw Error('Bad credentials');
        })
        .catch(error => logout());
}

function deleteSession(sessionid) {
    fetch(getContextPath() + '/api/sessions/' + sessionid + '?access_token=' + getAccessToken(), {
        method: 'delete',
    })
        .then(result => {
            if (result.status == 401)
                throw Error('Bad credentials');
        })
        .catch(error => logout());
}

/**
 * Assigns remove-user-from-selected-session API call to all "leave" buttons.
 */
function associateLeaveButtons() {
    let leaveButtons = $('[id^=leave-]');
    $.each(leaveButtons, function (index, leaveButton) {
        let sessionId = leaveButton.id.substring(6);
        $(leaveButton).on('click', function (event) {
            leaveSession(sessionId);
        });
    });
}

/**
 * Sents API request to remove a player from a session.
 * @param sessionid as the id of the session in question
 */
function leaveSession(sessionid) {
    fetch(getContextPath() + '/api/sessions/' + sessionid + '/players/' + getUserName() + '?access_token=' + getAccessToken(), {
        method: 'delete',
    })
        .then(result => {
            if (result.status == 401)
                throw Error('Bad credentials');
        })
        .catch(error => logout());
}

/**
 * Assigns join-user-to-selected-session API call to all enabled "launch" buttons.
 */
function associateLaunchButtons() {
    let launchButtons = $('[id^=launch-]');
    $.each(launchButtons, function (index, launchButton) {
        let sessionId = launchButton.id.substring(7);
        if (!$(launchButton).hasClass('disabled'))
            $(launchButton).on('click', function (event) {

                // Mark as disabled and remove click listener, so an impatient user can not launch the game twice.
                $(launchButton).addClass("disabled");
                $(launchButton).off();
                $(launchButton).text("Launching...")
                launchSession(sessionId);
            });
    });
}

/**
 * Sends API request to mark a session as launched
 */
function launchSession(sessionid) {

    // Sending this query to the LS will implicitly cause a session creation on game-service side issued by the LS.
    fetch(getContextPath() + '/api/sessions/' + sessionid + '?access_token=' + getAccessToken(), {
        method: 'post',
    })
        .then(result => {
            if (result.status == 401)
                throw Error('Bad credentials!');
            if (result.status == 400)
                throw Error('Bad request! UI out of sync?');
            //forwardToSessionLanding(sessionid);
        })
        .catch(error => {
            if (error.message.includes('credentials'))
                logout();
            else
                alert('Already running. UI out of sync?');
        });
}

/**
 * Redirects to the external game service (location provided upon registration).
 * @param session
 */
function forwardToSessionLanding(sessionId) {

    // associate corresponding redirect functions to all play / observe buttons.
    $.each(allSessions, function (key, session) {
        if (key === sessionId) {

            // Note: the location provided within the gameParameters property CAN be a docker-intern locator (e.g.
            // "lobby", "xox") rather than a location resolvable by the browser. Currently the BGP assumes all services
            // run on the same location, therefore a partial address preservation (as implemented below) sidesteps this
            // problem. On the long run the registration of services with an enabled "web" flag, contain an additional
            // landing page locator (resolvable by a browser, outside the virtual docker network)

            // step 0: preserve protocol
            let protocol = window.location.href.split(':')[0] + ":";

            // step 1: preserve current server location as prefix (location+':') (without the http://|https:// prefix)
            // Note: protocol information (http/https) is not provided - auto resolved by browser.
            // Algo: if there are two ':' in string (port provided) then split at second ':'. otherwise split at first
            // occurence of '/' after the ':'. (is equal to third occurence of '/')
            let serverLocation = window.location.href.split(':')[1] + ":";
            if (((window.location.href.match(/:/g) || []).length) === 1)
                // if the original URL did not contain a port. shorten until the third '/'
                serverLocation = "//" + serverLocation.split('/')[2] + ":";

            // step 2: build the relative location (will be used as suffix) based on the gameservice registry
            // information. (port + landing resource)
            let serviceInternalLocation = session.gameParameters.location + '/webui/games/' + sessionId; // Note: no
                                                                                                         // token in
                                                                                                         // URL
                                                                                                         // required -
                                                                                                         // is stored
                                                                                                         // within
                                                                                                         // cookie
            let serviceRelativeLocation = serviceInternalLocation.split(':')[2];

            // Construct the resulting landing url, that is resolvable by browser.
            let landinglocation = protocol + serverLocation + serviceRelativeLocation;

            console.log('Forwarding to external game session: ' + landinglocation);
            window.location.href = landinglocation;
        }
    });
}

/**
 * Assigns custom redirect page function to all "Play!" buttons.
 */
function associatePlayButtons(sessionId) {
    let playButtons = $('[id^=play-]');
    $.each(playButtons, function (index, playButton) {
        let sessionId = playButton.id.substring(5);
        $(playButton).on('click', function (event) {
            forwardToSessionLanding(sessionId);
        });
    });
}
