
export function getData<T>(url:string, defaultValue:T):Promise<T> {

    let token = localStorage.getItem('jwt');
    // sign out in case of unauthorized access (expired session)
    return fetch(url, {
        method: "GET",
        mode: "cors",
        cache: "no-cache",
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token,

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
