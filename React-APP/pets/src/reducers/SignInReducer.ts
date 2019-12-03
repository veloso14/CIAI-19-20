import {Action} from "redux";
import {SIGN_IN, SIGN_OUT, SignInAction} from "../actions/SignInAction";

function checkIfTokenIsValid() {
    return localStorage.getItem('jwt') != null;
}

const initialState = {isSignedIn: checkIfTokenIsValid() };

function signInReducer(state = initialState, action:Action) {
    switch (action.type) {
        case SIGN_IN:
            let token = (action as SignInAction).data;
            if( token ) {
                localStorage.setItem('jwt',token);
                return {...state, isSignedIn: true};
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