/**
 * Generic functionality for all top navigation bars in this file.
 */

/**
 * Sets the name in the navbar and registers tha handler for the logout button.
 */
function setupNavbar(current_page) {

    // Places all default tabs in default order in navbar. The currently displayed page and restricted tabs are removed
    // afterwards.
    fillDefaultTabs();
    $('#username-display').text(capitalizeFirstLetter(getUserName()));

    // Remove inactive elements (self tab, restricted tabs)
    removeObsoleteTabs(current_page);

    // Associate click functions with elem
    registerTabListeners();
}

// https://stackoverflow.com/questions/1026069/how-do-i-make-the-first-letter-of-a-string-uppercase-in-javascript
function capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1).toLowerCase();
}

/**
 * Helper function to remove unrequired tabs from the default nav bar.
 * @param current_page
 */
function removeObsoleteTabs(current_page) {
    // Admin Zone button is only available to admins
    if (getRole() != 'ROLE_ADMIN') {
        // remove the button entirely
        $('#admin-button').remove();
    }

    // Remove the tab for page currently displayed (received as function parameter)
    if (current_page.toLowerCase().includes('admin')) {
        // remove the button entirely
        $('#admin-button').remove();
    }
    else if (current_page.toLowerCase().includes('lobby')) {
        // remove the button entirely
        $('#lobby-button').remove();
    }
    else if (current_page.toLowerCase().includes('settings')) {
        // remove the button entirely
        $('#settings-button').remove();
    }
}

/**
 * Helper function to fill empty navbar placeholder with all potentially required tab elements.
 * Admin Zone, Lobby, Settings, Logout
 */
function fillDefaultTabs() {
    $('<li class="nav-item">' +
        '<button id="admin-button" type="button" class="btn btn-outline-danger  mr-1">' +
        'Admin Zone ' +
        '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"' +
        '    class="bi bi-sliders"' +
        '   viewBox="0 0 16 16">' +
        ' <path d="M7.938 2.016A.13.13 0 0 1 8.002 2a.13.13 0 0 1 .063.016.146.146 0 0 1 .054.057l6.857 11.667c.036.06.035.124.002.183a.163.163 0 0 1-.054.06.116.116 0 0 1-.066.017H1.146a.115.115 0 0 1-.066-.017.163.163 0 0 1-.054-.06.176.176 0 0 1 .002-.183L7.884 2.073a.147.147 0 0 1 .054-.057zm1.044-.45a1.13 1.13 0 0 0-1.96 0L.165 13.233c-.457.778.091 1.767.98 1.767h13.713c.889 0 1.438-.99.98-1.767L8.982 1.566z"/>'+
        ' <path d="M7.002 12a1 1 0 1 1 2 0 1 1 0 0 1-2 0zM7.1 5.995a.905.905 0 1 1 1.8 0l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 5.995z"/>'+
        '</svg>' +
        '</button>' +
        '</li>' +
        '<li class="nav-item">' +
        '<button id="lobby-button" type="button" class="btn btn-outline-primary  mr-1">' +
        'Lobby ' +
        '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-controller" viewBox="0 0 16 16">'+
        '<path d="M11.5 6.027a.5.5 0 1 1-1 0 .5.5 0 0 1 1 0zm-1.5 1.5a.5.5 0 1 0 0-1 .5.5 0 0 0 0 1zm2.5-.5a.5.5 0 1 1-1 0 .5.5 0 0 1 1 0zm-1.5 1.5a.5.5 0 1 0 0-1 .5.5 0 0 0 0 1zm-6.5-3h1v1h1v1h-1v1h-1v-1h-1v-1h1v-1z"/>'+
        '<path d="M3.051 3.26a.5.5 0 0 1 .354-.613l1.932-.518a.5.5 0 0 1 .62.39c.655-.079 1.35-.117 2.043-.117.72 0 1.443.041 2.12.126a.5.5 0 0 1 .622-.399l1.932.518a.5.5 0 0 1 .306.729c.14.09.266.19.373.297.408.408.78 1.05 1.095 1.772.32.733.599 1.591.805 2.466.206.875.34 1.78.364 2.606.024.816-.059 1.602-.328 2.21a1.42 1.42 0 0 1-1.445.83c-.636-.067-1.115-.394-1.513-.773-.245-.232-.496-.526-.739-.808-.126-.148-.25-.292-.368-.423-.728-.804-1.597-1.527-3.224-1.527-1.627 0-2.496.723-3.224 1.527-.119.131-.242.275-.368.423-.243.282-.494.575-.739.808-.398.38-.877.706-1.513.773a1.42 1.42 0 0 1-1.445-.83c-.27-.608-.352-1.395-.329-2.21.024-.826.16-1.73.365-2.606.206-.875.486-1.733.805-2.466.315-.722.687-1.364 1.094-1.772a2.34 2.34 0 0 1 .433-.335.504.504 0 0 1-.028-.079zm2.036.412c-.877.185-1.469.443-1.733.708-.276.276-.587.783-.885 1.465a13.748 13.748 0 0 0-.748 2.295 12.351 12.351 0 0 0-.339 2.406c-.022.755.062 1.368.243 1.776a.42.42 0 0 0 .426.24c.327-.034.61-.199.929-.502.212-.202.4-.423.615-.674.133-.156.276-.323.44-.504C4.861 9.969 5.978 9.027 8 9.027s3.139.942 3.965 1.855c.164.181.307.348.44.504.214.251.403.472.615.674.318.303.601.468.929.503a.42.42 0 0 0 .426-.241c.18-.408.265-1.02.243-1.776a12.354 12.354 0 0 0-.339-2.406 13.753 13.753 0 0 0-.748-2.295c-.298-.682-.61-1.19-.885-1.465-.264-.265-.856-.523-1.733-.708-.85-.179-1.877-.27-2.913-.27-1.036 0-2.063.091-2.913.27z"/>'+
        '</svg>' +
        '</button>' +
        '</li>' +
        '<li class="nav-item">' +
        '<button id="settings-button" type="button" class="btn btn-outline-primary  mr-1">' +
        'Settings ' +
        '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"' +
        '    class="bi bi-sliders"' +
        '   viewBox="0 0 16 16">' +
        ' <path fill-rule="evenodd"' +
        '       d="M11.5 2a1.5 1.5 0 1 0 0 3 1.5 1.5 0 0 0 0-3zM9.05 3a2.5 2.5 0 0 1 4.9 0H16v1h-2.05a2.5 2.5 0 0 1-4.9 0H0V3h9.05zM4.5 7a1.5 1.5 0 1 0 0 3 1.5 1.5 0 0 0 0-3zM2.05 8a2.5 2.5 0 0 1 4.9 0H16v1H6.95a2.5 2.5 0 0 1-4.9 0H0V8h2.05zm9.45 4a1.5 1.5 0 1 0 0 3 1.5 1.5 0 0 0 0-3zm-2.45 1a2.5 2.5 0 0 1 4.9 0H16v1h-2.05a2.5 2.5 0 0 1-4.9 0H0v-1h9.05z"></path>' +
        '</svg>' +
        '</button>' +
        '</li>' +
        '<li class="nav-item">' +
        '   <button id="logout-button" type="button" class="btn btn-outline-primary">' +
        '      Log' +
        '     out <a id="username-display"></a>' +
        '    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"' +
        '        class="bi bi-door-closed" viewBox="0 0 16 16">' +
        '      <path fill-rule="evenodd"' +
        '           d="M3 2a1 1 0 0 1 1-1h8a1 1 0 0 1 1 1v13h1.5a.5.5 0 0 1 0 1h-13a.5.5 0 0 1 0-1H3V2zm1 13h8V2H4v13z"></path>' +
        '    <path d="M9 9a1 1 0 1 0 2 0 1 1 0 0 0-2 0z"></path>' +
        '</svg>' +
        '</button>' +
        '</li>').appendTo($('#nav-ul'));
}

/**
 * Helper function to associate the right page redirect to every navbar button.
 */
function registerTabListeners()
{
    $('#logout-button').on('click', logout);

    $('#settings-button').on('click', function () {
        window.location.href = getContextPath() + "/settings.html";
    })

    $('#lobby-button').on('click', function () {
        window.location.href = getContextPath() + "/lobby.html";
    })

    $('#admin-button').on('click', function () {
        window.location.href = getContextPath() + "/admin.html";
    });
}