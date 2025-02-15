const ofRandom = (arr) => {
    return arr[Math.floor(Math.random() * arr.length)];
}

const isLoggedIn = async () => {
    const resp = await fetch("https://cs571.org/api/s24/hw11/whoami", {
        credentials: "include",
        headers: {
            "X-CS571-ID": 'bid_63ed070a4e5af5e79ef6893489cebe2f86e0fb75fcf2d0e6577911e9f14d0cce'
        }
    })
    const body = await resp.json();
    return body.isLoggedIn;
}

const getLoggedInUsername = async () => {
    const resp = await fetch("https://cs571.org/api/s24/hw11/whoami", {
        credentials: "include",
        headers: {
            "X-CS571-ID": 'bid_63ed070a4e5af5e79ef6893489cebe2f86e0fb75fcf2d0e6577911e9f14d0cce'
        }
    })
    const body = await resp.json();
    if (body.isLoggedIn) {
        return body.user.username;
    } else {
        return undefined;
    }
}

const logout = async () => {
    await fetch("https://cs571.org/api/s24/hw11/logout", {
        method: "POST",
        credentials: "include",
        headers: {
            "X-CS571-ID": 'bid_63ed070a4e5af5e79ef6893489cebe2f86e0fb75fcf2d0e6577911e9f14d0cce'
        }
    })
}

export {
    ofRandom,
    isLoggedIn,
    getLoggedInUsername,
    logout
}