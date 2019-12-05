import {Action} from "redux";
import {SIGN_IN, SIGN_OUT, SignInAction} from "../actions/SignInAction";
import {getData} from "../Utils/NetworkUtils";
import {Client} from "../pages/ClientPage";


function checkIfTokenIsValid() {
    return localStorage.getItem('jwt') != null;
}

function decodeUsername() {
    const jwt = require("jsonwebtoken");
    const token = localStorage.getItem('jwt');
    if (token) {
        const t = token.slice(7, token.length);
        const decode = jwt.decode(t);
        return decode.username
    } else {
        return ""
    }
}

const initialState = {isSignedIn: checkIfTokenIsValid(), currentUser: decodeUsername()};

function signInReducer(state = initialState, action: Action) {
    switch (action.type) {
        case SIGN_IN:

            let token = (action as SignInAction).data;
            if (token) {
                localStorage.setItem('jwt', token);
                const username = decodeUsername();
                return {...state, isSignedIn: true, currentUser: username};
            } else {
                return state;
            }
        case SIGN_OUT:
            localStorage.removeItem('jwt');
            return {...state, isSignedIn: false};
        default:
            return state;
    }
}

export default signInReducer