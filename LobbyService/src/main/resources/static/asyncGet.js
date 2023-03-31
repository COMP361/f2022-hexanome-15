/**
 * Functionality related to repeated long polling on a resource.
 * As an ARL user you would typically call this function, to subscribe for updates on a given resource.
 *
 * How it works: Once launched recursive polls for updates, until the server relied with a 200 or non-408 return code.
 * That is to say:
 * If 200 was returned, the success function is called. Then new long polling is automatically started, based on the hash of the received content.
 * If 408 was returned, a subsequent long poll on the same resource is immediately fired.
 * If anything else was returned, the error function is called.
 * Credit for a good error-handling basis goes to:
 * -> https://css-tricks.com/using-fetch/
 */
function observeResource(serverurl, successFunction, errorFunction, currentContent) {
    console.log("Polling: " + serverurl + md5(currentContent, false, false));
    fetch(serverurl + md5(currentContent, false, false))
        .then(reply => {
            verifyReturnCode(reply, 200);
            verifyJsonContentType(reply);

            // This happens only in case the previous line passed (so we know the server replied with 200)
            return extractJsonObject(reply);
        })
        .then(payloadAsObject => {

            // In reaction to the previous return (which is a promise), we call the success function with the extracted data.
            currentContent = JSON.stringify(payloadAsObject);
            successFunction(payloadAsObject);
            // Next line leads to infinite loop. Server replies although hashes should mismatch.
            observeResource(serverurl, successFunction, errorFunction, currentContent);
        })
        .catch(error => {
            handleNon200(error, serverurl, successFunction, errorFunction, currentContent)
        })
}

/**
 * Error handling for non200 return codes. In case of a timeout, keep polling. In case of any other return code, stop.
 */
function handleNon200(error, serverurl, successFunction, errorFunction, currentContent) {

    console.log("Server replied with a non 200... -> " + error);

    // In case of a 408 (timeout) continue polling / wait for next update
    if (error.response.received === 408) {  // ToDo: fix this, if server was brutally killed.
        console.log("Detected a 408");
        observeResource(serverurl, successFunction, errorFunction, currentContent);
    } else {
        console.log("Detected a truly unexpected return code");
        // In any other case, stop. Something went wrong.
        errorFunction(errorMessage);
    }
}

/**
 * Default error handling function, that simply logs if things go wrong and returns an empty (but valid) json object.
 * @param error
 * @returns {string}
 */
function onUpdateError(error) {
    console.log('Unable to retrieve resource form API: ' + error)
    return "{}";
}

/**
 * Analyzes an HTTP reply and only returns it back, it the header declares the return code to equal the expected type. (Typically you'd check for a 200.)
 * @param reply
 * @returns {*}
 */
function verifyReturnCode(reply, expectedValue) {
    if (reply.status == expectedValue) {
        return reply;
    } else {
        // Embed expected and received codes in error object and fire it.
        let error = new Error("Server replied with unexpected return code.");
        error.response = {"expected":expectedValue, "received":reply.status};
        throw error;
    }
}
/**
 * Analyzes a HTTP reply and only returns it back, if the header declares the content to by JSON formatted.
 * @param reply
 * @returns {*}
 */
function verifyJsonContentType(reply) {
    let contentType = reply.headers.get('content-type')
    if (contentType.includes('application/json')) {
        return reply;
    } else {
        alert("Wrong content type!");
        throw new Error(`Sorry, content-type ${contentType} not supported.`)
    }
}

/**
 * Extract the actual message payload, assuming the reply came in JSON format.
 * @param response as the HTTP payload
 * @returns {Promise<T | never>}
 */
function extractJsonObject(response) {
    return response.json()
        .then(json => {
            if (response.ok) {
                return json
            } else {
                return Promise.reject(Object.assign({}, json, {
                    status: response.status,
                    statusText: response.statusText
                }))
            }
        })
}