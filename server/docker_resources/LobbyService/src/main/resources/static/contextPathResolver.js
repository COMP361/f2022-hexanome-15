/**
 * Helper function to dynamically resolve the context path the application was deployed at. This is only relevant for
 * war deployment as the spring boot configuration uses the default "root" context path.
 * @returns {string} the context path this application is deployed at.
 */
function getContextPath() {
    let contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
    console.log("Resolved deployment context path to: \""+contextPath+"\"");
    return contextPath;
}