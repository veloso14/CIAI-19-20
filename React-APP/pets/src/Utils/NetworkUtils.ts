
export function getData<T>(url:string, defaultValue:T):Promise<T> {
    let auth = {};
    let token = localStorage.getItem('jwt');
    if (token) auth = {'Authorization':token};

    // sign out in case of unauthorized access (expired session)
    return fetch(url, {
        method: "GET",
        mode: "cors",
        cache: "no-cache",
        headers: {
            ...auth,
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.ok)
                return response.json();
            else {
                console.log(`Error: ${response.status}: ${response.statusText}`);
                return new Promise<T>((resolve, reject) => resolve(defaultValue))
            }
        })
        .catch(reason => {
            console.log(reason);
        });
}
